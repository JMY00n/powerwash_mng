package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.ProductDTO;
import dto.PartDTO;
import util.DBConnection;

public class ProductDAO {

    // 전체 제품 조회
    public List<ProductDTO> getAll() {
        String sql = "SELECT product_id, name, type, stock, image_name FROM products";
        List<ProductDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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

    // 타입별 제품 조회 (전기식, 연료식)
    public List<ProductDTO> getByType(String type) {
        String sql = "SELECT product_id, name, type, stock, image_name FROM products WHERE type = ?";
        List<ProductDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);
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

    // 제품 구성 부품(레시피) 조회 - 상세보기용
    // quantity_needed를 PartDTO의 stock 필드에 넣어서 "필요 개수"로 사용한다.
    public List<PartDTO> getProductRecipe(int productId) {

        List<PartDTO> list = new ArrayList<>();

        String sql =
            "SELECT p.part_id, p.name, r.quantity_needed AS need_qty, p.image_name, " +
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
                    rs.getInt("need_qty"),          // 여기서는 "필요 수량"으로 사용
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

    /**
     * 제품 생산 로직
     * 1) 레시피 기준으로 필요한 부품 수량 계산
     * 2) 각 부품 재고가 충분한지 확인
     * 3) 충분하면 부품 재고 차감 + 제품 재고 증가 (트랜잭션)
     *
     * @return "SUCCESS" 또는 에러 메시지
     */
    public String produceProduct(int productId, int amount) {

        if (amount <= 0) {
            return "생산 수량은 1 이상이어야 합니다.";
        }

        String checkSql =
            "SELECT r.part_id, r.quantity_needed, p.stock " +
            "FROM production_recipes r " +
            "JOIN parts p ON r.part_id = p.part_id " +
            "WHERE r.product_id = ?";

        String updatePartSql = "UPDATE parts SET stock = stock - ? WHERE part_id = ?";
        String updateProductSql = "UPDATE products SET stock = stock + ? WHERE product_id = ?";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1) 필요 부품 & 재고 체크
            List<PartNeed> needs = new ArrayList<>();

            try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                pstmt.setInt(1, productId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int partId = rs.getInt("part_id");
                    int qtyNeededPerUnit = rs.getInt("quantity_needed");
                    int currentStock = rs.getInt("stock");

                    int totalNeed = qtyNeededPerUnit * amount;

                    if (currentStock < totalNeed) {
                        conn.rollback();
                        return "부품 재고 부족 (부품 ID: " + partId + ")";
                    }

                    needs.add(new PartNeed(partId, totalNeed));
                }
            }

            // 레시피가 없는 경우
            if (needs.isEmpty()) {
                conn.rollback();
                return "등록된 레시피가 없습니다.";
            }

            // 2) 부품 재고 차감
            try (PreparedStatement pstmt = conn.prepareStatement(updatePartSql)) {
                for (PartNeed need : needs) {
                    pstmt.setInt(1, need.totalNeed);
                    pstmt.setInt(2, need.partId);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            // 3) 제품 재고 증가
            try (PreparedStatement pstmt = conn.prepareStatement(updateProductSql)) {
                pstmt.setInt(1, amount);
                pstmt.setInt(2, productId);
                pstmt.executeUpdate();
            }

            conn.commit();
            return "SUCCESS";

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return "예기치 못한 오류 발생";
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 내부에서만 사용하는 보조 클래스
    private static class PartNeed {
        int partId;
        int totalNeed;
        PartNeed(int partId, int totalNeed) {
            this.partId = partId;
            this.totalNeed = totalNeed;
        }
    }
    
    public static class ProductionResult {
        public boolean success;
        public List<LackItem> lackList;

        public ProductionResult(boolean success) {
            this.success = success;
        }
    }

    public static class LackItem {
        public String partName;
        public int lackAmount;

        public LackItem(String partName, int lackAmount) {
            this.partName = partName;
            this.lackAmount = lackAmount;
        }
    }
    
    public ProductionResult produceProductDetailed(int productId, int amount) {

        ProductionResult result = new ProductionResult(true);
        result.lackList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            // 1. 제조에 필요한 부품 목록 조회
            String sql = """
                SELECT p.part_id, p.name, r.quantity_needed, p.stock
                FROM parts p
                JOIN production_recipes r ON p.part_id = r.part_id
                WHERE r.product_id = ?
            """;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            boolean enough = true;

            while (rs.next()) {
                int need = rs.getInt("quantity_needed") * amount;
                int stock = rs.getInt("stock");

                if (stock < need) {
                    enough = false;
                    int lack = need - stock;

                    result.lackList.add(new LackItem(
                            rs.getString("name"),
                            lack
                    ));
                }
            }

            if (!enough) {
                result.success = false;
                return result;
            }

            // 2. 모든 부품 재고 차감
            String deductSql = """
                UPDATE parts p
                JOIN production_recipes r ON p.part_id = r.part_id
                SET p.stock = p.stock - (r.quantity_needed * ?)
                WHERE r.product_id = ?
            """;

            PreparedStatement deductPstmt = conn.prepareStatement(deductSql);
            deductPstmt.setInt(1, amount);
            deductPstmt.setInt(2, productId);
            deductPstmt.executeUpdate();

            // 3. 완제품 재고 증가
            PreparedStatement updateProduct = conn.prepareStatement(
                    "UPDATE products SET stock = stock + ? WHERE product_id = ?"
            );
            updateProduct.setInt(1, amount);
            updateProduct.setInt(2, productId);
            updateProduct.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            result.success = false;
        }

        return result;
    }


}
