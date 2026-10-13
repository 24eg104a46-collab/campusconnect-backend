package com.campusconnect.dto;

import java.util.List;
import java.util.Map;

public class JdbcReportDTO {
    private String queryExecuted;
    private String driverName;
    private String databaseProductName;
    private String databaseProductVersion;
    private long executionTimeMs;
    private List<String> columns;
    private List<Map<String, Object>> rows;
    private int recordCount;

    public JdbcReportDTO() {}

    public String getQueryExecuted() {
        return queryExecuted;
    }

    public void setQueryExecuted(String queryExecuted) {
        this.queryExecuted = queryExecuted;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public void setDatabaseProductName(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    public String getDatabaseProductVersion() {
        return databaseProductVersion;
    }

    public void setDatabaseProductVersion(String databaseProductVersion) {
        this.databaseProductVersion = databaseProductVersion;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }
}
