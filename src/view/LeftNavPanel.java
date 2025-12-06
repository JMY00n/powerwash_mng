package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class LeftNavPanel extends JPanel {

    // 디자인 색상 상수 (다크 테마)
    private static final Color BG_COLOR = new Color(44, 62, 80);
    private static final Color HOVER_COLOR = new Color(52, 73, 94);
    private static final Color TEXT_COLOR = Color.WHITE;

    public LeftNavPanel(Consumer<String> onCategorySelected) {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setPreferredSize(new Dimension(200, 0)); // 너비를 살짝 넓힘 (200px)

        // 1. 상단 로고/타이틀
        JLabel titleLabel = new JLabel("ERP SYSTEM", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 2. 메뉴 버튼 영역
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(10, 1, 0, 0)); // 간격 없이
        menuPanel.setBackground(BG_COLOR);

        // "홈" 버튼 추가
        menuPanel.add(createMenuButton("홈", onCategorySelected));

        // 기존 카테고리 버튼들
        String[] menus = {"전체", "전기식", "연료식", "부품"};
        for (String m : menus) {
            menuPanel.add(createMenuButton(m, onCategorySelected));
        }

        add(menuPanel, BorderLayout.CENTER);

        // 3. 하단 종료 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton exitBtn = new JButton("시스템 종료");
        styleExitButton(exitBtn);
        exitBtn.addActionListener(e -> System.exit(0));
        
        bottomPanel.add(exitBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 메뉴 버튼 생성 헬퍼
    private JButton createMenuButton(String text, Consumer<String> action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        btn.setForeground(TEXT_COLOR);
        btn.setBackground(BG_COLOR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 0)); // 패딩

        // 마우스 호버 효과
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(HOVER_COLOR);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(BG_COLOR);
            }
        });

        btn.addActionListener(e -> action.accept(text));
        return btn;
    }

    // 종료 버튼 스타일
    private void styleExitButton(JButton btn) {
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        btn.setBackground(new Color(231, 76, 60)); // 빨간색
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}