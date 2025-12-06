package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HomePanel extends JPanel {

    public HomePanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ★ 여기 경로 수정
        URL url = getClass().getResource("/main.png");
        // 디버그용 (원하면 지워도 됨)
        System.out.println("main.png url = " + url);

        ImageIcon icon = new ImageIcon(url);
        JLabel imgLabel = new JLabel(icon);
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(imgLabel, BorderLayout.CENTER);
    }
}
