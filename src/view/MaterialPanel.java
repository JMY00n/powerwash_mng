package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

import dao.PartDAO;
import dto.PartDTO;

public class MaterialPanel extends JPanel {

    private PartDAO partDAO = new PartDAO();

    public MaterialPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        refreshData();
    }

    public void refreshData() {

        removeAll();
        List<PartDTO> list = partDAO.getAllParts();

        for (PartDTO p : list) {
            add(createRow(p));
        }

        revalidate();
        repaint();
    }

 // ============================================
    // 부품 Row 생성 (수정됨: 안전재고 추가 + 팝업 위치 해결)
    // ============================================
    private JPanel createRow(PartDTO p) {
        
        // 1. 레이아웃을 GridBagLayout으로 변경 (좌표계산 없이 깔끔하게 정렬)
        JPanel row = new JPanel(new GridBagLayout());
        row.setPreferredSize(new Dimension(1000, 115)); // 높이를 115로 살짝 늘림 (3줄 표시 위해)
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        row.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10, 15, 10, 15); // 안쪽 여백

        // 2. 이미지 영역
        gbc.gridx = 0; gbc.weightx = 0;
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(80, 80));
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        URL imgURL = getClass().getResource("/" + p.getImageName());
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("NO IMG");
        }
        row.add(imgLabel, gbc);

        // 3. 텍스트 정보 영역 (3줄로 변경)
        gbc.gridx = 1; gbc.weightx = 1.0; // 남은 공간 꽉 채우기
        gbc.fill = GridBagConstraints.BOTH;
        
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 2)); // 3줄 구성
        textPanel.setBackground(Color.WHITE);

        // (1) 이름
        JLabel name = new JLabel(p.getName());
        name.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        
        // (2) 재고
        JLabel stock = new JLabel("현재 재고: " + p.getStock() + "개");
        stock.setForeground(new Color(0, 70, 150)); // 파란색
        stock.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        
        // (3) ★ 안전재고 (빨간색 추가) ★
        JLabel safety = new JLabel("안전재고: " + p.getSafetyStock() + "개");
        safety.setForeground(new Color(231, 76, 60)); // 빨간색
        safety.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        textPanel.add(name);
        textPanel.add(stock);
        textPanel.add(safety);
        
        row.add(textPanel, gbc);

        // 4. 버튼 영역
        gbc.gridx = 2; gbc.weightx = 0;
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        btnPanel.setBackground(Color.WHITE);

        JButton order = new JButton("주문하기");
        styleMiniButton(order, new Color(46, 204, 113)); // 초록색 버튼 스타일
        
        // ★ 팝업 위치 해결 (getWindowAncestor)
        order.addActionListener(e -> {
            Window parent = SwingUtilities.getWindowAncestor(this); // 부모 창 찾기
            String qty = JOptionPane.showInputDialog(parent, p.getName() + " 주문 수량 입력:");
            
            if (qty != null && !qty.isEmpty()) {
                try {
                    int amount = Integer.parseInt(qty);
                    partDAO.addStock(p.getPartId(), amount);
                    JOptionPane.showMessageDialog(parent, "주문(입고) 완료!");
                    refreshData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "숫자만 입력하세요.");
                }
            }
        });

        JButton detail = new JButton("상세보기");
        styleMiniButton(detail, new Color(149, 165, 166)); // 회색 버튼 스타일
        
        // ★ 팝업 위치 해결 (getWindowAncestor)
        detail.addActionListener(e -> {
            Window parent = SwingUtilities.getWindowAncestor(this); // 부모 창 찾기
            String info = 
                "부품명: " + p.getName() + "\n" +
                "부품코드: P-" + p.getPartId() + "\n" +
                "제조사: " + p.getManufacturer() + "\n" +
                "공급사: " + p.getSupplier() + "\n" +
                "단가: " + p.getUnitPrice() + "원\n" +
                "안전재고: " + p.getSafetyStock() + "개\n" +
                "설명:\n" + p.getDescription();
            
            JOptionPane.showMessageDialog(parent, info, "부품 상세정보", JOptionPane.INFORMATION_MESSAGE);
        });

        btnPanel.add(order);
        btnPanel.add(detail);
        row.add(btnPanel, gbc);

        return row;
    }

    // 버튼 예쁘게 꾸며주는 헬퍼 메서드 (없으면 추가하세요)
    private void styleMiniButton(JButton btn, Color c) {
        btn.setBackground(c);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        btn.setPreferredSize(new Dimension(100, 30));
    }
}
