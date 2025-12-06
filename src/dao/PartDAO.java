package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.PartDTO;
import util.DBConnection;

public class PartDAO {

    // 전체 부품 조회
    public List<PartDTO> getAllParts() {

        List<PartDTO> list = new ArrayList<>();

        String sql =
            "SELECT part_id, name, stock, image_name, " +
            "manufacturer, supplier, unit_price, safety_stock, description " +
            "FROM parts";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                PartDTO dto = new PartDTO(
                    rs.getInt("part_id"),
                    rs.getString("name"),
                    rs.getInt("stock"),
                    rs.getString("image_name"),
                    rs.getString("manufacturer"),
                    rs.getString("supplier"),
                    rs.getInt("unit_price"),
                    rs.getInt("safety_stock"),
                    rs.getString("description")
                );
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 재고를 amount 만큼 추가 (주문 입고 비슷하게 사용)
    public boolean addStock(int partId, int amount) {

        String sql = "UPDATE parts SET stock = stock + ? WHERE part_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, amount);
            pstmt.setInt(2, partId);

            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
