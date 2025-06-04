package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.entity.ChatMessage;
import com.codegym.airline_tickets.repository.ChatRepository;
import com.codegym.airline_tickets.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class    ChatService implements IChatService {

    private final Map<String,String> customerToEmployee = new ConcurrentHashMap<>();
    private final Map<String,String> employeeToCustomer = new ConcurrentHashMap<>();

    @Autowired
    private ChatRepository chatRepository;


    @Override
    public void sendMessage(ChatMessage message, Principal principal) {}

    @Override
    public void sendMessageToUser(String recipient, String content) {}

    @Override
    public String getRandomEmployeeName() {
        String[] EMPLOYEE_NAMES = {
                "admin1@example.com",
                "admin1@example.com",
                "admin1@example.com"
        };

        for (String employee : EMPLOYEE_NAMES) {
            if (!employeeToCustomer.containsKey(employee)) {
                return employee;
            }
        }

        return null;

    }

    public String assignEmployeeToSession(String customerName) {
        if (customerToEmployee.containsKey(customerName)) {
            return customerToEmployee.get(customerName);
        } else {
            String employeeName = getRandomEmployeeName();
            if (employeeName != null) {
                customerToEmployee.put(customerName, employeeName);
                employeeToCustomer.put(employeeName, customerName);
            }
            return employeeName;
        }
    }

    public String findCustomerBySession(String employeeName) {
        for (Map.Entry<String, String> entry : customerToEmployee.entrySet()) {
            if (entry.getValue().equals(employeeName)) {
                return entry.getKey();
            }
        }
        return null;
    }


    public void removeSession(String customerName) {
        String employeeName = customerToEmployee.remove(customerName);
        if (employeeName != null) {
            employeeToCustomer.remove(employeeName);
        }
    }

    public List<ChatMessage> getAllMessageByUserName(String userName) {
        return chatRepository.getAllMessageByUserName(userName);
    }

    @Override
    public void save(ChatMessage s) {
        chatRepository.save(s);
    }






    @Override
    public List<ChatMessage> getAll() {
        return List.of();
    }



    @Override
    public void update(long id, ChatMessage s) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public ChatMessage findById(long id) {
        return null;
    }

    @Override
    public List<ChatMessage> findByName(String name) {
        return List.of();
    }


}
