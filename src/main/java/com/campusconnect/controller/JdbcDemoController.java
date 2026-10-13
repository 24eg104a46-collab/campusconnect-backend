package com.campusconnect.controller;

import com.campusconnect.dto.ApiResponse;
import com.campusconnect.dto.JdbcReportDTO;
import com.campusconnect.service.JdbcDemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jdbc-demo")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class JdbcDemoController {

    private final JdbcDemoService jdbcDemoService;

    public JdbcDemoController(JdbcDemoService jdbcDemoService) {
        this.jdbcDemoService = jdbcDemoService;
    }

    @GetMapping("/query")
    public ResponseEntity<ApiResponse<JdbcReportDTO>> runJdbcQuery(
            @RequestParam(defaultValue = "department_stats") String type) {
        JdbcReportDTO report = jdbcDemoService.executeDirectJdbcQuery(type);
        return ResponseEntity.ok(ApiResponse.success("Direct JDBC PreparedStatement executed successfully", report));
    }

    @GetMapping("/metadata")
    public ResponseEntity<ApiResponse<JdbcReportDTO>> getDatabaseMetadata() {
        JdbcReportDTO metadata = jdbcDemoService.getDatabaseMetadata();
        return ResponseEntity.ok(ApiResponse.success("JDBC DatabaseMetaData inspected successfully", metadata));
    }
}
