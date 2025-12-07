package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.LogDTO;
import util.DBConnection; // 사용하시는 DB 연결 클래스 import
import view.UserSession;  // 방금 만든 세션 클래스 import

public class LogDAO {

    // 1. 일지 기록 (생산 버튼 누를 때 호출)
    public void addLog(String productName, int amount) {
        String sql = "INSERT INTO production_log (emp_id, emp_name, product_name, amount) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // ★ 현재 로그인된 세션 정보를 자동으로 가져옴
            pstmt.setString(1, UserSession.EMP_ID);   
            pstmt.setString(2, UserSession.EMP_NAME); 
            pstmt.setString(3, productName);
            pstmt.setInt(4, amount);
            
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2. 일지 목록 가져오기 (화면에 뿌릴 때 사용)
    public List<LogDTO> getAllLogs() {
        List<LogDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM production_log ORDER BY work_date DESC"; // 최신순 정렬

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LogDTO dto = new LogDTO();
                // LogDTO에 setter가 있다고 가정하고 값 주입
                // (DTO 코드는 아래 4단계 참고)
                dto.setEmpId(rs.getString("emp_id"));
                dto.setEmpName(rs.getString("emp_name"));
                dto.setProductName(rs.getString("product_name"));
                dto.setAmount(rs.getInt("amount"));
                dto.setWorkDate(rs.getTimestamp("work_date")); // Timestamp로 가져옴
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}