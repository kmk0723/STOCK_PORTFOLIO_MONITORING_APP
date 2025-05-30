package com.capgemini.Stock.Portfolio.Monitoring.App.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.ReportService;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.ReportServiceImpl;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired private ReportServiceImpl reportService;

    @GetMapping("/portfolio-summary")
    public List<Holding> getSummary(@RequestParam String email) {
        return reportService.getPortfolioSummary(email);
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportExcel(@RequestParam String email) throws Exception {
        var in = reportService.exportToExcel(email);
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=portfolio.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }
}