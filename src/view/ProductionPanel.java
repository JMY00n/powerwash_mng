package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

import dao.ProductDAO;
import dto.ProductDTO;
import dto.PartDTO;

public class ProductionPanel extends JPanel {

    private ProductDAO productDAO = new ProductDAO();
    private String currentCategory = "전체";

    public ProductionPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));
        setBackground(Color.WHITE);
    }

    public void updateCategory(String category) {
        this.currentCategory = category;
        refreshData();
    }

    private void refreshData() {

        removeAll();

        List<ProductDTO> products;

        if (currentCategory.equals("전체")) {
            products = productDAO.getAll();
        } else {
            products = productDAO.getByType(currentCategory);
        }

        for (ProductDTO p : products) {
            add(createCard(p));
        }

        revalidate();
        repaint();
    }

    private JPanel createCard(ProductDTO p) {

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(220, 300));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(Color.WHITE);

        // 이미지
        JLabel img = new JLabel();
        img.setBounds(30, 10, 160, 130);
        img.setHorizontalAlignment(JLabel.CENTER);

        URL url = getClass().getResource("/" + p.getImageName());
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image scaled = icon.getImage().getScaledInstance(160, 130, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(scaled));
        }
        panel.add(img);

        // 이름
        JLabel name = new JLabel(p.getName(), JLabel.CENTER);
        name.setBounds(10, 150, 200, 22);
        panel.add(name);

        // 재고
        JLabel stock = new JLabel("재고: " + p.getStock() + "대", JLabel.CENTER);
        stock.setForeground(new Color(0, 70, 150));
        stock.setBounds(10, 175, 200, 22);
        panel.add(stock);

        // 상세보기
        JButton detail = new JButton("상세보기");
        detail.setBounds(20, 215, 85, 30);
        detail.addActionListener(e -> showDetailDialog(p));
        panel.add(detail);

        // 생산하기
        JButton produce = new JButton("생산하기");
        produce.setBounds(115, 215, 85, 30);
        produce.addActionListener(e -> produceProduct(p));
        panel.add(produce);

        return panel;
    }

    // -------------------------------
    // 제품 상세 (구성 부품 다이얼로그)
    // -------------------------------
    private void showDetailDialog(ProductDTO product) {

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                product.getName() + " 구성 부품",
                true
        );
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        List<PartDTO> parts = productDAO.getProductRecipe(product.getProductId());

        if (parts.isEmpty()) {
            dialog.add(new JLabel("구성 부품 정보가 없습니다."));
        } else {

            for (PartDTO p : parts) {

                JPanel card = new JPanel(null);
                card.setPreferredSize(new Dimension(140, 150));
                card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                card.setBackground(Color.WHITE);

                JLabel img = new JLabel();
                img.setBounds(25, 10, 90, 60);

                URL url = getClass().getResource("/" + p.getImageName());
                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    Image sc = icon.getImage().getScaledInstance(90, 60, Image.SCALE_SMOOTH);
                    img.setIcon(new ImageIcon(sc));
                }
                card.add(img);

                JLabel name = new JLabel(p.getName(), JLabel.CENTER);
                name.setBounds(5, 80, 130, 20);
                card.add(name);

                JLabel qty = new JLabel("필요: " + p.getStock() + "개", JLabel.CENTER); // stock필드에 need_qty 넣어둔 상태
                qty.setBounds(5, 100, 130, 20);
                qty.setForeground(new Color(0, 70, 150));
                card.add(qty);

                dialog.add(card);
            }
        }

        dialog.setVisible(true);
    }

    // -------------------------------
    // 생산하기 (+ 부족 부품 영수증)
    // -------------------------------
    private void produceProduct(ProductDTO p) {

        String input = JOptionPane.showInputDialog(
                this,
                p.getName() + " 생산 수량 입력:"
        );

        if (input == null || input.isEmpty()) return;

        try {
            int amount = Integer.parseInt(input);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "1 이상 수량을 입력하세요.");
                return;
            }

            // ❗ 여기서 기존 produceProduct(String) 대신
            //    상세 결과를 주는 produceProductDetailed 사용
            ProductDAO.ProductionResult result =
                    productDAO.produceProductDetailed(p.getProductId(), amount);

            if (result.success) {
                JOptionPane.showMessageDialog(this, amount + "대 생산 완료!");
                refreshData();
            } else {
                // 부족 부품 리스트가 있을 때: 확인 / 자세히
                if (result.lackList != null && !result.lackList.isEmpty()) {

                    Object[] options = {"자세히", "확인"};
                    int choice = JOptionPane.showOptionDialog(
                            this,
                            "생산 실패: 부품 재고 부족",
                            "생산 실패",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[1]   // 기본 선택: 확인
                    );

                    if (choice == 0) { // "자세히"
                        StringBuilder sb = new StringBuilder();
                        sb.append("부족 부품 내역\n\n");
                        for (ProductDAO.LackItem item : result.lackList) {
                            sb.append("- ")
                              .append(item.partName)
                              .append(" : ")
                              .append(item.lackAmount)
                              .append("개 부족\n");
                        }

                        JOptionPane.showMessageDialog(
                                this,
                                sb.toString(),
                                "부족 부품 상세",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                } else {
                    // lackList가 비어 있으면 그냥 일반 에러
                    JOptionPane.showMessageDialog(
                            this,
                            "생산 실패: 부품 재고 부족 또는 오류가 발생했습니다."
                    );
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "숫자를 입력하세요.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "알 수 없는 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
