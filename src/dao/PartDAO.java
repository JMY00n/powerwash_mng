package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.PartDTO;
import util.DBConnection;

public class PartDAO {

    // 1) 전체 부품 조회
    public List<PartDTO> getAllParts() {

        List<PartDTO> list = new ArrayList<>();

        String sql =
            "SELECT part_id, name, stock, image_name, " +
            "manufacturer, supplier, unit_price, safety_stock, description " +
            "FROM parts";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

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


    // 2) 단일 부품 상세조회
    public PartDTO getPartById(int partId) {

        String sql =
            "SELECT part_id, name, stock, image_name, " +
            "manufacturer, supplier, unit_price, safety_stock, description " +
            "FROM parts WHERE part_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, partId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new PartDTO(
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // 3) 발주(주문) 생성 — ORDER 테이블에 INSERT
    public boolean createOrder(int partId, int amount) {

        String sql =
            "INSERT INTO order_parts (part_id, quantity, status) VALUES (?, ?, 'ORDERED')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, partId);
            pstmt.setInt(2, amount);

            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    // 4) 발주 입고 처리 (재고 증가 + 상태변경)
    public boolean receiveOrder(int orderId, int amount, int partId) {

        String updateOrder = "UPDATE order_parts SET status = 'RECEIVED' WHERE order_id = ?";
        String updateStock = "UPDATE parts SET stock = stock + ? WHERE part_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            // 주문 상태 변경
            try (PreparedStatement pst1 = conn.prepareStatement(updateOrder)) {
                pst1.setInt(1, orderId);
                pst1.executeUpdate();
            }

            // 재고 증가
            try (PreparedStatement pst2 = conn.prepareStatement(updateStock)) {
                pst2.setInt(1, amount);
                pst2.setInt(2, partId);
                pst2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}