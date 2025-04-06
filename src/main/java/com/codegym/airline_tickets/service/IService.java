package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.dto.RevenueByDateDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IService<T>{

    List<T> getAll();

    void save(T s);

    void update(long id, T s);

    void remove(Long id);

    T findById(long id);

    List<T> findByName(String name);

}
