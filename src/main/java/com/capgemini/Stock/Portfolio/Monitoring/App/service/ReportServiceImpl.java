package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.UserRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.HoldingsRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportServiceImpl {
	@Autowired private UserRepository userRepository;
    @Autowired private HoldingsRepository holdingRepository;
    @Autowired private PriceFetcherServiceImpl priceFetcherService;

    public List<Holding> getPortfolioSummary(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Portfolio portfolio = user.getPortfolio();
        return holdingRepository.findByPortfolio(portfolio);
    }

    public ByteArrayInputStream exportToExcel(String email) throws Exception {
        List<Holding> holdings = getPortfolioSummary(email);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Portfolio Summary");

            Row header = sheet.createRow(0);
            String[] columns = {"Stock Symbol", "Quantity", "Buy Price", "Current Price", "Gain/Loss %"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (Holding h : holdings) {
                double current = priceFetcherService.getLatestPrice(h.getSymbol());
                double gainPercent = ((current - h.getBuyPrice()) / h.getBuyPrice()) * 100;

                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(h.getSymbol());
                row.createCell(1).setCellValue(h.getQuantity());
                row.createCell(2).setCellValue(h.getBuyPrice());
                row.createCell(3).setCellValue(current);
                row.createCell(4).setCellValue(gainPercent);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

}
