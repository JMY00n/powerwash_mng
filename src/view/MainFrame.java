package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private ProductionPanel productionPanel;

    public MainFrame() {

        setTitle("고압세척기 생산관리 ERP");
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 중앙에 들어갈 패널
        productionPanel = new ProductionPanel();

        // 왼쪽 네비게이션
        LeftNavPanel leftNav = new LeftNavPanel(category -> {
            productionPanel.updateCategory(category);
        });

        add(leftNav, BorderLayout.WEST);

        // 스크롤은 ProductionPanel을 감싸기만 함
        JScrollPane scrollPane = new JScrollPane(productionPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
