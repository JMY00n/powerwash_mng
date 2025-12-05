package view;

import javax.swing.*;
import dao.UserDAO;

public class LoginFrame extends JFrame {

    private JTextField tfId;
    private JPasswordField tfPw;
    private UserDAO dao = new UserDAO();

    public LoginFrame() {
        setTitle("로그인");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblId = new JLabel("아이디");
        lblId.setBounds(30, 20, 80, 25);
        add(lblId);

        tfId = new JTextField();
        tfId.setBounds(100, 20, 150, 25);
        add(tfId);

        JLabel lblPw = new JLabel("비밀번호");
        lblPw.setBounds(30, 60, 80, 25);
        add(lblPw);

        tfPw = new JPasswordField();
        tfPw.setBounds(100, 60, 150, 25);
        add(tfPw);

        JButton btnLogin = new JButton("로그인");
        btnLogin.setBounds(100, 100, 150, 25);
        add(btnLogin);

        btnLogin.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        String id = tfId.getText();
        String pw = new String(tfPw.getPassword());

        if (dao.loginCheck(id, pw)) {
            JOptionPane.showMessageDialog(this, "로그인 성공");
            new MainFrame();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "로그인 실패");
        }
    }
}