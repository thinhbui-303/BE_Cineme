package com.example.goldenticketnew.cgvadmin.service;

import com.example.goldenticketnew.cgvadmin.dto.response.CgvRevenueDTO;
import com.example.goldenticketnew.enums.BillStatus;
import com.example.goldenticketnew.model.Bill;
import com.example.goldenticketnew.model.Ticket;
import com.example.goldenticketnew.repository.IBillRepository;
import com.example.goldenticketnew.repository.TicketRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CgvDashboardService {

    @Autowired
    private IBillRepository billRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public List<CgvRevenueDTO> getRevenueByMovie(LocalDate startDate, LocalDate endDate) {
        // Fetch all bills
        List<Bill> allBills = billRepository.findAll();
        
        // Filter by status = SUCCESS and date range on createdTime
        List<Bill> validBills = new ArrayList<>();
        for (Bill bill : allBills) {
            if (bill.getStatus() == BillStatus.SUCCESS && bill.getCreatedTime() != null) {
                LocalDate createdDate = bill.getCreatedTime().toLocalDate();
                if (!createdDate.isBefore(startDate) && !createdDate.isAfter(endDate)) {
                    validBills.add(bill);
                }
            }
        }

        Map<String, CgvRevenueDTO> revenueMap = new HashMap<>();

        for (Bill bill : validBills) {
            List<Ticket> tickets = ticketRepository.findTicketsByBillId(bill.getId());
            
            // Edge case: if bill has no tickets, skip it to avoid NullPointerException
            if (tickets == null || tickets.isEmpty()) {
                continue;
            }

            // Since 1 Bill = 1 Schedule = 1 Movie (from BookingRequestDto),
            // we get the movie name from the first ticket.
            Ticket firstTicket = tickets.get(0);
            if (firstTicket.getSchedule() != null && firstTicket.getSchedule().getMovie() != null) {
                String movieName = firstTicket.getSchedule().getMovie().getName();

                CgvRevenueDTO dto = revenueMap.getOrDefault(movieName, new CgvRevenueDTO(movieName, 0L, 0.0));
                
                // Add ticket count
                dto.setTicketCount(dto.getTicketCount() + tickets.size());
                // Add total revenue for this bill
                dto.setTotalRevenue(dto.getTotalRevenue() + bill.getPrice());

                revenueMap.put(movieName, dto);
            }
        }

        return new ArrayList<>(revenueMap.values());
    }

    public ByteArrayInputStream exportToExcel(List<CgvRevenueDTO> revenueList) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Doanh_Thu_Phim");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = { "STT", "Tên phim", "Số vé bán ra", "Doanh thu (VND)" };
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
                
                // Styling
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Data
            int rowIdx = 1;
            for (CgvRevenueDTO revenue : revenueList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(rowIdx - 1);
                row.createCell(1).setCellValue(revenue.getTitle());
                row.createCell(2).setCellValue(revenue.getTicketCount());
                row.createCell(3).setCellValue(revenue.getTotalRevenue());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi xuất file Excel", e);
        }
    }
}
