package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HomePanel extends JPanel {

    public HomePanel() {
        // 생산관리 패널과 배경색 통일 (연한 회색)
        setBackground(new Color(245, 246, 250));
        setLayout(new BorderLayout());

        URL url = getClass().getResource("/main.png");
        
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            JLabel imgLabel = new JLabel(icon);
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(imgLabel, BorderLayout.CENTER);
        } else {
            JLabel errLabel = new JLabel("이미지를 찾을 수 없습니다.", SwingConstants.CENTER);
            add(errLabel, BorderLayout.CENTER);
        }
    }
}