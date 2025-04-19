package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.News;
import com.codegym.airline_tickets.repository.NewsRepository;
import com.codegym.airline_tickets.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService implements INewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public List<News> getAll() {
        return newsRepository.findAll();
    }

    @Override
    public void save(News s) {
        newsRepository.save(s);
    }

    @Override
    public void update(long id, News s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public News findById(long id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Override
    public List<News> findByName(String name) {
        return List.of();
    }
}
