package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.Role;
import com.codegym.airline_tickets.entity.User;
import com.codegym.airline_tickets.repository.UserRepository;
import com.codegym.airline_tickets.service.IRoleService;
import com.codegym.airline_tickets.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public void save(User s) {

    }

    @Override
    public void update(long id, User s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public User findById(long id) {
        return userRepository.findNotDeletedById(id);
    }

    @Override
    public List<User> findByName(String name) {
        return List.of();
    }
}