package com.codegym.airline_tickets.repository;

import com.codegym.airline_tickets.dto.UserAccountDTO;
import com.codegym.airline_tickets.dto.UserResponseDTO;
import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT code FROM users ORDER BY code DESC LIMIT 1", nativeQuery = true)
    String findMaxCode();


    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL")
    User findNotDeletedById(long id);

    @Query(value = "SELECT new com.codegym.airline_tickets.dto.UserAccountDTO(u.id, u.fullName, u.dob, u.gender, u.phone, a.email, u.address, u.nationality, u.citizenIdentification) from User u JOIN Account a ON u.id = a.user.id where u.id = :id")
    UserAccountDTO findUserAccountById(long id);

}
