package com.codegym.airline_tickets.controller.Api;

import com.codegym.airline_tickets.entity.Review;
import com.codegym.airline_tickets.response.ResponseObject;
import com.codegym.airline_tickets.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
public class ReviewRestController {

    @Autowired
    private IReviewService reviewService;

    @PostMapping()
    public ResponseEntity<ResponseObject> create(@ModelAttribute Review review) {
        reviewService.save(review);

        return ResponseEntity.ok().body(
                ResponseObject
                        .builder()
                        .url("/review/finish")
                        .build()
        );
    }
}
