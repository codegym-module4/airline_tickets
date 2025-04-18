package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.dto.EmployeeAccountDTO;
import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Employee;
import com.codegym.airline_tickets.repository.AccountRepository;
import com.codegym.airline_tickets.repository.EmployeeRepository;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateEmployeeIdInAccount(Long accountId, Long employeeId) {
        String sql = "UPDATE accounts SET employee_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, employeeId, accountId);
    }

    @Override
    public void save(Employee s) {
        employeeRepository.save(s);
    }

    @Override
    public void update(long id, Employee s) {

    }


    public void update(EmployeeAccountDTO dto) {
        // Cập nhật Account (nếu có)
        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);
        if (account != null) {
            account.setEmail(dto.getEmail());
            account.setPassword(dto.getPassword()); // nếu bạn cho phép sửa pass
            // Có thể cập nhật role nếu muốn:
            // Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
            // account.setRole(role);
            accountRepository.save(account);
        }

        // Cập nhật Employee
        Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElse(null);
        if (employee != null) {
            employee.setCode(dto.getCode());
            employee.setFullName(dto.getFullName());
            employee.setDob(dto.getDob());
            employee.setGender(dto.getGender());
            employee.setPhone(dto.getPhone());
            employee.setAddress(dto.getAddress());
            employeeRepository.save(employee);
        }
    }


    @Override
    public void remove(Long id) {
        employeeRepository.deleteById(id);

    }

    @Override
    public Employee findById(long id) {
        return employeeRepository.findById(id).get();
    }

    @Override
    public List<Employee> findByName(String name) {
        return List.of();
    }



//    public List<Employee> findByCodeContaining(String keyword) {
//        return employeeRepository.findByCodeContainingIgnoreCase(keyword);
//    }
//
//
//    public List<Employee> findByFullNameContaining(String keyword) {
//        return employeeRepository.findByFullNameContainingIgnoreCase(keyword);
//    }

    public List<EmployeeAccountDTO> searchByCodeDTO(String keyword) {
        List<Employee> employees = employeeRepository.findByCodeContainingIgnoreCase(keyword);
        return mapToDTOs(employees);
    }

    public List<EmployeeAccountDTO> searchByFullNameDTO(String keyword) {
        List<Employee> employees = employeeRepository.findByFullNameContainingIgnoreCase(keyword);
        return mapToDTOs(employees);
    }

    private List<EmployeeAccountDTO> mapToDTOs(List<Employee> employees) {
        List<Account> accounts = accountRepository.findAll();

        List<EmployeeAccountDTO> dtos = new ArrayList<>();

        for (Employee e : employees) {
            EmployeeAccountDTO dto = new EmployeeAccountDTO();
            dto.setEmployeeId(e.getId());
            dto.setCode(e.getCode());
            dto.setFullName(e.getFullName());
            dto.setDob(e.getDob());
            dto.setGender(e.getGender());
            dto.setPhone(e.getPhone());
            dto.setAddress(e.getAddress());

            Account account = accounts.stream()
                    .filter(a -> a.getEmail().equalsIgnoreCase(e.getCode() + "@yourcompany.com"))
                    .findFirst()
                    .orElse(null);

            if (account != null) {
                dto.setAccountId(account.getId());
                dto.setEmail(account.getEmail());
                dto.setRoleId(account.getRole() != null ? account.getRole().getId() : null);
            }

            dtos.add(dto);
        }

        return dtos;
    }


    public List<EmployeeAccountDTO> getEmployeeDTOList() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(employee -> {
            Account account = accountRepository.findByEmployeeId(employee.getId());

            return EmployeeAccountDTO.builder()
                    .employeeId(employee.getId())
                    .code(employee.getCode())
                    .fullName(employee.getFullName())
                    .dob(employee.getDob())
                    .gender(employee.getGender())
                    .phone(employee.getPhone())
                    .address(employee.getAddress())

                    // Gán thông tin từ account nếu có
                    .accountId(account != null ? account.getId() : null)
                    .email(account != null ? account.getEmail() : null)
                    .password(null) // không cần trả password ra view
                    .roleId(account != null && account.getRole() != null ? account.getRole().getId() : null)
                    .roleName(account != null && account.getRole() != null ? account.getRole().getRoleName() : null)
                    .build();
        }).collect(Collectors.toList());
    }



    public EmployeeAccountDTO findDTOById(long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) return null;

        EmployeeAccountDTO dto = new EmployeeAccountDTO();
        dto.setEmployeeId(employee.getId());
        dto.setCode(employee.getCode());
        dto.setFullName(employee.getFullName());
        dto.setDob(employee.getDob());
        dto.setGender(employee.getGender());
        dto.setPhone(employee.getPhone());
        dto.setAddress(employee.getAddress());

        // Tìm account liên quan (giả định theo mã code => email)
        String email = employee.getCode() + "@yourcompany.com";
        Account account = accountRepository.findByEmail(email);

        if (account != null) {
            dto.setAccountId(account.getId());
            dto.setEmail(account.getEmail());
            dto.setRoleId(account.getRole() != null ? account.getRole().getId() : null);
        }

        return dto;
    }

    @Override
    public Employee updateOrCreate(Employee e) {
        return  employeeRepository.save(e);
    }

    public void saveNewEmployeeAndAccount(EmployeeAccountDTO dto) {
        // 1. Tạo Employee
        Employee employee = new Employee();
        employee.setCode(dto.getCode());
        employee.setFullName(dto.getFullName());
        employee.setDob(dto.getDob());
        employee.setGender(dto.getGender());
        employee.setPhone(dto.getPhone());
        employee.setAddress(dto.getAddress());
        employeeRepository.save(employee);

        // 2. Tạo Account
        Account account = new Account();
        account.setEmail(dto.getEmail());
        account.setPassword(dto.getPassword());
        account.setRole(account.getRole()); // hoặc dùng roleRepository.findById(dto.getRoleId())
        accountRepository.save(account);

        // 3. Cập nhật employee_id vào bảng accounts (bằng tay)
        updateEmployeeIdInAccount(account.getId(), employee.getId());
    }

}