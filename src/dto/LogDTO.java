package dto;

import java.sql.Timestamp;

public class LogDTO {
    private int logId;          // 순번 (DB의 log_id)
    private String empId;       // 사번
    private String empName;     // 이름
    private String productName; // 제품명
    private int amount;         // 수량
    private Timestamp workDate; // 생산일시 (날짜+시간)

    // 기본 생성자
    public LogDTO() {}

    // 전체 필드 생성자 (DAO에서 데이터 담을 때 사용)
    public LogDTO(int logId, String empId, String empName, String productName, int amount, Timestamp workDate) {
        this.logId = logId;
        this.empId = empId;
        this.empName = empName;
        this.productName = productName;
        this.amount = amount;
        this.workDate = workDate;
    }

    // Getter & Setter
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }

    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public Timestamp getWorkDate() { return workDate; }
    public void setWorkDate(Timestamp workDate) { this.workDate = workDate; }
}