package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

import dao.LogDAO;
import dto.LogDTO;

public class LogPanel extends JPanel {

    private LogDAO logDAO = new LogDAO();
    private DefaultTableModel model;
    private JTable table;

    public LogPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250)); // 배경색 통일

        // 1. 테이블 모델 설정 (컬럼명)
        String[] cols = {"No.", "사번", "이름", "제품명", "생산수량", "생산일시"};
        model = new DefaultTableModel(null, cols) {
            @Override // 내용 수정 불가하게 설정
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 2. 테이블 디자인
        table = new JTable(model);
        table.setRowHeight(35); // 행 높이 시원하게
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        
        // 헤더 디자인
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94)); // 다크 네이비
        table.getTableHeader().setForeground(Color.WHITE);

        // 가운데 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // 3. 스크롤바에 담기
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백
        scroll.getViewport().setBackground(Color.WHITE);

        // 4. 상단 제목
        JLabel titleLabel = new JLabel("  📅  생산 작업 일지");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        add(titleLabel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        
        // 데이터 불러오기
        refreshData();
    }

    public void refreshData() {
        model.setRowCount(0); // 초기화
        List<LogDTO> list = logDAO.getAllLogs();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        int count = list.size(); // 순번 카운트 변수

        for (LogDTO log : list) {
            String no = String.format("%03d", count--); 
            String dateStr = sdf.format(log.getWorkDate());

            Object[] row = {
                no,
                log.getEmpId(),     
                log.getEmpName(),   
                log.getProductName(), 
                log.getAmount() + "대", 
                dateStr             
            };
            model.addRow(row);
        }
    }
}