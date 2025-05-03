package com.codegym.airline_tickets.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^NV\\d{2,}$", message = "Sai định dạng mã nhân viên. Mẫu đúng: NV01, NV123, ...")
    private String code;

    @NotEmpty(message = "Họ tên không được để trống")
    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @NotEmpty(message = "Giới tính không được để trống")
    private String gender;

    @NotEmpty(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải từ 10-11 số")
    private String phone;

    @NotEmpty(message = "Số điện thoại không được để trống")
    private String address;






    private Long accountId;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private String password;

    @NotNull(message = "Vai trò không được để trống")
    private Long roleId;

    private String roleName;


}
