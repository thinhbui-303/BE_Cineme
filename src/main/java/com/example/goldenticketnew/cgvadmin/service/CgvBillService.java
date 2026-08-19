package com.example.goldenticketnew.cgvadmin.service;

import com.example.goldenticketnew.cgvadmin.dto.response.CgvBillDTO;
import com.example.goldenticketnew.cgvadmin.dto.response.CgvBillDetailDTO;
import com.example.goldenticketnew.dtos.DeleteBillTicketRequest;
import com.example.goldenticketnew.model.Bill;
import com.example.goldenticketnew.model.Schedule;
import com.example.goldenticketnew.model.Ticket;
import com.example.goldenticketnew.repository.IBillRepository;
import com.example.goldenticketnew.repository.TicketRepository;
import com.example.goldenticketnew.service.bill.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CgvBillService {

    @Autowired
    private IBillRepository billRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private IBillService baseBillService;

    public Page<CgvBillDTO> getAllBills(Pageable pageable) {
        // Sort by createdTime descending to get the newest bills first
        List<Bill> bills = billRepository.findAll(Sort.by(Sort.Direction.DESC, "createdTime"));
        
        List<CgvBillDTO> dtos = bills.stream().map(bill -> {
            CgvBillDTO dto = new CgvBillDTO();
            dto.setId(bill.getId());
            dto.setUserName(bill.getUser() != null ? bill.getUser().getName() : "");
            dto.setStatus(bill.getStatus());
            dto.setPrice(bill.getPrice());
            dto.setCreatedTime(bill.getCreatedTime());
            
            List<Ticket> tickets = ticketRepository.findTicketsByBillId(bill.getId());
            if (tickets != null && !tickets.isEmpty()) {
                Schedule schedule = tickets.get(0).getSchedule();
                if (schedule != null) {
                    dto.setMovieName(schedule.getMovie() != null ? schedule.getMovie().getName() : "");
                    dto.setBranchName(schedule.getBranch() != null ? schedule.getBranch().getName() : "");
                    dto.setRoomName(schedule.getRoom() != null ? schedule.getRoom().getName() : "");
                    dto.setStartDate(schedule.getStartDate());
                    dto.setStartTime(schedule.getStartTime());
                }
            } else {
                dto.setMovieName("Chưa hoàn tất");
                dto.setBranchName("Chưa hoàn tất");
                dto.setRoomName("Chưa hoàn tất");
                dto.setStartDate(null);
                dto.setStartTime(null);
            }
            return dto;
        }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        List<CgvBillDTO> subList = start > dtos.size() ? List.of() : dtos.subList(start, end);

        return new PageImpl<>(subList, pageable, dtos.size());
    }

    public CgvBillDetailDTO getBillDetail(Integer billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        CgvBillDetailDTO dto = new CgvBillDetailDTO();
        dto.setId(bill.getId());
        dto.setUserName(bill.getUser() != null ? bill.getUser().getName() : "");
        dto.setUserEmail(bill.getUser() != null ? bill.getUser().getEmail() : "");
        dto.setStatus(bill.getStatus());
        dto.setPrice(bill.getPrice());
        dto.setCreatedTime(bill.getCreatedTime());

        List<Ticket> tickets = ticketRepository.findTicketsByBillId(bill.getId());
        if (tickets != null && !tickets.isEmpty()) {
            Schedule schedule = tickets.get(0).getSchedule();
            if (schedule != null) {
                dto.setMovieName(schedule.getMovie() != null ? schedule.getMovie().getName() : "");
                dto.setMoviePoster(schedule.getMovie() != null ? schedule.getMovie().getSmallImageURl() : "");
                dto.setBranchName(schedule.getBranch() != null ? schedule.getBranch().getName() : "");
                dto.setRoomName(schedule.getRoom() != null ? schedule.getRoom().getName() : "");
                dto.setStartDate(schedule.getStartDate());
                dto.setStartTime(schedule.getStartTime());
            }

            String seats = tickets.stream()
                    .map(t -> t.getSeat() != null ? t.getSeat().getName() : "")
                    .collect(Collectors.joining(", "));
            dto.setSeats(seats);
        } else {
            dto.setMovieName("Chưa hoàn tất");
            dto.setBranchName("Chưa hoàn tất");
            dto.setRoomName("Chưa hoàn tất");
            dto.setSeats("Chưa có ghế ngồi");
        }

        return dto;
    }

    public void cancelBill(Integer billId) {
        DeleteBillTicketRequest request = new DeleteBillTicketRequest();
        request.setBillId(billId);
        
        // This will throw exception if bill is SUCCESS, and will delete tickets and set EXPIRATION
        baseBillService.removeBill(request);
    }
}
