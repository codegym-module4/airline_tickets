    package com.codegym.airline_tickets.controller.Admin;

    import com.codegym.airline_tickets.entity.Ticket;
    import com.codegym.airline_tickets.service.impl.TicketService;

    import com.codegym.airline_tickets.util.ExportPDFTicket2;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;


    import java.util.List;

    @Controller
    @RequestMapping("/admin/ticketManagement")
    public class TicketManagementController {

        @Autowired
        private TicketService ticketService;

        @Autowired
        private ExportPDFTicket2 exportPDFTicket;


        // Hiển thị danh sách tất cả vé
        @GetMapping("/list")
        public String showTicketList(Model model) {
            List<Ticket> ticketList = ticketService.getAll();
            model.addAttribute("ticketList", ticketList);
            return "admin/ticketManagement/list";
        }

        // Tìm kiếm vé theo trường bất kỳ (code, name, route)
        @GetMapping("/search")
        public String searchTicket(@RequestParam("field") String field,
                                   @RequestParam("value") String keyword,
                                   Model model) {
            List<Ticket> ticketList = ticketService.searchTicketsByField(field, keyword);
            model.addAttribute("ticketList", ticketList);
            return "admin/ticketManagement/list";
        }

        private static final Logger log = LoggerFactory.getLogger(TicketManagementController.class);

        // Hiển thị form chỉnh sửa vé
        @GetMapping("/update/{id}")
        public String showUpdateForm(@PathVariable Long id, Model model) {
            Ticket ticket = ticketService.findById(id);
            if (ticket == null) {
                log.warn("Không tìm thấy ticket với id={}", id);
                return "redirect:/admin/ticketManagement/list";
            }
            log.debug("Mở form cập nhật cho ticket id={}", id);
            model.addAttribute("ticket", ticket);
            return "admin/ticketManagement/update"; // HTML form cập nhật
        }

        // Xử lý cập nhật vé
        @PostMapping("/update/{id}")
        public String updateTicket(@PathVariable("id") Long id,
                                   @ModelAttribute("ticket") Ticket ticket) {
            log.debug("Nhận request cập nhật ticket id={} với dữ liệu: {}", id, ticket);

            Ticket existingTicket = ticketService.findById(id);
            if (existingTicket == null) {
                log.warn("Cập nhật thất bại: không tìm thấy ticket id={}", id);
                return "redirect:/admin/ticketManagement/list";
            }

            try {
                // Cập nhật các trường
                existingTicket.setName(ticket.getName());
                existingTicket.setPhone(ticket.getPhone());
                existingTicket.setEmail(ticket.getEmail());
                existingTicket.setPrice(ticket.getPrice());

                // Quan trọng: bạn cần truyền đúng ID của flight và booking
                existingTicket.setFlight(ticket.getFlight());
                existingTicket.setBooking(ticket.getBooking());

                ticketService.update(id, existingTicket);
                log.info("Cập nhật thành công ticket id={}", id);

            } catch (Exception e) {
                log.error("Lỗi khi cập nhật ticket id=" + id, e);
                return "admin/ticketManagement/update"; // Quay lại form nếu lỗi
            }

            return "redirect:/admin/ticketManagement/list";
        }



        // In vé
        @GetMapping("/print/{id}")
        public void printTicket(@PathVariable("id") Long id, HttpServletResponse response) {
            Ticket ticket = ticketService.findById(id);
            if (ticket == null) {
                throw new RuntimeException("Không tìm thấy ticket với id=" + id);
            }

            try {
                exportPDFTicket.export(response, ticket);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi khi in vé: " + e.getMessage(), e);
            }
        }



        // Xóa vé mềm
        @GetMapping("/delete/{id}")
        public String deleteTicket(@PathVariable("id") Long id) {
            ticketService.remove(id);
            return "redirect:/admin/ticketManagement/list";
        }
    }
