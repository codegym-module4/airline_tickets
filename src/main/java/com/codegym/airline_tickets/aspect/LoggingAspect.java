package com.codegym.airline_tickets.aspect;

import com.codegym.airline_tickets.dto.EmployeeAccountDTO;
import com.codegym.airline_tickets.entity.*;
import com.codegym.airline_tickets.service.impl.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private LogService logService;

    // Ghi log khi update vé thành công
    @Pointcut("execution(* com.codegym.airline_tickets.service.impl.BookingService.updateOrCreate(..))")
    public void bookTicketSuccess() {
    }

    @Around("bookTicketSuccess()")
    public void logAroundBooking(ProceedingJoinPoint joinPoint) throws Throwable {
        Booking booking = (Booking) joinPoint.getArgs()[0];
        boolean isUpdate = (booking.getId() != null); // Lấy id trước khi phương thức thực hiện

        Booking result = (Booking) joinPoint.proceed();

        Long id = result.getId();

        Log bookingUpdateLog = new Log();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        bookingUpdateLog.setUsername(username);
        bookingUpdateLog.setIdAffected(id.toString());
        if (isUpdate) {
            bookingUpdateLog.setAction("Cập nhật vé thành công: " + id);
        } else {
            bookingUpdateLog.setAction("Đặt vé mới thành công: " + id);
        }
        bookingUpdateLog.setTimestamp(LocalDateTime.now());
        logService.save(bookingUpdateLog);
    }

    // Ghi log khi chỉnh sửa thông tin user hoặc employee
    @Pointcut("execution(* com.codegym.airline_tickets.service.impl.UserService.update(..))")
    public void updateUser() {
    }

    @Around("updateUser()")
    public void LogAroundUpdateUser (ProceedingJoinPoint joinPoint) throws Throwable {
        User user = (User) joinPoint.getArgs()[1];

        joinPoint.proceed();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Log userUpdatedLog = new Log();
        userUpdatedLog.setUsername(username);
        userUpdatedLog.setAction("Cập nhật thông tin người dùng: " + user.getId());
        if (user.getId() != null) {
            userUpdatedLog.setIdAffected(user.getId().toString());
        } else {
            userUpdatedLog.setIdAffected("N/A");
        }
        userUpdatedLog.setTimestamp(LocalDateTime.now());
        logService.save(userUpdatedLog);
    }

    @Pointcut("execution(* com.codegym.airline_tickets.service.impl.EmployeeService.updateEmployeeAndAccount(..))")
    public void updateEmployee() {
    }

    @Around("updateEmployee()")
    public void LogAroundUpdateEmployee(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        EmployeeAccountDTO employee = (EmployeeAccountDTO) args[0];

        joinPoint.proceed();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Log employeeUpdatedLog = new Log();
        employeeUpdatedLog.setUsername(username);
        if (employee.getEmployeeId() != null) {
            employeeUpdatedLog.setAction("Cập nhật thông tin nhân viên: " + employee.getEmployeeId());
            employeeUpdatedLog.setIdAffected(employee.getEmployeeId().toString());
        }
        else {
            employeeUpdatedLog.setAction("Cập nhật thông tin nhân viên không thành công ");
            employeeUpdatedLog.setIdAffected("N/A");
        }
        employeeUpdatedLog.setTimestamp(LocalDateTime.now());
        logService.save(employeeUpdatedLog);
    }


    // Ghi log khi có lỗi xảy ra trong hệ thống
