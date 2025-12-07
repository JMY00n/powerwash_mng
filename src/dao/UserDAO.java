package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBConnection;
import dto.UserDTO; // UserDTO import 필요

public class UserDAO {
	
	// 1. 기존에 있던 로그인 체크 기능
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
	} // loginCheck 메서드는 여기서 끝납니다.
	
	// ---------------------------------------------------------
	// ★ 여기에 getUser 메서드를 추가하는 것입니다 (형제처럼 나란히)
	// ---------------------------------------------------------

	// 2. ID로 회원 정보 전체 가져오기 (새로 추가)
	public UserDTO getUser(String id) {
		UserDTO dto = null;
		String sql = "SELECT * FROM user WHERE id = ?"; 

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, id);
			
			try(ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					// DB에서 꺼내서 DTO 그릇에 담기
					dto = new UserDTO();
					dto.setId(rs.getString("id"));
					dto.setPw(rs.getString("pw"));
					dto.setName(rs.getString("name"));
					dto.setRole(rs.getString("role")); // role 컬럼이 있다고 가정
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

} // UserDAO 클래스 끝