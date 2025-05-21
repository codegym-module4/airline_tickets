package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Review;
import com.codegym.airline_tickets.repository.ReviewRepository;
import com.codegym.airline_tickets.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    @Override
    public void save(Review s) {
        reviewRepository.save(s);
    }

    @Override
    public void update(long id, Review s) {

    }

    @Override
    public void remove(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public Review findById(long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    @Override
    public List<Review> findByName(String name) {
        return List.of();
    }

    @Override
    public Review findByBookId(Long bookId) {
        return reviewRepository.findByBookingId(bookId);
    }
}