//    @Pointcut("execution(* com.codegym.airline_tickets.service.impl.*.*(..))")
//    public void serviceErrors() {
//    }
//
//    @AfterThrowing(pointcut = "serviceErrors()", throwing = "error")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
//        Log errorLog = new Log();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = "Hệ thống";
//        if (authentication != null ) {
//            username = authentication.getName();
//        }
//        errorLog.setUsername(username);
//        errorLog.setAction("Lỗi trong phương thức: " + joinPoint.getSignature().getName() + " - " + error.getMessage());
//        errorLog.setIdAffected("N/A");
//        errorLog.setTimestamp(LocalDateTime.now());
//        logService.save(errorLog);
//    }

    // Ghi log khi thêm mới nhân viên, thêm mới khách hàng
    @Pointcut("execution(* com.codegym.airline_tickets.service.impl.EmployeeService.updateOrCreate(..))")
    public void createEmployee() {
    }

    @Around("createEmployee()")
    public void logAroundCreateEmployee(ProceedingJoinPoint joinPoint) throws Throwable {
        Employee result = (Employee) joinPoint.proceed();

        Log employeeCreatedLog = new Log();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        employeeCreatedLog.setUsername(username);
        if (result.getId() != null) {
            employeeCreatedLog.setAction("Thêm mới nhân viên: " + result.getId());
            employeeCreatedLog.setIdAffected(result.getId().toString());
        }
        employeeCreatedLog.setTimestamp(LocalDateTime.now());
        logService.save(employeeCreatedLog);
    }

    @Pointcut("execution(* com.codegym.airline_tickets.service.impl.AccountService.register(..))")
    public void createUser() {
    }

    @Around("createUser()")
    public void logAfterCreateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Account account = (Account) joinPoint.getArgs()[0];
        User user = account.getUser();

        joinPoint.proceed();

        if (user != null && user.getCode() != null) {
            Log userCreatedLog = new Log();
            userCreatedLog.setUsername("anonymousUser");
            userCreatedLog.setAction("Thêm mới người dùng" + user.getId());
            userCreatedLog.setIdAffected(user.getId().toString());
            userCreatedLog.setTimestamp(LocalDateTime.now());
            logService.save(userCreatedLog);
        }
    }

    //Ghi log khi xóa nhân viên, xóa khách hàng, xóa đặt vé
    @Pointcut("execution(* com.codegym.airline_tickets.repository.EmployeeRepository.delete(..))")
    public void deleteEmployee() {
    }

    @After("deleteEmployee()")
    public void logAfterDeleteEmployee(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Employee employee = (Employee) args[0];
        Log employeeDeletedLog = new Log();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        employeeDeletedLog.setUsername(username);
        employeeDeletedLog.setAction("Xóa nhân viên: " + employee.getId());
        employeeDeletedLog.setIdAffected(employee.getId().toString());
        employeeDeletedLog.setTimestamp(LocalDateTime.now());
        logService.save(employeeDeletedLog);
    }

    @Pointcut("execution(* com.codegym.airline_tickets.service.impl.UserService.remove(..))")
    public void deleteUser() {
    }

    @After("deleteUser()")
    public void logAfterDeleteUser(JoinPoint joinPoint) {
        Long userId = (Long) joinPoint.getArgs()[0];
        Log userDeletedLog = new Log();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userDeletedLog.setUsername(username);
        userDeletedLog.setAction("Xóa người dùng: " + userId);
        userDeletedLog.setIdAffected(userId.toString());
        userDeletedLog.setTimestamp(LocalDateTime.now());
        logService.save(userDeletedLog);
    }

    // Ghi log khi xuất báo cáo
    @Pointcut("execution(* com.codegym.airline_tickets.controller.Admin.ReportController.exportExcel(..))")
    public void logAfterExport1() {
    }

    @After("logAfterExport1()")
    public void logAfterExportMethod1() {
        Log exportLog = new Log();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        exportLog.setUsername(username);
        exportLog.setAction("Xuất báo cáo doanh thu theo thời gian thành công");
        exportLog.setIdAffected("N/A");
        exportLog.setTimestamp(LocalDateTime.now());
        logService.save(exportLog);
    }

    @Pointcut("execution(* com.codegym.airline_tickets.controller.Admin.ReportController.exportExcel2(..))")
    public void logAfterExport2() {
    }

    @After("logAfterExport2()")
    public void logAfterExportMethod2() {
        Log exportLog = new Log();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        exportLog.setUsername(username);
        exportLog.setAction("Xuất báo cáo so sánh theo doanh thu thành công");
        exportLog.setIdAffected("N/A");
        exportLog.setTimestamp(LocalDateTime.now());
        logService.save(exportLog);
    }

    //Ghi log khi thêm mới hoặc update hoặc xóa tin tức
    @Pointcut("execution(* com.codegym.airline_tickets.service.impl.NewsService.save(..))")
    public void createNews() {
    }

    @After("createNews()")
    public void logAfterCreateNews(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        News news = (News) args[0];
        Log newsCreatedLog = new Log();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        newsCreatedLog.setUsername(username);
        newsCreatedLog.setAction("Thêm mới/Cập nhật tin tức");
        if (news.getId() != null) {
            newsCreatedLog.setIdAffected(news.getId().toString());
        } else {
            newsCreatedLog.setIdAffected("N/A");
        }
        newsCreatedLog.setTimestamp(LocalDateTime.now());
        logService.save(newsCreatedLog);
    }

    @Pointcut("execution(* com.codegym.airline_tickets.service.impl.NewsService.remove(..))")
    public void deleteNews() {
    }

    @After("deleteNews()")
    public void logAfterDeleteNews(JoinPoint joinPoint) {
        Long newsId = (Long) joinPoint.getArgs()[0];
        Log newsDeletedLog = new Log();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        newsDeletedLog.setUsername(username);
        newsDeletedLog.setAction("Xóa tin tức: " + newsId);
        newsDeletedLog.setIdAffected(newsId.toString());
        newsDeletedLog.setTimestamp(LocalDateTime.now());
        logService.save(newsDeletedLog);
    }
}
