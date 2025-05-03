package com.codegym.airline_tickets.service.impl;

import com.codegym.airline_tickets.dto.EmployeeAccountDTO;
import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.Booking;
import com.codegym.airline_tickets.entity.Employee;
import com.codegym.airline_tickets.entity.Role;
import com.codegym.airline_tickets.repository.AccountRepository;
import com.codegym.airline_tickets.repository.EmployeeRepository;
import com.codegym.airline_tickets.repository.RoleRepository;
import com.codegym.airline_tickets.service.IBookingService;
import com.codegym.airline_tickets.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }
    @Autowired
    private RoleRepository  roleRepository;

    @Override
    public void save(Employee s) {
        employeeRepository.save(s);
    }

    @Override
    public void update(long id, Employee s) {

    }


    public void update(EmployeeAccountDTO dto) {
        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);
        if (account != null) {
            account.setEmail(dto.getEmail());
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                account.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

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





    public List<EmployeeAccountDTO> searchByCodeDTO(String keyword) {
        List<Employee> employees = employeeRepository.findByCodeContainingIgnoreCase(keyword);
        return mapToDTOs(employees);
    }

    public List<EmployeeAccountDTO> searchByFullNameDTO(String keyword) {
        List<Employee> employees = employeeRepository.findByFullNameContainingIgnoreCase(keyword);
        return mapToDTOs(employees);
    }

    private List<EmployeeAccountDTO> mapToDTOs(List<Employee> employees) {
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

            Account account = accountRepository.findByEmployeeId(e.getId());

            if (account != null) {
                dto.setAccountId(account.getId());
                dto.setEmail(account.getEmail());
                dto.setRoleId(account.getRole() != null ? account.getRole().getId() : null);
                dto.setRoleName(account.getRole() != null ? account.getRole().getRoleName() : null);
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

                    .accountId(account != null ? account.getId() : null)
                    .email(account != null ? account.getEmail() : null)
                    .password(null)
                    .roleId(account != null && account.getRole() != null ? account.getRole().getId() : null)
                    .roleName(account != null && account.getRole() != null ? account.getRole().getRoleName() : null)
                    .build();
        }).collect(Collectors.toList());
    }





    @Override
    public Employee updateOrCreate(Employee e) {
        return  employeeRepository.save(e);
    }


    public void updateEmployeeAndAccount(EmployeeAccountDTO dto) {
        // Cập nhật thông tin Employee
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên có id: " + dto.getEmployeeId()));
        employee.setFullName(dto.getFullName());
        employee.setDob(dto.getDob());
        employee.setGender(dto.getGender());
        employee.setPhone(dto.getPhone());
        employee.setAddress(dto.getAddress());
        employeeRepository.save(employee);

        Account account = accountRepository.findByEmployeeId(dto.getEmployeeId());
        if (account == null) {
            throw new RuntimeException("Không tìm thấy tài khoản có employeeId: " + dto.getEmployeeId());
        }
        account.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            account.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        accountRepository.save(account);
    }

    public EmployeeAccountDTO getEmployeeAccountDTOById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        Account account = accountRepository.findByEmployeeId(employeeId);
        if (account == null) {
            throw new RuntimeException("Không tìm thấy tài khoản");
        }

        Role role = roleRepository.findById(account.getRole().getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

        return EmployeeAccountDTO.builder()
                .employeeId(employee.getId())
                .code(employee.getCode())
                .fullName(employee.getFullName())
                .dob(employee.getDob())
                .gender(employee.getGender())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .email(account.getEmail())
                .roleId(role.getId())
                .roleName(role.getRoleName())
                .build();
    }

}
