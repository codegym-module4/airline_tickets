package com.codegym.airline_tickets.controller.User;

import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Review;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @Autowired
    private IBookingService bookingService;

    @GetMapping("/{bookingId}")
    public String showReview(@PathVariable("bookingId") Long bookingId, Model model, RedirectAttributes redirectAttributes) {
        Review review = reviewService.findByBookId(bookingId);
        if (review != null) {
            return "redirect:/review/finish";
        }

        Booking booking = bookingService.findById(bookingId);
        if (booking == null) {
            redirectAttributes.addFlashAttribute("messageError", "Không tìm thấy thông tin chuyến bay");
            return "redirect:/";
        }

        model.addAttribute("booking", booking);
        model.addAttribute("review", review);

        return "user/review/index";
    }

    @GetMapping("/finish")
    public String showFinish(Model model) {
        return "user/review/finish";
    }
}
