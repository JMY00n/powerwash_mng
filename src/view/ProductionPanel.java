package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
    private JPanel contentPanel; // 카드가 담길 공간

    public ProductionPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250)); // 배경: 아주 연한 회색 (눈 편안)

        // 상단 헤더 (현재 카테고리 표시)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        headerPanel.setOpaque(false);
        // 필요하면 헤더 내용을 추가할 수 있습니다.
        
        // 메인 컨텐츠 영역 (스크롤은 MainFrame에서 처리하지만, 내부 정렬을 위해 Panel 사용)
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 246, 250)); // 배경 통일
        
        // 기본값 세팅
        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        add(contentPanel, BorderLayout.CENTER);
    }

    public void updateCategory(String category) {
        this.currentCategory = category;
        refreshData();
    }

    public void refreshData() {
        contentPanel.removeAll();

        if (currentCategory.equals("부품")) {
            // 부품: 위에서 아래로 쌓이는 박스 레이아웃
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            showParts();
        } else {
            // 제품: 왼쪽에서 오른쪽으로 흐르는 플로우 레이아웃
            contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 25));
            showProducts();
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ============================================
    // 제품 목록 표시 (ProductCard 사용)
    // ============================================
    private void showProducts() {
        List<ProductDTO> products;
        if (currentCategory.equals("전체")) {
            products = productDAO.getAll();
        } else {
            products = productDAO.getByType(currentCategory);
        }

        for (ProductDTO p : products) {
            // 여기서 ProductCard 생성하며 콜백 메서드(상세보기, 생산하기) 전달
            ProductCard card = new ProductCard(p, 
                this::showDetailDialog, // 상세 버튼 클릭 시 실행
                this::produce           // 생산 버튼 클릭 시 실행
            );
            contentPanel.add(card);
        }
    }

    // ============================================
    // 부품 목록 표시 (리스트 스타일)
    // ============================================
    private void showParts() {
        List<PartDTO> parts = partDAO.getAllParts();
        
        if (parts.isEmpty()) {
            contentPanel.add(new JLabel("등록된 부품이 없습니다."));
            return;
        }

        for (PartDTO part : parts) {
            contentPanel.add(createPartRow(part));
            contentPanel.add(Box.createVerticalStrut(10)); // 아이템 간 간격
        }
    }

    // 부품 Row 생성 (좌표계산 삭제 -> GridBagLayout 사용)
    private JPanel createPartRow(PartDTO part) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1));
        row.setMaximumSize(new Dimension(1000, 100)); // 높이 고정
        row.setPreferredSize(new Dimension(900, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10, 15, 10, 15); // 패딩

        // 1. 이미지
        gbc.gridx = 0; gbc.weightx = 0;
        JLabel imgLabel = new JLabel();
        URL imgURL = getClass().getResource("/" + part.getImageName());
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("IMG");
        }
        row.add(imgLabel, gbc);

        // 2. 이름 및 정보
        gbc.gridx = 1; gbc.weightx = 1.0; // 남은 공간 차지
        gbc.fill = GridBagConstraints.BOTH;
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(part.getName());
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        
        JLabel stockLabel = new JLabel("현재 재고: " + part.getStock() + "개");
        stockLabel.setForeground(new Color(100, 100, 100));
        
        textPanel.add(nameLabel);
        textPanel.add(stockLabel);
        row.add(textPanel, gbc);

        // 3. 버튼들
        gbc.gridx = 2; gbc.weightx = 0;
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        btnPanel.setBackground(Color.WHITE);

        JButton orderBtn = new JButton("주문하기");
        styleMiniButton(orderBtn, new Color(46, 204, 113));
        orderBtn.addActionListener(e -> orderPart(part));

        JButton infoBtn = new JButton("정보확인");
        styleMiniButton(infoBtn, new Color(149, 165, 166));
        infoBtn.addActionListener(e -> showPartInfo(part));

        btnPanel.add(orderBtn);
        btnPanel.add(infoBtn);
        row.add(btnPanel, gbc);

        return row;
    }

    private void styleMiniButton(JButton btn, Color c) {
        btn.setBackground(c);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
    }

    // ============================================
    // 로직 메서드들 (기존 로직 유지)
    // ============================================

    // 제품 상세 (부품 구성)
    private void showDetailDialog(ProductDTO p) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), p.getName() + " 구성 부품", true);
        dialog.setSize(600, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        container.setBackground(Color.WHITE);

        List<PartDTO> parts = productDAO.getProductRecipe(p.getProductId());
        if (parts.isEmpty()) {
            container.add(new JLabel("등록된 부품 정보가 없습니다."));
        } else {
            for (PartDTO part : parts) {
                container.add(createRecipeCard(part));
            }
        }
        dialog.add(new JScrollPane(container));
        dialog.setVisible(true);
    }

    // 레시피 카드 (작은 카드)
    private JPanel createRecipeCard(PartDTO part) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(120, 140));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);

        JLabel imgLabel = new JLabel("", JLabel.CENTER);
        URL imgURL = getClass().getResource("/" + part.getImageName());
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(80, 60, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        }
        panel.add(imgLabel, BorderLayout.CENTER);

        JLabel txtLabel = new JLabel("<html><center>" + part.getName() + "<br><font color='blue'>" + part.getStock() + "개 필요</font></center></html>", JLabel.CENTER);
        panel.add(txtLabel, BorderLayout.SOUTH);

        return panel;
    }

    // 제품 생산 로직
    private void produce(ProductDTO p) {
        String input = JOptionPane.showInputDialog(this, p.getName() + " 생산 수량을 입력하세요:");
        if (input == null || input.isEmpty()) return;

        try {
            int amount = Integer.parseInt(input);
            var result = productDAO.produceProductDetailed(p.getProductId(), amount);

            if (result.success) {
            	new dao.LogDAO().addLog(p.getName(), amount);
                JOptionPane.showMessageDialog(this, amount + "대 생산 완료!");
                refreshData(); 
            } else {
                showProductionFailureDialog(result);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "숫자만 입력 가능합니다.");
        }
    }

    // 생산 실패 팝업
    private void showProductionFailureDialog(ProductDAO.ProductionResult result) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "재고 부족 알림", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JTextArea area = new JTextArea();
        area.setText("   [부족한 부품 목록]\n\n");
        for (var item : result.lackList) {
            area.append(" - " + item.partName + " : " + item.lackAmount + "개 부족\n");
        }
        area.setEditable(false);
        dialog.add(new JScrollPane(area));
        dialog.setVisible(true);
    }

 // ============================================
    // 부품 주문 (입고 처리) - 수정됨
    // ============================================
    private void orderPart(PartDTO part) {
        
        // ★ 핵심: 현재 패널(this)이 아니라, 전체 윈도우를 찾아서 기준으로 삼음
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        String input = JOptionPane.showInputDialog(
                parentWindow, // 여기에 this 대신 parentWindow를 넣으세요
                part.getName() + " 주문 수량 입력:"
        );

        if (input == null || input.isEmpty()) return;

        try {
            int amount = Integer.parseInt(input);

            boolean ok = partDAO.addStock(part.getPartId(), amount);

            if (ok) {
                JOptionPane.showMessageDialog(parentWindow, // 여기도 parentWindow
                    amount + "개 입고 완료!");
                refreshData(); // 자동 새로고침
            } else {
                JOptionPane.showMessageDialog(parentWindow, // 여기도 parentWindow
                    "입고 실패", "오류", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentWindow, "숫자만 입력하세요.");
        }
    }

 // ============================================
    // 부품 상세정보 - 수정됨
    // ============================================
    private void showPartInfo(PartDTO part) {

        // ★ 핵심: 전체 윈도우 기준 잡기
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        String info =
            "부품명: " + part.getName() + "\n" +
            "부품코드: P-" + part.getPartId() + "\n" +
            "제조사: " + part.getManufacturer() + "\n" +
            "공급사: " + part.getSupplier() + "\n" +
            "단가: " + part.getUnitPrice() + "원\n" +
            "안전재고: " + part.getSafetyStock() + "개\n" +
            "설명: " + part.getDescription();

        JOptionPane.showMessageDialog(
            parentWindow, // this 대신 parentWindow 사용
            info,
            "부품 상세정보",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}