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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

            Optional<Account> accountOpt = accountRepository.findByEmployeeId(e.getId());
            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
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
            Optional<Account> accountOpt = accountRepository.findByEmployeeId(employee.getId());

            return EmployeeAccountDTO.builder()
                    .employeeId(employee.getId())
                    .code(employee.getCode())
                    .fullName(employee.getFullName())
                    .dob(employee.getDob())
                    .gender(employee.getGender())
                    .phone(employee.getPhone())
                    .address(employee.getAddress())
                    .accountId(accountOpt.map(Account::getId).orElse(null))
                    .email(accountOpt.map(Account::getEmail).orElse(null))
                    .password(null)
                    .roleId(accountOpt.map(a -> a.getRole() != null ? a.getRole().getId() : null).orElse(null))
                    .roleName(accountOpt.map(a -> a.getRole() != null ? a.getRole().getRoleName() : null).orElse(null))
                    .build();
        }).collect(Collectors.toList());
    }





    @Override
    public Employee updateOrCreate(Employee e) {
        return  employeeRepository.save(e);
    }



    @Transactional
    public void updateEmployeeAndAccount(EmployeeAccountDTO dto) {
        System.out.println("====== BẮT ĐẦU CẬP NHẬT ======");
        System.out.println("DTO nhận được: " + dto);
        System.out.println("employeeId: " + dto.getEmployeeId());
        System.out.println("accountId: " + dto.getAccountId());
        System.out.println("email: " + dto.getEmail());
        System.out.println("roleId: " + dto.getRoleId());
        System.out.println("password: " + dto.getPassword());

        // 1. Lấy thông tin nhân viên
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        System.out.println("Tìm thấy nhân viên: " + employee.getFullName());


        // 2. Lấy thông tin tài khoản
        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        System.out.println("Tìm thấy tài khoản: " + account.getEmail());

        // 3. Kiểm tra email đã tồn tại chưa (trừ khi giữ nguyên email cũ)
        Optional<Account> emailExistedOpt = accountRepository.findByEmail(dto.getEmail());
        if (emailExistedOpt.isPresent()) {
            Account emailExisted = emailExistedOpt.get();

            System.out.println("Email đã tồn tại trong DB: " + emailExisted.getEmail());

            if (!emailExisted.getId().equals(account.getId())) {
                throw new RuntimeException("Email đã được sử dụng.");
            }
        }


        // 4. Cập nhật thông tin nhân viên
        employee.setFullName(dto.getFullName());
        employee.setPhone(dto.getPhone());
        employee.setGender(dto.getGender());
        employee.setDob(dto.getDob());
        employee.setAddress(dto.getAddress());

        // 5. Cập nhật thông tin tài khoản
        account.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
        account.setRole(role);

        System.out.println("Gán vai trò: " + role.getRoleName());


        // 6. Lưu lại
        employeeRepository.save(employee);
        accountRepository.save(account);

        System.out.println("✅ Cập nhật thành công nhân viên và tài khoản.");
        System.out.println("====== KẾT THÚC CẬP NHẬT ======");
    }






    public EmployeeAccountDTO getEmployeeAccountDTOById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        Account account = accountRepository.findByEmployeeId(employeeId).orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        Role role = roleRepository.findById(account.getRole().getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

        return EmployeeAccountDTO.builder()
                .employeeId(employee.getId())
                .accountId(account.getId())
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
