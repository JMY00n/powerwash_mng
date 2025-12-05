package view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class LeftNavPanel extends JPanel {

    public LeftNavPanel(Consumer<String> onCategorySelected) {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(180, 0));  // 폭 고정

        // ------- 위쪽 메뉴 영역 -------
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(10, 1, 5, 5));
        menuPanel.setBackground(Color.WHITE);

        String[] menus = {"전체", "전기식", "연료식", "부품"};

        for (String m : menus) {
            JButton btn = new JButton(m);
            btn.addActionListener(e -> onCategorySelected.accept(m));
            menuPanel.add(btn);
        }

        add(menuPanel, BorderLayout.NORTH);

        // ------- 아래쪽 끝내기 버튼 -------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));  
        bottomPanel.setBackground(Color.WHITE);

        JButton exitBtn = new JButton("끝내기");
        exitBtn.addActionListener(e -> System.exit(0));
        bottomPanel.add(exitBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}