package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // 화면 전환을 위한 패널들
    private JPanel contentPanel; // 내용이 표시될 영역
    private ProductionPanel productionPanel; // 제품 생산 화면
    private HomePanel homePanel; // 홈 화면

    public MainFrame() {
        setTitle("202530533 윤정민"); // 제목 변경
        setSize(1300, 800); // 넉넉한 크기
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. 패널 초기화
        productionPanel = new ProductionPanel();
        homePanel = new HomePanel();

        // 2. 좌측 메뉴 (카테고리 선택 시 switchScreen 실행)
        LeftNavPanel leftNav = new LeftNavPanel(category -> switchScreen(category));
        add(leftNav, BorderLayout.WEST);

        // 3. 중앙 콘텐츠 영역
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 246, 250)); // 배경 통일
        add(contentPanel, BorderLayout.CENTER);

        // 4. 초기 화면 설정 (홈 화면)
        switchScreen("홈");

        setVisible(true);
    }

    // 화면 전환 로직
    private void switchScreen(String category) {
        contentPanel.removeAll(); // 기존 화면 제거

        if (category.equals("홈")) {
            // 홈 화면은 스크롤 없이 꽉 차게
            contentPanel.add(homePanel, BorderLayout.CENTER);

        } else if (category.equals("부품")) {
            // 부품 화면 (MaterialPanel 사용하신다고 하셨으므로)
            MaterialPanel mp = new MaterialPanel();
            
            // 스크롤바 추가
            JScrollPane scroll = new JScrollPane(mp);
            scroll.setBorder(null); // 테두리 없애서 깔끔하게
            scroll.getVerticalScrollBar().setUnitIncrement(20); // 스크롤 속도
            contentPanel.add(scroll, BorderLayout.CENTER);

        } else {
            // "전체", "전기식", "연료식" -> ProductionPanel 재활용
            productionPanel.updateCategory(category);
            
            // ProductionPanel도 내용이 많으면 스크롤 필요
            JScrollPane scroll = new JScrollPane(productionPanel);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(20);
            contentPanel.add(scroll, BorderLayout.CENTER);
        }

        // 화면 갱신
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}