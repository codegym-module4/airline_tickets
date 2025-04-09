package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private IBookingService bookingService;

    @GetMapping()
    public String payment(Model model) {
        List<Booking> list = bookingService.findByStatusAndUserId(1, 1);
        model.addAttribute("list", list);

        return "user/payment/payment";
    }

    @GetMapping("/transfer-history")
    public String history(Model model) {
        List<Booking> list = bookingService.getAll();
        model.addAttribute("list", list);
        return "user/payment/transfer_history";
    }

    @GetMapping("/payment-callback")
    public String paymentCallback(@RequestParam Map<String, String> params, Model model) {
//        vnp_Amount=585000000&vnp_BankCode=NCB&vnp_BankTranNo=VNP14895195&vnp_CardType=ATM&vnp_OrderInfo=Thanh+toan+don+hang%3A78501758&vnp_PayDate=20250408144303&vnp_ResponseCode=00&vnp_TmnCode=UGUN2SYV&vnp_TransactionNo=14895195&vnp_TransactionStatus=00&vnp_TxnRef=78501758&vnp_SecureHash=cfc08f50708513fc6ef99f687ee306818d79a4c9b24bb323db0c51f706f9b2f6dd08ff876aa21bca853764e2cceb98ee76d1270861246241e0d3d93139598cc1
        String status = params.get("vnp_ResponseCode");
        String vnp_TxnRef = params.get("vnp_TxnRef");
        if (Objects.equals(status, "00")) {
            bookingService.updateStatusByVnPayId(vnp_TxnRef, 2);

            return "redirect:/payment/success";
        }

        return "redirect:/payment/fail";
    }

    @GetMapping("/success")
    public String success(Model model) {

        return "user/payment/success";
    }

    @GetMapping("/fail")
    public String fail(Model model) {

        return "user/payment/fail";
    }

    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Booking booking = bookingService.findById(id);
        if (booking == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thông tin thanh toán!!!");

            return "redirect:/payment";
        }

        bookingService.updateStatusById(id, 3);
        redirectAttributes.addFlashAttribute("message", "Hủy đặt vé thành công");

        return "redirect:/payment";
    }
}
