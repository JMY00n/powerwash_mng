package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contentPanel;
    private ProductionPanel productionPanel;
    private HomePanel homePanel;

    public MainFrame() {

        setTitle("고압세척기 생산관리 ERP");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 좌측 메뉴
        LeftNavPanel leftNav = new LeftNavPanel(category -> switchScreen(category));
        add(leftNav, BorderLayout.WEST);

        // 중앙 화면
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        // 패널 준비
        productionPanel = new ProductionPanel();
        homePanel = new HomePanel();

        // 기본 홈 화면
        showHomeScreen();

        setVisible(true);
    }

    private void switchScreen(String category) {

        contentPanel.removeAll();

        if (category.equals("전체") || category.equals("전기식") || category.equals("연료식")) {

            productionPanel.updateCategory(category);
            contentPanel.add(productionPanel, BorderLayout.CENTER);

        } else if (category.equals("부품")) {

            // 부품 화면은 스크롤 + MaterialPanel
            MaterialPanel mp = new MaterialPanel();

            JScrollPane scroll = new JScrollPane(mp);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scroll.getVerticalScrollBar().setUnitIncrement(20);

            contentPanel.add(scroll, BorderLayout.CENTER);

        } else {
            // 홈 화면
            contentPanel.add(homePanel, BorderLayout.CENTER);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showHomeScreen() {
        contentPanel.removeAll();
        contentPanel.add(homePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
