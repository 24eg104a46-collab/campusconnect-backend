package com.campusconnect.service.impl;

import com.campusconnect.dto.JdbcReportDTO;
import com.campusconnect.service.JdbcDemoService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Service
public class JdbcDemoServiceImpl implements JdbcDemoService {

    private final DataSource dataSource;

    public JdbcDemoServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public JdbcReportDTO executeDirectJdbcQuery(String queryType) {
        JdbcReportDTO report = new JdbcReportDTO();
        long start = System.currentTimeMillis();

        String sql;
        if ("students_summary".equalsIgnoreCase(queryType)) {
            sql = "SELECT id, full_name, email, roll_number, department, cgpa, created_at FROM students ORDER BY cgpa DESC LIMIT 10";
        } else if ("opportunity_status".equalsIgnoreCase(queryType)) {
            sql = "SELECT id, title, company, type, location, min_cgpa, is_active FROM opportunities WHERE is_active = true";
        } else {
            sql = "SELECT s.department, COUNT(s.id) AS student_count, ROUND(AVG(s.cgpa), 2) AS avg_cgpa FROM students s GROUP BY s.department";
        }

        report.setQueryExecuted(sql);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            DatabaseMetaData metaData = connection.getMetaData();
            report.setDriverName(metaData.getDriverName() + " " + metaData.getDriverVersion());
            report.setDatabaseProductName(metaData.getDatabaseProductName());
            report.setDatabaseProductVersion(metaData.getDatabaseProductVersion());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnCount = rsmd.getColumnCount();

                List<String> columns = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columns.add(rsmd.getColumnLabel(i));
                }
                report.setColumns(columns);

                List<Map<String, Object>> rows = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(columns.get(i - 1), resultSet.getObject(i));
                    }
                    rows.add(row);
                }
                report.setRows(rows);
                report.setRecordCount(rows.size());
            }

        } catch (SQLException e) {
            report.setQueryExecuted("ERROR: " + e.getMessage());
            report.setColumns(Collections.singletonList("Error"));
            report.setRows(Collections.emptyList());
            report.setRecordCount(0);
        }

        report.setExecutionTimeMs(System.currentTimeMillis() - start);
        return report;
    }

    @Override
    public JdbcReportDTO getDatabaseMetadata() {
        JdbcReportDTO report = new JdbcReportDTO();
        long start = System.currentTimeMillis();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            report.setDriverName(meta.getDriverName() + " " + meta.getDriverVersion());
            report.setDatabaseProductName(meta.getDatabaseProductName());
            report.setDatabaseProductVersion(meta.getDatabaseProductVersion());
            report.setQueryExecuted("DatabaseMetaData Inspection (JDBC Architecture - Week 4)");

            List<String> columns = Arrays.asList("Property", "Value");
            List<Map<String, Object>> rows = new ArrayList<>();

            rows.add(Map.of("Property", "Database Product", "Value", meta.getDatabaseProductName()));
            rows.add(Map.of("Property", "Database Version", "Value", meta.getDatabaseProductVersion()));
            rows.add(Map.of("Property", "JDBC Driver", "Value", meta.getDriverName()));
            rows.add(Map.of("Property", "JDBC Driver Version", "Value", meta.getDriverVersion()));
            rows.add(Map.of("Property", "Database URL", "Value", meta.getURL()));
            rows.add(Map.of("Property", "User Name", "Value", meta.getUserName()));
            rows.add(Map.of("Property", "Supports Transactions", "Value", String.valueOf(meta.supportsTransactions())));

            report.setColumns(columns);
            report.setRows(rows);
            report.setRecordCount(rows.size());
        } catch (SQLException e) {
            report.setQueryExecuted("ERROR: " + e.getMessage());
            report.setColumns(Collections.singletonList("Error"));
            report.setRows(Collections.emptyList());
        }

        report.setExecutionTimeMs(System.currentTimeMillis() - start);
        return report;
    }
}
