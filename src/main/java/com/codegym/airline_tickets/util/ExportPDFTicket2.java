package com.codegym.airline_tickets.util;

import com.codegym.airline_tickets.entity.Ticket;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class ExportPDFTicket2 {

    private final TemplateEngine templateEngine;

    public ExportPDFTicket2(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void export(HttpServletResponse response, Ticket ticket) throws Exception {
        response.setContentType("application/pdf");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "inline; filename=ticket_" + ticket.getId() + ".pdf");

        Context context = new Context();
        context.setVariable("ticket", ticket);
        String htmlContent = templateEngine.process("admin/ticketManagement/ticket-pdf", context);

        try (OutputStream os = response.getOutputStream()) {
            HtmlConverter.convertToPdf(htmlContent, os);
        }
    }
}
