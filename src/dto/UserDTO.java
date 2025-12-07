package dto;

public class UserDTO {
    private String id;      // 아이디 (사번)
    private String pw;      // 비밀번호
    private String name;    // 이름
    private String role;    // 직급/역할

    // 기본 생성자
    public UserDTO() {}

    // 전체 생성자
    public UserDTO(String id, String pw, String name, String role) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.role = role;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPw() { return pw; }
    public void setPw(String pw) { this.pw = pw; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}