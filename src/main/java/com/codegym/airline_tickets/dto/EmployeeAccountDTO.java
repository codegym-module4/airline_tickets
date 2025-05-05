package com.codegym.airline_tickets.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAccountDTO {

    private Long employeeId;

    @NotEmpty(message = "Mã nhân viên không được để trống")
    private String code;

    @NotEmpty(message = "Họ tên không được để trống")
    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @NotEmpty(message = "Giới tính không được để trống")
    private String gender;

    @NotEmpty(message = "Số điện thoại không được để trống")
    private String phone;

    private String address;






    private Long accountId;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotEmpty(message = "Mật khẩu không được để trống")
    private String password;

    @NotNull(message = "Vai trò không được để trống")
    private Long roleId;

    private String roleName;


}
