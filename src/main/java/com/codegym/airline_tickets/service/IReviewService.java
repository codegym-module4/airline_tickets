package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.entity.Review;

public interface IReviewService extends IService<Review> {
    Review findByBookId(Long bookId);
}
