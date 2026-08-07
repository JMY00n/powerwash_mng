package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	// 💡 MySQL 접속 정보 (사용자 환경에 맞게 수정하세요)
	private static final String URL = "jdbc:mysql://localhost:3306/erp";
	private static final String USER = "root"; // 또는 본인 MySQL 사용자명
	private static final String PASSWORD = "wjdalsdl!590"; // 본인 비밀번호로 변경

	// JDBC 드라이버 이름 (MySQL 8.0 이상)
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

	// 드라이버 로딩 (최초 1회만 실행)
	static {
		try {
			Class.forName(DRIVER);
			System.out.println("✅ JDBC 드라이버 로딩 성공");
		} catch (ClassNotFoundException e) {
			System.err.println("❌ JDBC 드라이버 로딩 실패: " + DRIVER);
			e.printStackTrace();
		}
	}

	/**
     * MySQL 데이터베이스 연결 객체를 반환합니다.
     * @return Connection 객체, 연결 실패 시 null
     */
    public static Connection getConnection() {
        try {
            // DriverManager를 사용하여 DB에 연결 시도
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // System.out.println("✅ 데이터베이스 연결 성공!"); // 테스트용
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ 데이터베이스 연결 실패!");
            e.printStackTrace();
            return null;
        }
    }

	/**
	 * 사용 후 Connection 객체를 닫아 자원을 해제합니다.
	 * 
	 * @param conn 닫을 Connection 객체
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			System.err.println("Connection 닫기 오류 발생");
			e.printStackTrace();
		}
	}
}
