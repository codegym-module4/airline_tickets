    package com.codegym.airline_tickets.controller.Admin;

    import com.codegym.airline_tickets.dto.*;
    import com.codegym.airline_tickets.service.IBookingService;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.apache.poi.ss.usermodel.*;
    import org.apache.poi.xssf.usermodel.XSSFWorkbook;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.format.annotation.DateTimeFormat;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import javax.imageio.ImageIO;
    import java.awt.image.BufferedImage;
    import java.io.ByteArrayInputStream;
    import java.io.ByteArrayOutputStream;
    import java.io.IOException;
    import java.math.BigInteger;
    import java.time.DayOfWeek;
    import java.time.LocalDate;
    import java.util.Base64;
    import java.util.List;
    import java.util.Map;

    @Controller
    @RequestMapping("/admin/")
    public class ReportController {

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
                            if (revenue.getRevenue() != null) {
                                totalSumRevenue = totalSumRevenue.add(revenue.getRevenue());
                            }
                        }

                        List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);
                        BigInteger totalSumCompareRevenue = BigInteger.ZERO;
                        for (RevenueByDateDto revenue : compareRevenue) {
                            if (revenue.getRevenue() != null) {
                                totalSumCompareRevenue = totalSumCompareRevenue.add(revenue.getRevenue());
                            }
                        }

                        model.addAttribute("mainRevenue", mainRevenue);
                        model.addAttribute("compareRevenue", compareRevenue);
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
                            if (revenue.getRevenue() != null) {
                                totalSumRevenue = totalSumRevenue.add(revenue.getRevenue());
                            }
                        }

                        List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);
                        BigInteger totalSumCompareRevenue = BigInteger.ZERO;
                        for (RevenueByDateDto revenue : compareRevenue) {
                            if (revenue.getRevenue() != null) {
                                totalSumCompareRevenue = totalSumCompareRevenue.add(revenue.getRevenue());
                            }
                        }

                        model.addAttribute("mainRevenue", mainRevenue);
                        model.addAttribute("compareRevenue", compareRevenue);
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
                            if (revenue.getRevenue() != null) {
                                totalSumRevenue = totalSumRevenue.add(revenue.getRevenue());
                            }
                        }

                        List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);
                        BigInteger totalSumCompareRevenue = BigInteger.ZERO;
                        for (RevenueByDateDto revenue : compareRevenue) {
                            if (revenue.getRevenue() != null) {
                                totalSumCompareRevenue = totalSumCompareRevenue.add(revenue.getRevenue());
                            }
                        }

                        model.addAttribute("mainRevenue", mainRevenue);
                        model.addAttribute("compareRevenue", compareRevenue);
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
                            if (revenue.getRevenue() != null) {
                                totalSumRevenue = totalSumRevenue.add(revenue.getRevenue());
                            }
                        }

                        List<RevenueByDateDto> compareRevenue = bookingService.getRevenueByDay(compareRange[0], compareRange[1]);
                        BigInteger totalSumCompareRevenue = BigInteger.ZERO;
                        for (RevenueByDateDto revenue : compareRevenue) {
                            if (revenue.getRevenue() != null) {
                                totalSumCompareRevenue = totalSumCompareRevenue.add(revenue.getRevenue());
                            }
                        }

                        model.addAttribute("mainRevenue", mainRevenue);
                        model.addAttribute("compareRevenue", compareRevenue);
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
        public ResponseEntity<byte[]> exportExcel(@RequestBody ExportRequestDTO request) throws IOException {
            // Lấy dữ liệu từ request
            List<RevenueByDateDto> totalRevenue = request.getTotalRevenue();
            String chartBase64 = request.getChartImage();

            // Decode ảnh từ base64
            byte[] imageBytes = Base64.getDecoder().decode(chartBase64.split(",")[1]);

            // Tạo workbook và sheet cho Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Doanh Thu");
            Sheet sheet2 = workbook.createSheet("Doanh Thu Theo Khách Hàng");
            Sheet sheet3 = workbook.createSheet("Doanh Thu Theo Chuyến Bay");
            Sheet sheet4 = workbook.createSheet("Doanh Thu Theo Loại Chuyến Bay");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet đầu
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Ngày");
            headerRow.createCell(1).setCellValue("Doanh thu (VND)");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet thứ hai
            Row headerRow2 = sheet2.createRow(0);
            headerRow2.createCell(0).setCellValue("ID Khách hàng");
            headerRow2.createCell(1).setCellValue("Tên Khách hàng");
            headerRow2.createCell(2).setCellValue("Doanh thu (VND)");

            // Tao dòng đầu tiên (tiêu đề cột) của sheet thứ ba
            Row headerRow3 = sheet3.createRow(0);
            headerRow3.createCell(0).setCellValue("Chuyến bay");
            headerRow3.createCell(1).setCellValue("Chuyến bay khứ hồi");
            headerRow3.createCell(2).setCellValue("Doanh thu (VND)");

            // Tao dòng đầu tiên (tiêu đề cột) của sheet thứ tư
            Row headerRow4 = sheet4.createRow(0);
            headerRow4.createCell(0).setCellValue("Loại chuyến bay");
            headerRow4.createCell(1).setCellValue("Doanh thu (VND)");

            // Thêm dữ liệu vào sheet đầu
            int rowNum = 1;
            for (RevenueByDateDto revenueData : totalRevenue) {
                Row row = sheet.createRow(rowNum++);

                Object dateObj = revenueData.getDate();
                Object revenueObj = revenueData.getRevenue();
            //  Xét null ngày
                row.createCell(0).setCellValue(dateObj != null ? dateObj.toString() : "N/A");

            // Xét null doanh thu và khi lỗi parse thì điền Invalid
                if (revenueObj != null) {
                    try {
                        row.createCell(1).setCellValue(Double.parseDouble(revenueObj.toString()));
                    } catch (NumberFormatException e) {
                        row.createCell(1).setCellValue("Invalid");
                    }
                } else {
                    row.createCell(1).setCellValue("N/A");
                }
            }

            // Thêm dữ liệu vào sheet thứ hai
            int rowNum2 = 1;
            for (RevenueByDateDto revenueData : totalRevenue) {
                List<RevenueByUserDto> revenueByUser = revenueData.getRevenueByUser();
                for (RevenueByUserDto userRevenue : revenueByUser) {
                    Row row = sheet2.createRow(rowNum2++);
                    row.createCell(0).setCellValue(userRevenue.getUserId().getId());
                    row.createCell(1).setCellValue(userRevenue.getUserId().getFullName());
                    Object userRevenueObj = userRevenue.getRevenue();
                //  Xét null doanh thu và khi lỗi parse thì điền Invalid
                    if (userRevenueObj != null) {
                        try {
                            row.createCell(2).setCellValue(Double.parseDouble(userRevenueObj.toString()));
                        } catch (NumberFormatException e) {
                            row.createCell(2).setCellValue("Invalid");
                        }
                    } else {
                        row.createCell(2).setCellValue("N/A");
                    }
                }
            }

            // Thêm dữ liệu vào sheet thứ ba
            int rowNum3 = 1;
            for (RevenueByDateDto revenueData : totalRevenue) {
                List<RevenueByFlightDto> revenueByFlight = revenueData.getRevenueByFlight();
                for (RevenueByFlightDto flightRevenue : revenueByFlight) {
                    Row row = sheet3.createRow(rowNum3++);
                    Object flightObj = flightRevenue.getFlightId().getId();
                    Object returnFlightObj = flightRevenue.getReturnFlightId() != null ? flightRevenue.getReturnFlightId().getId() : null;
                    row.createCell(0).setCellValue(flightObj != null ? flightObj.toString() : "N/A");
                    row.createCell(1).setCellValue(returnFlightObj != null ? returnFlightObj.toString() : "N/A");
                    Object flightRevenueObj = flightRevenue.getRevenue();
                //  Xét null doanh thu và khi lỗi parse thì điền Invalid
                    if (flightRevenueObj != null) {
                        try {
                            row.createCell(2).setCellValue(Double.parseDouble(flightRevenueObj.toString()));
                        } catch (NumberFormatException e) {
                            row.createCell(2).setCellValue("Invalid");
                        }
                    } else {
                        row.createCell(2).setCellValue("N/A");
                    }
                }
            }

            // Thêm dữ liệu vào sheet thứ tư
            int rowNum4 = 1;
            for (RevenueByDateDto revenueData : totalRevenue) {
                List<RevenueByFlightTypeDto> revenueByFlightType = revenueData.getRevenueByFlightType();
                for (RevenueByFlightTypeDto flightTypeRevenue : revenueByFlightType) {
                    Row row = sheet4.createRow(rowNum4++);
                    Object flightTypeObj = flightTypeRevenue.getFlightType();
                    Object flightTypeRevenueObj = flightTypeRevenue.getRevenue();

                    if (flightTypeObj != null && "1".equals(flightTypeObj.toString())) {
                            row.createCell(0).setCellValue("Một chiều");
                        } else if (flightTypeObj != null && "2".equals(flightTypeObj.toString())) {
                            row.createCell(0).setCellValue("Khứ hồi");
                        } else {
                            row.createCell(0).setCellValue("N/A");
                    }

                //  Xét null doanh thu và khi lỗi parse thì điền Invalid
                    if (flightTypeRevenueObj != null) {
                        try {
                            row.createCell(1).setCellValue(Double.parseDouble(flightTypeRevenueObj.toString()));
                        } catch (NumberFormatException e) {
                            row.createCell(1).setCellValue("Invalid");
                        }
                    } else {
                        row.createCell(1).setCellValue("N/A");
                    }
                }
            }

            // Thêm ảnh vào sheet đầu
            int pictureIndex = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);

            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor(); //anchor để xác định vị trí ảnh (tọa độ), lớp ClientAnchor hỗ trợ thiết lập tọa độ
            anchor.setCol1(0);
            anchor.setRow1(rowNum + 2); // Chèn ảnh bắt đầu từ dòng cuối cùng + 2

            Picture pict = drawing.createPicture(anchor, pictureIndex);
            pict.resize(); // Tự động chỉnh kích thước ảnh

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
        public ResponseEntity<byte[]> exportExcel2(@RequestBody ExportRequestDTO request) throws IOException {

            // Lấy dữ liệu từ request
            BigInteger totalSumRevenue = request.getTotalSumRevenue();
            BigInteger totalSumCompareRevenue = request.getTotalSumCompareRevenue();

            String chartBase64 = request.getChartImage();

            List<RevenueByDateDto> mainRevenue = request.getMainRevenue();
            List<RevenueByDateDto> compareRevenue = request.getCompareRevenue();

            // Decode ảnh từ base64
            byte[] imageBytes = Base64.getDecoder().decode(chartBase64.split(",")[1]);

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Tổng Hợp Doanh Thu");
            Sheet sheet2 = workbook.createSheet("Doanh Thu Chính Theo Ngày");
            Sheet sheet3 = workbook.createSheet("Doanh Thu Được So Sánh Theo Ngày");
            Sheet sheet4 = workbook.createSheet("Doanh Thu Chính Theo Khách Hàng");
            Sheet sheet5 = workbook.createSheet("Doanh Thu Được So Sánh Theo Khách Hàng");
            Sheet sheet6 = workbook.createSheet("Doanh Thu Chính Theo Chuyến Bay");
            Sheet sheet7 = workbook.createSheet("Doanh Thu Được So Sánh Theo Chuyến Bay");
            Sheet sheet8 = workbook.createSheet("Doanh Thu Chính Theo Loại Chuyến Bay");
            Sheet sheet9 = workbook.createSheet("Doanh Thu Được So Sánh Theo Loại Chuyến Bay");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet đầu
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Doanh thu chính");
            header.createCell(1).setCellValue("Doanh thu được so sánh");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet thứ hai
            Row header2 = sheet2.createRow(0);
            header2.createCell(0).setCellValue("Ngày");
            header2.createCell(1).setCellValue("Doanh thu (VND)");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet thứ ba
            Row header3 = sheet3.createRow(0);
            header3.createCell(0).setCellValue("Ngày");
            header3.createCell(1).setCellValue("Doanh thu (VND)");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet thứ tư
            Row header4 = sheet4.createRow(0);
            header4.createCell(0).setCellValue("ID Khách hàng");
            header4.createCell(1).setCellValue("Tên Khách hàng");
            header4.createCell(2).setCellValue("Doanh thu (VND)");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet thứ năm
            Row header5 = sheet5.createRow(0);
            header5.createCell(0).setCellValue("ID Khách hàng");
            header5.createCell(1).setCellValue("Tên Khách hàng");
            header5.createCell(2).setCellValue("Doanh thu (VND)");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet thứ sáu
            Row header6 = sheet6.createRow(0);
            header6.createCell(0).setCellValue("Chuyến bay");
            header6.createCell(1).setCellValue("Chuyến bay khứ hồi");
            header6.createCell(2).setCellValue("Doanh thu (VND)");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet thứ bảy
            Row header7 = sheet7.createRow(0);
            header7.createCell(0).setCellValue("Chuyến bay");
            header7.createCell(1).setCellValue("Chuyến bay khứ hồi");
            header7.createCell(2).setCellValue("Doanh thu (VND)");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet thứ tám
            Row header8 = sheet8.createRow(0);
            header8.createCell(0).setCellValue("Loại chuyến bay");
            header8.createCell(1).setCellValue("Doanh thu (VND)");

            // Tạo dòng đầu tiên (tiêu đề cột) của sheet thứ chín
            Row header9 = sheet9.createRow(0);
            header9.createCell(0).setCellValue("Loại chuyến bay");
            header9.createCell(1).setCellValue("Doanh thu (VND)");


            // Thêm dữ liệu vào sheet đầu
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue(totalSumRevenue.doubleValue());
            row.createCell(1).setCellValue(totalSumCompareRevenue.doubleValue());


            // Thêm dữ liệu vào sheet thứ hai
            int rowNum2 = 1;
            for (RevenueByDateDto revenueData : mainRevenue) {
                Row row2 = sheet2.createRow(rowNum2++);
                Object dateObj = revenueData.getDate();
                Object revenueObj = revenueData.getRevenue();

                //  Xét null ngày
                row2.createCell(0).setCellValue(dateObj != null ? dateObj.toString() : "N/A");
                // Xét null doanh thu và khi lỗi parse thì điền Invalid
                if (revenueObj != null) {
                    try {
                        row2.createCell(1).setCellValue(Double.parseDouble(revenueObj.toString()));
                    } catch (NumberFormatException e) {
                        row2.createCell(1).setCellValue("Invalid");
                    }
                } else {
                    row2.createCell(1).setCellValue("N/A");
                }
            }

            // Thêm dữ liệu vào sheet thứ ba
            int rowNum3 = 1;
            for (RevenueByDateDto revenueData : compareRevenue) {
                Row row3 = sheet3.createRow(rowNum3++);
                Object dateObj = revenueData.getDate();
                Object revenueObj = revenueData.getRevenue();

                //  Xét null ngày
                row3.createCell(0).setCellValue(dateObj != null ? dateObj.toString() : "N/A");
                // Xét null doanh thu và khi lỗi parse thì điền Invalid
                if (revenueObj != null) {
                    try {
                        row3.createCell(1).setCellValue(Double.parseDouble(revenueObj.toString()));
                    } catch (NumberFormatException e) {
                        row3.createCell(1).setCellValue("Invalid");
                    }
                } else {
                    row3.createCell(1).setCellValue("N/A");
                }
            }

            // Thêm dữ liệu vào sheet thứ tư
            int rowNum4 = 1;
            for (RevenueByDateDto revenueData : mainRevenue) {
                List<RevenueByUserDto> revenueByUser = revenueData.getRevenueByUser();
                for (RevenueByUserDto userRevenue : revenueByUser) {
                    Row row4 = sheet4.createRow(rowNum4++);
                    row4.createCell(0).setCellValue(userRevenue.getUserId().getId());
                    row4.createCell(1).setCellValue(userRevenue.getUserId().getFullName());
                    Object userRevenueObj = userRevenue.getRevenue();
                    //  Xét null doanh thu và khi lỗi parse thì điền Invalid
                    if (userRevenueObj != null) {
                        try {
                            row4.createCell(2).setCellValue(Double.parseDouble(userRevenueObj.toString()));
                        } catch (NumberFormatException e) {
                            row4.createCell(2).setCellValue("Invalid");
                        }
                    } else {
                        row4.createCell(2).setCellValue("N/A");
                    }
                }
            }


            // Thêm dữ liệu vào sheet thứ năm
            int rowNum5 = 1;
            for (RevenueByDateDto revenueData : compareRevenue) {
                List<RevenueByUserDto> revenueByUser = revenueData.getRevenueByUser();
                for (RevenueByUserDto userRevenue : revenueByUser) {
                    Row row5 = sheet5.createRow(rowNum5++);
                    row5.createCell(0).setCellValue(userRevenue.getUserId().getId());
                    row5.createCell(1).setCellValue(userRevenue.getUserId().getFullName());
                    Object userRevenueObj = userRevenue.getRevenue();
                    //  Xét null doanh thu và khi lỗi parse thì điền Invalid
                    if (userRevenueObj != null) {
                        try {
                            row5.createCell(2).setCellValue(Double.parseDouble(userRevenueObj.toString()));
                        } catch (NumberFormatException e) {
                            row5.createCell(2).setCellValue("Invalid");
                        }
                    } else {
                        row5.createCell(2).setCellValue("N/A");
                    }
                }
            }

            // Thêm dữ liệu vào sheet thứ sáu
            int rowNum6 = 1;
            for (RevenueByDateDto revenueData : mainRevenue) {
                List<RevenueByFlightDto> revenueByFlight = revenueData.getRevenueByFlight();
                for (RevenueByFlightDto flightRevenue : revenueByFlight) {
                    Row row6 = sheet6.createRow(rowNum6++);
                    Object flightObj = flightRevenue.getFlightId().getId();
                    Object returnFlightObj = flightRevenue.getReturnFlightId() != null ? flightRevenue.getReturnFlightId().getId() : null;
                    row6.createCell(0).setCellValue(flightObj != null ? flightObj.toString() : "N/A");
                    row6.createCell(1).setCellValue(returnFlightObj != null ? returnFlightObj.toString() : "N/A");
                    Object flightRevenueObj = flightRevenue.getRevenue();
                    //  Xét null doanh thu và khi lỗi parse thì điền Invalid
                    if (flightRevenueObj != null) {
                        try {
                            row6.createCell(2).setCellValue(Double.parseDouble(flightRevenueObj.toString()));
                        } catch (NumberFormatException e) {
                            row6.createCell(2).setCellValue("Invalid");
                        }
                    } else {
                        row6.createCell(2).setCellValue("N/A");
                    }
                }
            }

            // Thêm dữ liệu vào sheet thứ bảy
            int rowNum7 = 1;
            for (RevenueByDateDto revenueData : compareRevenue) {
                List<RevenueByFlightDto> revenueByFlight = revenueData.getRevenueByFlight();
                for (RevenueByFlightDto flightRevenue : revenueByFlight) {
                    Row row7 = sheet7.createRow(rowNum7++);
                    Object flightObj = flightRevenue.getFlightId().getId();
                    Object returnFlightObj = flightRevenue.getReturnFlightId() != null ? flightRevenue.getReturnFlightId().getId() : null;
                    row7.createCell(0).setCellValue(flightObj != null ? flightObj.toString() : "N/A");
                    row7.createCell(1).setCellValue(returnFlightObj != null ? returnFlightObj.toString() : "N/A");
                    Object flightRevenueObj = flightRevenue.getRevenue();
                    //  Xét null doanh thu và khi lỗi parse thì điền Invalid
                    if (flightRevenueObj != null) {
                        try {
                            row7.createCell(2).setCellValue(Double.parseDouble(flightRevenueObj.toString()));
                        } catch (NumberFormatException e) {
                            row7.createCell(2).setCellValue("Invalid");
                        }
                    } else {
                        row7.createCell(2).setCellValue("N/A");
                    }
                }
            }

            // Thêm dữ liệu vào sheet thứ tám
            int rowNum8 = 1;
            for (RevenueByDateDto revenueData : mainRevenue) {
                List<RevenueByFlightTypeDto> revenueByFlightType = revenueData.getRevenueByFlightType();
                for (RevenueByFlightTypeDto flightTypeRevenue : revenueByFlightType) {
                    Row row8 = sheet8.createRow(rowNum8++);
                    Object flightTypeObj = flightTypeRevenue.getFlightType();
                    Object flightTypeRevenueObj = flightTypeRevenue.getRevenue();

                    if (flightTypeObj != null && "1".equals(flightTypeObj.toString())) {
                        row8.createCell(0).setCellValue("Một chiều");
                    } else if (flightTypeObj != null && "2".equals(flightTypeObj.toString())) {
                        row8.createCell(0).setCellValue("Khứ hồi");
                    } else {
                        row8.createCell(0).setCellValue("N/A");
                    }

                    //  Xét null doanh thu và khi lỗi parse thì điền Invalid
                    if (flightTypeRevenueObj != null) {
                        try {
                            row8.createCell(1).setCellValue(Double.parseDouble(flightTypeRevenueObj.toString()));
                        } catch (NumberFormatException e) {
                            row8.createCell(1).setCellValue("Invalid");
                        }
                    } else {
                        row8.createCell(1).setCellValue("N/A");
                    }
                }
            }

            // Thêm dữ liệu vào sheet thứ chín
            int rowNum9 = 1;
            for (RevenueByDateDto revenueData : compareRevenue) {
                List<RevenueByFlightTypeDto> revenueByFlightType = revenueData.getRevenueByFlightType();
                for (RevenueByFlightTypeDto flightTypeRevenue : revenueByFlightType) {
                    Row row9 = sheet9.createRow(rowNum9++);
                    Object flightTypeObj = flightTypeRevenue.getFlightType();
                    Object flightTypeRevenueObj = flightTypeRevenue.getRevenue();

                    if (flightTypeObj != null && "1".equals(flightTypeObj.toString())) {
                        row9.createCell(0).setCellValue("Một chiều");
                    } else if (flightTypeObj != null && "2".equals(flightTypeObj.toString())) {
                        row9.createCell(0).setCellValue("Khứ hồi");
                    } else {
                        row9.createCell(0).setCellValue("N/A");
                    }

                    //  Xét null doanh thu và khi lỗi parse thì điền Invalid
                    if (flightTypeRevenueObj != null) {
                        try {
                            row9.createCell(1).setCellValue(Double.parseDouble(flightTypeRevenueObj.toString()));
                        } catch (NumberFormatException e) {
                            row9.createCell(1).setCellValue("Invalid");
                        }
                    } else {
                        row9.createCell(1).setCellValue("N/A");
                    }
                }
            }


            // Thêm ảnh vào sheet đầu
            int pictureIndex = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(3); // Chèn ảnh bắt đầu từ dòng cuối cùng + 2

            Picture pict = drawing.createPicture(anchor, pictureIndex);
            pict.resize(); // Tự động chỉnh kích thước ảnh

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

    }
