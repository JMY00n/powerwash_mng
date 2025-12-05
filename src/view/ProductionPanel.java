package view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.SwingUtilities;

import dao.ProductDAO;
import dto.PartDTO;
import dto.ProductDTO;

public class ProductionPanel extends JPanel {

    private ProductDAO productDAO = new ProductDAO();
    private String currentCategory = "전체";

    private JPanel cardsPanel;   // 실제 카드들이 표시되는 영역

    public ProductionPanel() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 중앙 카드 패널
        cardsPanel = new JPanel();
        add(cardsPanel, BorderLayout.CENTER);

        updateCategory("전체");
    }

    /* -----------------------------------------
     * 카테고리 변경 (LeftNavPanel에서 호출됨)
     * ----------------------------------------- */
    public void updateCategory(String category) {
        this.currentCategory = category;
        refreshData();
    }

    /* -----------------------------------------
     * UI 리프레시
     * ----------------------------------------- */
    public void refreshData() {

        cardsPanel.removeAll();

        if (currentCategory.equals("부품")) {
            // 부품 모드 → 카톡 친구 목록 UI
            cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
            showParts();
        } else {
            // 제품 모드 → 카드 격자 배치
            cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
            showProducts();
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    /* -----------------------------------------
     * 제품 목록 표시
     * ----------------------------------------- */
    private void showProducts() {

        List<ProductDTO> products;

        if (currentCategory.equals("전체")) {
            products = productDAO.getAll();
        } else {
            products = productDAO.getByType(currentCategory);
        }

        for (ProductDTO p : products) {
            cardsPanel.add(createProductItem(p));
        }
    }

    /* -----------------------------------------
     * 제품 카드 UI 생성
     * ----------------------------------------- */
    private JPanel createProductItem(ProductDTO p) {

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(200, 280));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(Color.WHITE);

        // 이미지
        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(30, 10, 140, 120);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        URL imgURL = getClass().getResource("/" + p.getImageName());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(140, 110, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("IMG");
        }
        panel.add(imgLabel);

        // 이름
        JLabel nameLabel = new JLabel(p.getName(), JLabel.CENTER);
        nameLabel.setBounds(15, 140, 170, 22);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 13f));
        panel.add(nameLabel);

        // 재고
        JLabel stockLabel = new JLabel("재고: " + p.getStock(), JLabel.CENTER);
        stockLabel.setBounds(15, 165, 170, 22);
        stockLabel.setForeground(new Color(0, 70, 150));
        panel.add(stockLabel);

        // 상세보기
        JButton detailBtn = new JButton("상세보기");
        detailBtn.setBounds(15, 200, 80, 30);
        detailBtn.addActionListener(e -> showDetailDialog(p));
        panel.add(detailBtn);

        // 생산하기
        JButton produceBtn = new JButton("생산하기");
        produceBtn.setBounds(105, 200, 80, 30);
        produceBtn.addActionListener(e -> produce(p));
        panel.add(produceBtn);

        return panel;
    }

    /* -----------------------------------------
     * 부품 목록 표시 (카톡 리스트형)
     * ----------------------------------------- */
    private void showParts() {

        List<PartDTO> parts = productDAO.getAllParts();

        if (parts.isEmpty()) {
            cardsPanel.add(new JLabel("등록된 부품이 없습니다."));
            return;
        }

        for (PartDTO part : parts) {
            cardsPanel.add(createPartRow(part));
        }
    }

    /* -----------------------------------------
     * 부품 Row UI (가로 길쭉한 카톡 친구 스타일)
     * ----------------------------------------- */
    private JPanel createPartRow(PartDTO part) {

        JPanel row = new JPanel(null);
        row.setPreferredSize(new Dimension(800, 100));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        row.setBackground(Color.WHITE);

        // 이미지
        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(10, 10, 80, 80);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        URL imgURL = getClass().getResource("/" + part.getImageName());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("IMG");
        }
        row.add(imgLabel);

        // 이름
        JLabel nameLabel = new JLabel(part.getName());
        nameLabel.setBounds(110, 10, 300, 25);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 13f));
        row.add(nameLabel);

        // 재고
        JLabel stockLabel = new JLabel("재고: " + part.getStock() + "개");
        stockLabel.setBounds(110, 40, 200, 20);
        stockLabel.setForeground(new Color(0, 70, 150));
        row.add(stockLabel);

        // 주문하기 버튼
        JButton orderBtn = new JButton("주문하기");
        orderBtn.setBounds(450, 20, 100, 28);
        orderBtn.addActionListener(e -> orderPart(part));
        row.add(orderBtn);

        // 부품 상세정보 버튼
        JButton infoBtn = new JButton("상세보기");
        infoBtn.setBounds(450, 55, 100, 28);
        infoBtn.addActionListener(e -> showPartInfo(part));
        row.add(infoBtn);

        return row;
    }

    /* -----------------------------------------
     * 제품 상세보기 (레시피 화면)
     * ----------------------------------------- */
    private void showDetailDialog(ProductDTO p) {

        JDialog dialog = new JDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            p.getName() + " 구성 부품",
            true
        );

        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        List<PartDTO> parts = productDAO.getProductRecipe(p.getProductId());

        if (parts.isEmpty()) {
            dialog.add(new JLabel("등록된 부품 정보가 없습니다."));
        } else {
            for (PartDTO part : parts) {
                dialog.add(createPartDetailCard(part));
            }
        }

        dialog.setVisible(true);
    }

    /* -----------------------------------------
     * 레시피 상세보기용 부품 카드
     * ----------------------------------------- */
    private JPanel createPartDetailCard(PartDTO part) {

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(130, 140));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(Color.WHITE);

        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(25, 10, 80, 60);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        URL imgURL = getClass().getResource("/" + part.getImageName());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(80, 60, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        }
        panel.add(imgLabel);

        JLabel nameLabel = new JLabel(part.getName(), JLabel.CENTER);
        nameLabel.setBounds(5, 80, 120, 20);
        panel.add(nameLabel);

        JLabel qtyLabel = new JLabel("필요: " + part.getStock() + "개", JLabel.CENTER);
        qtyLabel.setBounds(5, 100, 120, 20);
        qtyLabel.setForeground(new Color(0, 70, 150));
        panel.add(qtyLabel);

        return panel;
    }

    /* -----------------------------------------
     * 제품 생산 기능
     * ----------------------------------------- */
    private void produce(ProductDTO p) {

        String input = JOptionPane.showInputDialog(
            this,
            p.getName() + " 생산 수량을 입력하세요:"
        );

        if (input == null || input.isEmpty()) return;

        try {
            int amount = Integer.parseInt(input);

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "1 이상의 수량을 입력하세요.");
                return;
            }

            String result = productDAO.produceProduct(p.getProductId(), amount);

            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this, amount + "대 생산 완료!");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "생산 실패: " + result);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "숫자만 입력 가능합니다.");
        }
    }

    /* -----------------------------------------
     * 부품 주문하기
     * ----------------------------------------- */
    private void orderPart(PartDTO part) {

        String qty = JOptionPane.showInputDialog(
            this,
            part.getName() + " 주문 수량을 입력하세요:"
        );

        if (qty == null || qty.isEmpty()) return;

        try {
            int amount = Integer.parseInt(qty);

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "1 이상 입력하세요.");
                return;
            }

            JOptionPane.showMessageDialog(this,
                amount + "개 주문 요청 완료!\n\n※ 실제 발주 기능은 DB 연동하면 구현 가능");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "숫자만 입력 가능합니다.");
        }
    }

    /* -----------------------------------------
     * 부품 상세 정보 팝업
     * ----------------------------------------- */
    private void showPartInfo(PartDTO part) {
        String info =
            "부품명: " + part.getName() + "\n" +
            "부품코드: P-" + part.getPartId() + "\n" +
            "제조사: " + part.getManufacturer() + "\n" +
            "공급사: " + part.getSupplier() + "\n" +
            "단가: " + part.getUnitPrice() + "원\n" +
            "안전재고: " + part.getSafetyStock() + "개\n" +
            "설명: " + part.getDescription() + "\n";

        JOptionPane.showMessageDialog(
            this,
            info,
            "부품 상세정보",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

}