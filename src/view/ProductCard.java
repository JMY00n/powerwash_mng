package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.function.Consumer;

import dto.ProductDTO;

public class ProductCard extends JPanel {

    public ProductCard(ProductDTO p, Consumer<ProductDTO> onDetail, Consumer<ProductDTO> onProduce) {
        // 카드 크기 및 스타일
        setPreferredSize(new Dimension(240, 320));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        // 그림자 느낌을 내기 위한 테두리
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // 1. 이미지 영역 (상단)
        JLabel imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        imgLabel.setPreferredSize(new Dimension(200, 150));

        URL imgURL = getClass().getResource("/" + p.getImageName());
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(180, 130, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } else {
            imgLabel.setText("No Image");
            imgLabel.setForeground(Color.GRAY);
        }
        add(imgLabel, BorderLayout.CENTER);

        // 2. 정보 및 버튼 영역 (하단)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1, 5, 5)); // 3줄 구성
        infoPanel.setBackground(Color.WHITE);

        // 제품명
        JLabel nameLabel = new JLabel(p.getName(), JLabel.CENTER);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        // 재고 상태 (재고가 없으면 빨간색 강조)
        JLabel stockLabel = new JLabel("재고: " + p.getStock() + "대", JLabel.CENTER);
        stockLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        if (p.getStock() <= 0) {
            stockLabel.setForeground(new Color(231, 76, 60)); // Red
            stockLabel.setText("재고 없음 (생산 필요)");
        } else {
            stockLabel.setForeground(new Color(39, 174, 96)); // Green
        }

        // 버튼 그룹
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton detailBtn = new JButton("상세");
        styleButton(detailBtn, new Color(149, 165, 166)); // Grey
        detailBtn.addActionListener(e -> onDetail.accept(p));

        JButton produceBtn = new JButton("생산");
        styleButton(produceBtn, new Color(52, 152, 219)); // Blue
        produceBtn.addActionListener(e -> onProduce.accept(p));

        btnPanel.add(detailBtn);
        btnPanel.add(produceBtn);

        infoPanel.add(nameLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(btnPanel);

        add(infoPanel, BorderLayout.SOUTH);
    }

    // 버튼 스타일 꾸미기
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}