package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.entity.Ticket;
import com.codegym.airline_tickets.service.impl.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/ticketManagement")
public class TicketManagementController {

    @Autowired
    private TicketService ticketService;

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

    // Hiển thị form chỉnh sửa vé
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Ticket ticket = ticketService.findById(id);
        if (ticket == null) {
            return "redirect:/admin/ticketManagement/list";
        }
        model.addAttribute("ticket", ticket);
        return "admin/ticketManagement/edit";
    }

    // Cập nhật thông tin vé
    @PostMapping("/update")
    public String updateTicket(@ModelAttribute("ticket") Ticket ticket) {
        ticketService.save(ticket);
        return "redirect:/admin/ticketManagement/list";
    }

    // In vé
    @GetMapping("/print/{id}")
    public String printTicket(@PathVariable("id") Long id, Model model) {
        Ticket ticket = ticketService.findById(id);
        if (ticket == null) {
            return "redirect:/admin/ticketManagement/list";
        }
        model.addAttribute("ticket", ticket);
        return "admin/ticketManagement/print";
    }

    // Xóa vé mềm
    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable("id") Long id) {
        ticketService.remove(id);
        return "redirect:/admin/ticketManagement/list";
    }
}
