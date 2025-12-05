package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import dao.ProductDAO;

public class MainFrame extends JFrame {

    private ProductionPanel productionPanel; 
    private ProductDAO dao = new ProductDAO();

    public MainFrame() {

        setTitle("고압세척기 생산관리 ERP");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ⬅ 왼쪽 메뉴
        LeftNavPanel leftNav = new LeftNavPanel(category -> {
            productionPanel.updateCategory(category);
        });
        add(leftNav, BorderLayout.WEST);

        // 🎯 중앙에 ProductionPanel 하나만!
        productionPanel = new ProductionPanel();
        add(new JScrollPane(productionPanel), BorderLayout.CENTER);

        setVisible(true);
    }
}