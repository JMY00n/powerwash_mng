package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBConnection;

public class UserDAO {
	
	public boolean loginCheck(String id, String pw) {
		String sql = "SELECT * FROM user WHERE id=? AND pw=?";
		
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			
			ResultSet rs = pstmt.executeQuery();
			
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
