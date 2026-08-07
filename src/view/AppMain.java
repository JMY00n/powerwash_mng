package view;

import javax.swing.UIManager;

public class AppMain {
    public static void main(String[] args) {
    	try {
	        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
        // 스윙의 스레드 안전성을 위해 EventQueue 사용 권장
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}