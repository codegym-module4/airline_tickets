package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.dto.RevenueByDateDto;
import com.codegym.airline_tickets.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/")
public class EmployeeController {

    @Autowired
    private IBookingService bookingService;

    @GetMapping("report")
    public String report() {
        return "admin/report/report_create";
    }

    @PostMapping("create")
    public String createReport(
            @RequestParam("reportType") String reportType,
            @RequestParam("reportContent") String reportContent,
            @RequestParam("reportTimeMode") String reportTimeMode,
            @RequestParam(value = "reportTimeQuick", defaultValue = "") String reportTimeQuick,
            @RequestParam(value = "reportStartDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reportStartDate,
            @RequestParam(value = "reportEndDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reportEndDate,
            @RequestParam(value = "reportCompareMode", defaultValue = "false") String reportCompareMode,
            @RequestParam(value = "reportCompareQuick", defaultValue = "") String reportCompareQuick,
            @RequestParam(value = "reportCompareStartDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reportCompareStartDate,
            @RequestParam(value = "reportCompareEndDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reportCompareEndDate,
            Model model
    ) {

        switch (reportType) {
            case "revenue":
                if (reportTimeMode.equals("quick") && reportCompareMode.equals("false")) {
                    LocalDate[] range = getQuickDateTimeRange(reportTimeQuick);
                    List<RevenueByDateDto> totalRevenue = bookingService.getRevenueByDay(range[0], range[1]);
                    model.addAttribute("totalRevenue", totalRevenue);
                    model.addAttribute("reportContent", reportContent);
                    return "admin/report/report_revenue_single";

                } else if (reportTimeMode.equals("custom") && reportCompareMode.equals("false")) {
                    LocalDate[] range = getCustomDateTimeRange(reportStartDate, reportEndDate);
                    List<RevenueByDateDto> totalRevenue = bookingService.getRevenueByDay(range[0], range[1]);
                    model.addAttribute("totalRevenue", totalRevenue);
                    model.addAttribute("reportContent", reportContent);
                    return "admin/report/report_revenue_single";

                } else if (reportTimeMode.equals("quick") && reportCompareMode.equals("quick")) {
                    LocalDate[] mainRange = getQuickDateTimeRange(reportTimeQuick);
                    LocalDate[] compareRange = getQuickDateTimeRange(reportCompareQuick);

                    List<RevenueByDateDto> mainRevenue = bookingService.getRevenueByDay(mainRange[0], mainRange[1]);
                    List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);

                    model.addAttribute("mainRevenue", mainRevenue);
                    model.addAttribute("compareRevenue", compareRevenue);
                    model.addAttribute("reportContent", reportContent);
                    return "admin/report/report_revenue_compare";

                } else if (reportTimeMode.equals("custom") && reportCompareMode.equals("quick")) {
                    LocalDate[] mainRange = getCustomDateTimeRange(reportStartDate, reportEndDate);
                    LocalDate[] compareRange = getQuickDateTimeRange(reportCompareQuick);

                    List<RevenueByDateDto> mainRevenue = bookingService.getRevenueByDay(mainRange[0], mainRange[1]);
                    List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);

                    model.addAttribute("mainRevenue", mainRevenue);
                    model.addAttribute("compareRevenue", compareRevenue);
                    model.addAttribute("reportContent", reportContent);
                    return "admin/report/report_revenue_compare";

                } else if (reportTimeMode.equals("quick") && reportCompareMode.equals("custom")) {
                    LocalDate[] mainRange = getQuickDateTimeRange(reportTimeQuick);
                    LocalDate[] compareRange = getCustomDateTimeRange(reportCompareStartDate, reportCompareEndDate);

                    List<RevenueByDateDto> mainRevenue = bookingService.getRevenueByDay(mainRange[0], mainRange[1]);
                    List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);

                    model.addAttribute("mainRevenue", mainRevenue);
                    model.addAttribute("compareRevenue", compareRevenue);
                    model.addAttribute("reportContent", reportContent);
                    return "admin/report/report_revenue_compare";

                } else if (reportTimeMode.equals("custom") && reportCompareMode.equals("custom")) {
                    LocalDate[] mainRange = getCustomDateTimeRange(reportStartDate, reportEndDate);
                    LocalDate[] compareRange = getCustomDateTimeRange(reportCompareStartDate, reportCompareEndDate);

                    List<RevenueByDateDto> mainRevenue = bookingService.getRevenueByDay(mainRange[0], mainRange[1]);
                    List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);

                    model.addAttribute("mainRevenue", mainRevenue);
                    model.addAttribute("compareRevenue", compareRevenue);
                    model.addAttribute("reportContent", reportContent);
                    return "admin/report/report_revenue_compare";
                }
                break;
            case "employee":
                // Handle report type 2
                break;
            case "airline":
                // Handle report type 3
                break;
            default:
                // Handle default case
                break;
        }
        return "admin/report/report_create";
    }

    private LocalDate[] getQuickDateTimeRange(String quickType) {
        LocalDate start = null;
        LocalDate end = null;
        LocalDate today = LocalDate.now();

        switch (quickType) {
            case "yesterday":
                start = today.minusDays(1);
                end = today.minusDays(1);
                break;
            case "this_week":
                start = today.with(DayOfWeek.MONDAY);
                end = today.with(DayOfWeek.SUNDAY);
                break;
            case "this_month":
                start = today.withDayOfMonth(1);
                end = today.withDayOfMonth(today.lengthOfMonth());
                break;
            case "this_year":
                start = today.withDayOfYear(1);
                end = today.withDayOfYear(today.lengthOfYear());
                break;
        }

        return new LocalDate[]{start, end};
    }

    private LocalDate[] getCustomDateTimeRange(LocalDate startDate, LocalDate endDate) {
        return new LocalDate[]{startDate, endDate};
    }
}
