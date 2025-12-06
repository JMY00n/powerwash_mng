package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import dao.PartDAO;
import dao.ProductDAO;
import dto.PartDTO;
import dto.ProductDTO;

public class ProductionPanel extends JPanel {

    private ProductDAO productDAO = new ProductDAO();
    private PartDAO partDAO = new PartDAO();

    private String currentCategory = "전체";
    private JPanel cardsPanel;

    public ProductionPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        cardsPanel = new JPanel();
        cardsPanel.setBackground(Color.WHITE);

        add(cardsPanel, BorderLayout.CENTER);
    }

    /** LeftNavPanel에서 호출됨 */
    public void updateCategory(String category) {
        this.currentCategory = category;
        refreshData();
    }

    /** 화면 갱신 */
    public void refreshData() {

        cardsPanel.removeAll();

        if (currentCategory.equals("부품")) {
            // 부품은 카톡 리스트 형식
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            showParts();
        } else {
            // 제품은 카드 바둑판 + 가운데 정렬
            cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));
            showProducts();
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    // ============================================
    // 제품 목록 표시
    // ============================================
    private void showProducts() {
        List<ProductDTO> products;

        if (currentCategory.equals("전체")) {
            products = productDAO.getAll();
        } else {
            products = productDAO.getByType(currentCategory);
        }

        for (ProductDTO p : products) {
            cardsPanel.add(createProductCard(p));
        }
    }

    // ============================================
    // 제품 카드 UI
    // ============================================
    private JPanel createProductCard(ProductDTO p) {

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(220, 300));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(Color.WHITE);

        // 이미지
        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(30, 10, 160, 130);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        URL imgURL = getClass().getResource("/" + p.getImageName());
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(160, 130, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("IMG");
        }
        panel.add(imgLabel);

        // 이름
        JLabel nameLabel = new JLabel(p.getName(), JLabel.CENTER);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));
        nameLabel.setBounds(10, 150, 200, 25);
        panel.add(nameLabel);

        // 재고
        JLabel stockLabel = new JLabel("재고: " + p.getStock() + "대", JLabel.CENTER);
        stockLabel.setBounds(10, 175, 200, 25);
        stockLabel.setForeground(new Color(0, 70, 150));
        panel.add(stockLabel);

        // 상세보기 버튼
        JButton detailBtn = new JButton("상세보기");
        detailBtn.setBounds(15, 215, 90, 35);
        detailBtn.addActionListener(e -> showDetailDialog(p));
        panel.add(detailBtn);

        // 생산하기 버튼
        JButton produceBtn = new JButton("생산하기");
        produceBtn.setBounds(115, 215, 90, 35);
        produceBtn.addActionListener(e -> produce(p));
        panel.add(produceBtn);

        return panel;
    }

    // ============================================
    // 부품 목록 표시
    // ============================================
    private void showParts() {
        List<PartDTO> parts = partDAO.getAllParts();

        if (parts.isEmpty()) {
            JLabel label = new JLabel("등록된 부품이 없습니다.");
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardsPanel.add(label);
            return;
        }

        for (PartDTO part : parts) {
            cardsPanel.add(createPartRow(part));
        }
    }

    // ============================================
    // 부품 Row (카톡 리스트)
    // ============================================
    private JPanel createPartRow(PartDTO part) {

        JPanel row = new JPanel(null);
        row.setPreferredSize(new Dimension(900, 100));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        row.setBackground(Color.WHITE);

        // 이미지
        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(10, 10, 80, 80);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        URL imgURL = getClass().getResource("/" + part.getImageName());
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        }
        row.add(imgLabel);

        // 이름
        JLabel nameLabel = new JLabel(part.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 13f));
        nameLabel.setBounds(110, 10, 350, 25);
        row.add(nameLabel);

        // 재고
        JLabel stockLabel = new JLabel("재고: " + part.getStock() + "개");
        stockLabel.setForeground(new Color(0, 70, 150));
        stockLabel.setBounds(110, 40, 200, 20);
        row.add(stockLabel);

        // 주문하기 버튼
        JButton orderBtn = new JButton("주문하기");
        orderBtn.setBounds(650, 18, 120, 30);
        orderBtn.addActionListener(e -> orderPart(part));
        row.add(orderBtn);

        // 상세보기 버튼
        JButton infoBtn = new JButton("상세보기");
        infoBtn.setBounds(650, 55, 120, 30);
        infoBtn.addActionListener(e -> showPartInfo(part));
        row.add(infoBtn);

        return row;
    }

    // ============================================
    // 제품 상세보기 (부품 구성)
    // ============================================
    private void showDetailDialog(ProductDTO p) {

        JDialog dialog = new JDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            p.getName() + " 구성 부품",
            true
        );

        dialog.setSize(650, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        List<PartDTO> parts = productDAO.getProductRecipe(p.getProductId());

        if (parts.isEmpty()) {
            dialog.add(new JLabel("등록된 부품 정보가 없습니다."));
        } else {
            for (PartDTO part : parts) {
                dialog.add(createRecipeCard(part));
            }
        }

        dialog.setVisible(true);
    }

    private JPanel createRecipeCard(PartDTO part) {

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(140, 150));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(Color.WHITE);

        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(25, 10, 90, 60);

        URL imgURL = getClass().getResource("/" + part.getImageName());
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(90, 60, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        }
        panel.add(imgLabel);

        JLabel nameLabel = new JLabel(part.getName(), JLabel.CENTER);
        nameLabel.setBounds(5, 80, 130, 20);
        panel.add(nameLabel);

        JLabel qtyLabel = new JLabel("필요: " + part.getStock() + "개", JLabel.CENTER);
        qtyLabel.setForeground(new Color(0, 70, 150));
        qtyLabel.setBounds(5, 100, 130, 20);
        panel.add(qtyLabel);

        return panel;
    }

    // ============================================
    // 제품 생산
    // ============================================
    private void produce(ProductDTO p) {

        String input = JOptionPane.showInputDialog(
                this,
                p.getName() + " 생산 수량을 입력하세요:"
        );

        if (input == null || input.isEmpty()) return;

        try {
            int amount = Integer.parseInt(input);

            // 생산 처리 후 결과 가져오기
            var result = productDAO.produceProductDetailed(p.getProductId(), amount);

            if (result.success) {
                JOptionPane.showMessageDialog(this, amount + "대 생산 완료!");
                refreshData();
                return;
            }

            // 실패 시: 커스텀 팝업 실행
            showProductionFailureDialog(result);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "숫자만 입력 가능합니다.");
        }
    }
    
    /** 생산 실패 상세 팝업 */
    private void showProductionFailureDialog(ProductDAO.ProductionResult result) {

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "생산 실패: 부품 재고 부족",
                true
        );

        dialog.setSize(420, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // 메시지
        JLabel msg = new JLabel("부품 재고가 부족하여 생산할 수 없습니다.");
        msg.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        dialog.add(msg, BorderLayout.NORTH);

        // 영수증 스타일 리스트
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

        StringBuilder sb = new StringBuilder();
        sb.append("부족한 부품 목록\n");
        sb.append("---------------------------\n");

        for (var item : result.lackList) {
            sb.append(String.format(
                "%s  → 부족 %d개\n",
                item.partName,
                item.lackAmount
            ));
        }

        area.setText(sb.toString());
        dialog.add(new JScrollPane(area), BorderLayout.CENTER);

        // 버튼 영역
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okBtn = new JButton("확인");
        okBtn.addActionListener(e -> dialog.dispose());
        btnPanel.add(okBtn);

        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }



    // ============================================
    // 부품 주문 (입고 처리)
    // ============================================
    private void orderPart(PartDTO part) {

        String input = JOptionPane.showInputDialog(
                this,
                part.getName() + " 주문 수량 입력:"
        );

        if (input == null || input.isEmpty()) return;

        try {
            int amount = Integer.parseInt(input);

            boolean ok = partDAO.addStock(part.getPartId(), amount);

            if (ok) {
                JOptionPane.showMessageDialog(this,
                    amount + "개 입고 완료!");
                refreshData(); // 자동 새로고침
            } else {
                JOptionPane.showMessageDialog(this,
                    "입고 실패", "오류", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "숫자만 입력하세요.");
        }
    }

    // ============================================
    // 부품 상세정보
    // ============================================
    private void showPartInfo(PartDTO part) {

        String info =
            "부품명: " + part.getName() + "\n" +
            "부품코드: P-" + part.getPartId() + "\n" +
            "제조사: " + part.getManufacturer() + "\n" +
            "공급사: " + part.getSupplier() + "\n" +
            "단가: " + part.getUnitPrice() + "원\n" +
            "안전재고: " + part.getSafetyStock() + "개\n" +
            "설명: " + part.getDescription();

        JOptionPane.showMessageDialog(
            this,
            info,
            "부품 상세정보",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
