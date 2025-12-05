package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.PartDTO;
import dto.ProductDTO;
import util.DBConnection;

public class ProductDAO {

    // -----------------------------
    // 1. 전체 제품 조회
    // -----------------------------
    public List<ProductDTO> getAll() {
        String sql = "SELECT * FROM products";
        return getProductList(sql, null);
    }

    // ProductionPanel 호환용
    public List<ProductDTO> getAllProducts() {
        return getAll();
    }

    // -----------------------------
    // 2. 타입별 제품 조회 (전기식, 연료식 등)
    // -----------------------------
    public List<ProductDTO> getByType(String type) {
        String sql = "SELECT * FROM products WHERE type = ?";
        return getProductList(sql, type);
    }

    // -----------------------------
    // 3. 제품 공통 조회 함수
    // -----------------------------
    private List<ProductDTO> getProductList(String sql, String type) {

        List<ProductDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (type != null) {
                pstmt.setString(1, type);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ProductDTO dto = new ProductDTO(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getInt("stock"),
                    rs.getString("image_name")
                );
                list.add(dto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // -----------------------------
    // 4. 제품 생산 → 제품 재고 증가
    // -----------------------------
    public String produceProduct(int productId, int amount) {

        String sql = "UPDATE products SET stock = stock + ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, amount);
            pstmt.setInt(2, productId);

            int result = pstmt.executeUpdate();

            if (result > 0) return "SUCCESS";
            else return "NO_ROWS_UPDATED";

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    // -----------------------------
    // 5. 제품 레시피 조회 (상세보기용)
    //    production_recipes + parts JOIN
    // -----------------------------
    public List<PartDTO> getProductRecipe(int productId) {

        List<PartDTO> list = new ArrayList<>();

        String sql =
            "SELECT p.part_id, p.name, r.quantity_needed AS qty, p.image_name, " +
            "p.manufacturer, p.supplier, p.unit_price, p.safety_stock, p.description " +
            "FROM production_recipes r " +
            "JOIN parts p ON r.part_id = p.part_id " +
            "WHERE r.product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                PartDTO dto = new PartDTO(
                    rs.getInt("part_id"),
                    rs.getString("name"),
                    rs.getInt("qty"),
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

    // -----------------------------
    // 6. 부품 전체 조회 (부품 탭용)
    // -----------------------------
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
}