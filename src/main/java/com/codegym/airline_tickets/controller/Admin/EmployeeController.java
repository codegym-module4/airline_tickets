package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.dto.RevenueByDateDto;
import com.codegym.airline_tickets.service.IBookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
                    BigInteger totalSumRevenue = BigInteger.ZERO;
                    for (RevenueByDateDto revenue : mainRevenue) {
                        totalSumRevenue = totalSumRevenue.add(revenue.getRevenue());
                    }

                    List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);
                    BigInteger totalSumCompareRevenue = BigInteger.ZERO;
                    for (RevenueByDateDto revenue : compareRevenue) {
                        totalSumCompareRevenue = totalSumCompareRevenue.add(revenue.getRevenue());
                    }

                    model.addAttribute("totalSumRevenue", totalSumRevenue);
                    model.addAttribute("totalSumCompareRevenue", totalSumCompareRevenue);
                    model.addAttribute("reportContent", reportContent);
                    return "admin/report/report_revenue_compared";

                } else if (reportTimeMode.equals("custom") && reportCompareMode.equals("quick")) {
                    LocalDate[] mainRange = getCustomDateTimeRange(reportStartDate, reportEndDate);
                    LocalDate[] compareRange = getQuickDateTimeRange(reportCompareQuick);

                    List<RevenueByDateDto> mainRevenue = bookingService.getRevenueByDay(mainRange[0], mainRange[1]);
                    BigInteger totalSumRevenue = BigInteger.ZERO;
                    for (RevenueByDateDto revenue : mainRevenue) {
                        totalSumRevenue = totalSumRevenue.add(revenue.getRevenue());
                    }

                    List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);
                    BigInteger totalSumCompareRevenue = BigInteger.ZERO;
                    for (RevenueByDateDto revenue : compareRevenue) {
                        totalSumCompareRevenue = totalSumCompareRevenue.add(revenue.getRevenue());
                    }

                    model.addAttribute("totalSumRevenue", totalSumRevenue);
                    model.addAttribute("totalSumCompareRevenue", totalSumCompareRevenue);
                    model.addAttribute("reportContent", reportContent);
                    return "admin/report/report_revenue_compared";

                } else if (reportTimeMode.equals("quick") && reportCompareMode.equals("custom")) {
                    LocalDate[] mainRange = getQuickDateTimeRange(reportTimeQuick);
                    LocalDate[] compareRange = getCustomDateTimeRange(reportCompareStartDate, reportCompareEndDate);

                    List<RevenueByDateDto> mainRevenue = bookingService.getRevenueByDay(mainRange[0], mainRange[1]);
                    BigInteger totalSumRevenue = BigInteger.ZERO;
                    for (RevenueByDateDto revenue : mainRevenue) {
                        totalSumRevenue = totalSumRevenue.add(revenue.getRevenue());
                    }
                    List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);
                    BigInteger totalSumCompareRevenue = BigInteger.ZERO;
                    for (RevenueByDateDto revenue : compareRevenue) {
                        totalSumCompareRevenue = totalSumCompareRevenue.add(revenue.getRevenue());
                    }

                    model.addAttribute("totalSumRevenue", totalSumRevenue);
                    model.addAttribute("totalSumCompareRevenue", totalSumCompareRevenue);
                    model.addAttribute("reportContent", reportContent);
                    return "admin/report/report_revenue_compared";

                } else if (reportTimeMode.equals("custom") && reportCompareMode.equals("custom")) {
                    LocalDate[] mainRange = getCustomDateTimeRange(reportStartDate, reportEndDate);
                    LocalDate[] compareRange = getCustomDateTimeRange(reportCompareStartDate, reportCompareEndDate);

                    List<RevenueByDateDto> mainRevenue = bookingService.getRevenueByDay(mainRange[0], mainRange[1]);
                    BigInteger totalSumRevenue = BigInteger.ZERO;
                    for (RevenueByDateDto revenue : mainRevenue) {
                        totalSumRevenue = totalSumRevenue.add(revenue.getRevenue());
                    }

                    List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);
                    BigInteger totalSumCompareRevenue = BigInteger.ZERO;
                    for (RevenueByDateDto revenue : compareRevenue) {
                        totalSumCompareRevenue = totalSumCompareRevenue.add(revenue.getRevenue());
                    }

                    model.addAttribute("totalSumRevenue", totalSumRevenue);
                    model.addAttribute("totalSumCompareRevenue", totalSumCompareRevenue);
                    model.addAttribute("reportContent", reportContent);
                    return "admin/report/report_revenue_compared";
                }
                break;
            default:
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

    @PostMapping("export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam("totalRevenue") String totalRevenueJson) throws IOException {
        // Chuyển đổi chuỗi JSON thành List
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> totalRevenue = objectMapper.readValue(totalRevenueJson, List.class);

        // Tạo workbook và sheet cho Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh Thu");

        // Tạo dòng đầu tiên (tiêu đề cột)
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Ngày");
        headerRow.createCell(1).setCellValue("Doanh thu (VND)");

        // Thêm dữ liệu vào sheet
        int rowNum = 1;
        for (Map<String, Object> revenueData : totalRevenue) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(revenueData.get("date").toString());  // Ngày
            row.createCell(1).setCellValue(Double.parseDouble(revenueData.get("revenue").toString()));  // Doanh thu
        }

        // Chuyển dữ liệu trong workbook thành byte array để gửi qua HTTP response
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        byte[] excelData = byteArrayOutputStream.toByteArray();

        // Cấu hình headers cho file Excel
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=revenue.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // Trả về file Excel dưới dạng ResponseEntity
        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }

    @PostMapping("export-excel2")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam("totalSumRevenue") Double totalSumRevenue,
            @RequestParam("totalSumCompareRevenue") Double totalSumCompareRevenue
    ) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh Thu");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Doanh thu chính");
        header.createCell(1).setCellValue("Doanh thu so sánh");

        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(totalSumRevenue);
        row.createCell(1).setCellValue(totalSumCompareRevenue);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        byte[] content = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=doanh_thu.xlsx");
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

}
