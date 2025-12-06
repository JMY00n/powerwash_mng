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
        setLayout(new GridLayout(0, 4, 20, 20)); // 부품도 4개씩
        setBackground(Color.WHITE);
        refreshData();
    }

    public void refreshData() {
        removeAll();
        List<PartDTO> parts = partDAO.getAllParts();

        for (PartDTO p : parts) {
            add(createPartCard(p));
        }

        revalidate();
        repaint();
    }

    private JPanel createPartCard(PartDTO p) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(220, 200));

        // 이미지
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        URL imgURL = getClass().getResource("/" + p.getImageName());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("IMG");
        }

        panel.add(imgLabel, BorderLayout.CENTER);

        // 아래 정보 + 버튼
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(p.getName(), JLabel.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel stockLabel = new JLabel("재고: " + p.getStock() + "개", JLabel.CENTER);
        stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (p.getStock() < p.getSafetyStock()) {
            stockLabel.setForeground(Color.RED);
        } else {
            stockLabel.setForeground(new Color(0, 70, 150));
        }

        JButton orderBtn = new JButton("주문하기");
        orderBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderBtn.setPreferredSize(new Dimension(100, 30));

        orderBtn.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            String input = JOptionPane.showInputDialog(
                window,
                p.getName() + " 주문 수량을 입력하세요:"
            );

            if (input == null || input.trim().isEmpty()) return;

            try {
                int amount = Integer.parseInt(input.trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(window, "1 이상 입력해주세요.");
                    return;
                }

                if (partDAO.addStock(p.getPartId(), amount)) {
                    JOptionPane.showMessageDialog(window, "입고 처리 완료!");
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(window, "입고 처리 실패");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(window, "숫자만 입력해주세요.");
            }
        });

        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(nameLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(orderBtn);
        infoPanel.add(Box.createVerticalStrut(5));

        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }
}
