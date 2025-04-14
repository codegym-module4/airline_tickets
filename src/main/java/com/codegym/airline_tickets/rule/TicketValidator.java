package com.codegym.airline_tickets.rule;


import com.codegym.airline_tickets.annotation.ValidTicket;
import com.codegym.airline_tickets.dto.BookingTicketDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketValidator implements ConstraintValidator<ValidTicket, BookingTicketDTO> {

    @Override
    public boolean isValid(BookingTicketDTO item, ConstraintValidatorContext context) {
        if (item == null) return false;
        boolean isValid = true;

        Integer customerType = item.getCustomerType();
        String phone = item.getPhone();
        String email = item.getEmail();
        String citizenIdentification = item.getCitizenIdentification();
        String nationality = item.getNationality();

        if (customerType == 1) {
            if (phone == null || phone.equals("")){
                addConstraintViolation(context, "phone", "Số điện thoại không được để trống.");
                isValid = false;
            }
            if (email == null || email.equals("")) {
                addConstraintViolation(context, "email", "Email không được để trống.");
                isValid = false;
            }
            if (citizenIdentification == null || citizenIdentification.equals("")){
                addConstraintViolation(context, "citizenIdentification", "Số căn cước không được để trống.");
                isValid = false;
            }
            if (nationality == null || nationality.equals("")){
                addConstraintViolation(context, "nationality", "Quốc gia không được để trống.");
                isValid = false;
            }
        }

        return isValid;
    }

    // Hàm helper để thêm lỗi cụ thể cho từng trường
    private void addConstraintViolation(ConstraintValidatorContext context, String field, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}