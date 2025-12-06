package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dao.PartDAO;
import dto.PartDTO;

public class MaterialPanel extends JPanel {
    private PartDAO partDAO = new PartDAO();

    public MaterialPanel() {
        setLayout(new GridLayout(2, 5, 10, 10)); // 2행 5열
        setBackground(Color.WHITE);
        refreshData();
    }

    public void refreshData() {
        removeAll();
        List<PartDTO> parts = partDAO.getAllParts();

        for (PartDTO p : parts) {
            add(createPartItem(p));
        }
        revalidate();
        repaint();
    }

    private JPanel createPartItem(PartDTO p) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);

        // 1. 이미지
        String imagePath = "images/" + p.getImageName();
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(img));
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        // 2. 정보
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(p.getName(), JLabel.CENTER);
        
        JLabel stockLabel = new JLabel("재고: " + p.getStock(), JLabel.CENTER);
        if(p.getStock() < 10) stockLabel.setForeground(Color.RED); // 재고 부족 시 빨간색

        // 3. 주문 버튼
        JButton orderBtn = new JButton("주문하기");
        orderBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                SwingUtilities.getWindowAncestor(this),
                p.getName() + " 주문 수량을 입력하세요:"
            );

            if (input != null && !input.isEmpty()) {
                try {
                    int amount = Integer.parseInt(input);

                    if (partDAO.createOrder(p.getPartId(), amount)) {
                        JOptionPane.showMessageDialog(
                            SwingUtilities.getWindowAncestor(this),
                            "발주 완료!"
                        );
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "숫자만 입력해주세요.");
                }
            }
        });

        infoPanel.add(nameLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(orderBtn);

        panel.add(imgLabel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
        // 깃 연결 테스트
    }
}
