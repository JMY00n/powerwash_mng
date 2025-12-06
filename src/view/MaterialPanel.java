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

    private JPanel createRow(PartDTO p) {

        JPanel row = new JPanel(null);
        row.setPreferredSize(new Dimension(1000, 100));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        row.setBackground(Color.WHITE);

        // 이미지
        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(10, 10, 80, 80);
        imgLabel.setHorizontalAlignment(JLabel.CENTER);

        URL imgURL = getClass().getResource("/" + p.getImageName());
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        }

        row.add(imgLabel);

        // 이름
        JLabel name = new JLabel(p.getName());
        name.setBounds(110, 10, 350, 25);
        name.setFont(name.getFont().deriveFont(Font.BOLD, 14f));
        row.add(name);

        // 재고
        JLabel stock = new JLabel("재고: " + p.getStock() + "개");
        stock.setBounds(110, 40, 300, 20);
        stock.setForeground(new Color(0, 70, 150));
        row.add(stock);

        // 주문하기
        JButton order = new JButton("주문하기");
        order.setBounds(750, 20, 120, 30);
        order.addActionListener(e -> {

            String qty = JOptionPane.showInputDialog(
                    this,
                    p.getName() + " 주문 수량 입력:"
            );

            if (qty == null || qty.isEmpty()) return;

            try {
                int amount = Integer.parseInt(qty);
                partDAO.addStock(p.getPartId(), amount);
                JOptionPane.showMessageDialog(this, "주문(입고) 완료!");

                refreshData();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "숫자를 입력하세요");
            }
        });
        row.add(order);

        // 상세보기
        JButton detail = new JButton("상세보기");
        detail.setBounds(750, 60, 120, 30);
        detail.addActionListener(e -> {

            String info =
                    "부품명: " + p.getName() + "\n" +
                    "부품코드: P-" + p.getPartId() + "\n" +
                    "제조사: " + p.getManufacturer() + "\n" +
                    "공급사: " + p.getSupplier() + "\n" +
                    "단가: " + p.getUnitPrice() + "원\n" +
                    "안전재고: " + p.getSafetyStock() + "개\n" +
                    "설명:\n" + p.getDescription();

            JOptionPane.showMessageDialog(this, info, "부품 상세정보", JOptionPane.INFORMATION_MESSAGE);
        });

        row.add(detail);

        return row;
    }
}
