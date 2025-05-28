package com.codegym.airline_tickets.service;

import com.codegym.airline_tickets.dto.UserAccountDTO;
import com.codegym.airline_tickets.dto.UserResponseDTO;
import com.codegym.airline_tickets.entity.User;

public interface IUserService extends IService<User> {
    UserAccountDTO findUserAccountById(long id);
}
