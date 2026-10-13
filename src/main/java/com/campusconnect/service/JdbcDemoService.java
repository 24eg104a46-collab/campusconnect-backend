package com.campusconnect.service;

import com.campusconnect.dto.JdbcReportDTO;

public interface JdbcDemoService {
    JdbcReportDTO executeDirectJdbcQuery(String queryType);
    JdbcReportDTO getDatabaseMetadata();
}
