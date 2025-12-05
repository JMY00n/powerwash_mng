package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dto.ProductDTO;

public class ProductCard extends JPanel {

    public ProductCard(ProductDTO dto) {

        setPreferredSize(new Dimension(180, 240));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setLayout(null);

        // ------------------------------
        // ⭐ 이미지 로딩 코드 추가된 부분
        // ------------------------------
        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(20, 10, 140, 120);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        try {
            URL imgURL = getClass().getResource("/" + dto.getImageName());
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(140, 120, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            } else {
                imgLabel.setText("이미지 없음");
            }
        } catch (Exception e) {
            e.printStackTrace();
            imgLabel.setText("오류");
        }

        add(imgLabel);

        // ------------------------------
        // 기존 텍스트/버튼 코드
        // ------------------------------
        JLabel lblName = new JLabel(dto.getName());
        lblName.setBounds(20, 140, 150, 20);
        add(lblName);

        JLabel lblStock = new JLabel("재고: " + dto.getStock());
        lblStock.setBounds(20, 160, 150, 20);
        add(lblStock);

        JButton btnDetail = new JButton("상세보기");
        btnDetail.setBounds(10, 190, 80, 30);
        add(btnDetail);

        JButton btnProd = new JButton("생산하기");
        btnProd.setBounds(95, 190, 80, 30);
        add(btnProd);
    }
}