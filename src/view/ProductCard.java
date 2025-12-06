package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

import dao.ProductDAO;
import dto.ProductDTO;
import dto.PartDTO;

public class ProductCard extends JPanel {

    private final ProductDTO product;
    private final ProductDAO productDAO;
    private final Runnable refreshCallback; // 생산 후 목록 새로고침용

    public ProductCard(ProductDTO dto, ProductDAO dao, Runnable refreshCallback) {
        this.product = dto;
        this.productDAO = dao;
        this.refreshCallback = refreshCallback;

        setPreferredSize(new Dimension(220, 280));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 이미지 영역
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        URL imgURL = getClass().getResource("/" + product.getImageName());
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("이미지 없음");
        }

        add(imgLabel, BorderLayout.CENTER);

        // 아래 정보 + 버튼 영역
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(product.getName(), JLabel.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 13f));

        JLabel stockLabel = new JLabel("재고: " + product.getStock() + "대", JLabel.CENTER);
        stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stockLabel.setForeground(new Color(0, 70, 150));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton detailBtn = new JButton("상세보기");
        JButton produceBtn = new JButton("생산하기");

        detailBtn.setPreferredSize(new Dimension(100, 35));
        produceBtn.setPreferredSize(new Dimension(100, 35));

        btnPanel.add(detailBtn);
        btnPanel.add(produceBtn);

        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(nameLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(btnPanel);

        add(infoPanel, BorderLayout.SOUTH);

        // 이벤트 연결
        detailBtn.addActionListener(e -> showDetailDialog());
        produceBtn.addActionListener(e -> doProduce());
    }

    // 상세보기 다이얼로그
    private void showDetailDialog() {
        Window window = SwingUtilities.getWindowAncestor(this);

        JDialog dialog = new JDialog(window, product.getName() + " 구성 부품", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(700, 400);
        dialog.setLayout(new GridLayout(0, 4, 10, 10));
        dialog.getContentPane().setBackground(Color.WHITE);

        List<PartDTO> parts = productDAO.getProductRecipe(product.getProductId());

        if (parts.isEmpty()) {
            dialog.setLayout(new BorderLayout());
            JLabel msg = new JLabel("등록된 부품 정보가 없습니다.", JLabel.CENTER);
            dialog.add(msg, BorderLayout.CENTER);
        } else {
            for (PartDTO p : parts) {
                JPanel partPanel = new JPanel(new BorderLayout());
                partPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                partPanel.setBackground(Color.WHITE);
                partPanel.setPreferredSize(new Dimension(150, 160));

                JLabel imgLabel = new JLabel();
                imgLabel.setHorizontalAlignment(JLabel.CENTER);

                URL imgURL = getClass().getResource("/" + p.getImageName());
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(img));
                } else {
                    imgLabel.setText("IMG");
                }

                JLabel nameLabel = new JLabel(p.getName(), JLabel.CENTER);
                nameLabel.setFont(nameLabel.getFont().deriveFont(11f));

                JLabel qtyLabel = new JLabel("필요: " + p.getStock() + "개", JLabel.CENTER);
                qtyLabel.setForeground(new Color(0, 70, 150));
                qtyLabel.setFont(qtyLabel.getFont().deriveFont(11f));

                JPanel textPanel = new JPanel(new GridLayout(2, 1));
                textPanel.setBackground(Color.WHITE);
                textPanel.add(nameLabel);
                textPanel.add(qtyLabel);

                partPanel.add(imgLabel, BorderLayout.CENTER);
                partPanel.add(textPanel, BorderLayout.SOUTH);

                dialog.add(partPanel);
            }
        }

        dialog.setLocationRelativeTo(window);
        dialog.setVisible(true);
    }

    // 생산하기 로직
    private void doProduce() {
        Window window = SwingUtilities.getWindowAncestor(this);
        String input = JOptionPane.showInputDialog(
            window,
            product.getName() + " 생산 수량을 입력하세요:"
        );

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            int amount = Integer.parseInt(input.trim());
            String result = productDAO.produceProduct(product.getProductId(), amount);

            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(window, amount + "대 생산 완료!");
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
            } else {
                JOptionPane.showMessageDialog(window, "생산 실패: " + result,
                        "경고", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(window, "숫자만 입력해주세요.");
        }
    }
}
