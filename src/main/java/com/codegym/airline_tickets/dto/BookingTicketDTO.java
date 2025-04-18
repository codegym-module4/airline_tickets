package com.codegym.airline_tickets.dto;

import com.codegym.airline_tickets.annotation.ValidTicket;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidTicket
public class BookingTicketDTO {

    private Long id;

    @NotEmpty(message = "Họ và tên không được để trống")
    private String fullName;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    @NotEmpty(message = "Giới tính không được để trống")
    private String gender;

//    @NotEmpty(message = "Số điện thoại không được để trống")
    private String phone;
    private Integer extraKgGo;
    private Integer extraKgReturn;

//    @NotEmpty(message = "Quốc gia không được để trống")
    private String nationality;

//    @NotEmpty(message = "Email không được để trống")
    private String email;

//    @NotEmpty(message = "Số Căn cước không được để trống")
    private String citizenIdentification;
    private Integer customerType;
}
