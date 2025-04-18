package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.dto.EmployeeAccountDTO;
import com.codegym.airline_tickets.entity.Account;
import com.codegym.airline_tickets.entity.Employee;
import com.codegym.airline_tickets.entity.Role;
import com.codegym.airline_tickets.repository.AccountRepository;
import com.codegym.airline_tickets.repository.EmployeeRepository;
import com.codegym.airline_tickets.repository.RoleRepository;
import com.codegym.airline_tickets.service.IAccountService;
import com.codegym.airline_tickets.service.impl.EmployeeService;
import com.codegym.airline_tickets.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private IAccountService accountService;


    // ============================ LIST ===================================
    @GetMapping("/listEmployee")
    public String listEmployees(Model model) {
        List<EmployeeAccountDTO> employeeList = employeeService.getEmployeeDTOList();
        model.addAttribute("employeeList", employeeList);
        return "admin/employee/listEmployee";
    }

    // ============================ SEARCH =================================
    @GetMapping("/search")
    public String search(Model model,
                         @RequestParam("filter") String filter,
                         @RequestParam("keyword") String keyword) {
        List<EmployeeAccountDTO> resultList;

        if ("id".equals(filter)) {
            resultList = employeeService.searchByCodeDTO(keyword);
        } else {
            resultList = employeeService.searchByFullNameDTO(keyword);
        }

        model.addAttribute("employeeList", resultList);
        return "admin/employee/listEmployee";
    }

    // ============================ DELETE =================================
    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        // Tìm Employee
        Optional<Employee> employeeOpt = employeeRepository.findById(id);

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();

            // Tìm Account của Employee
            Account account = accountRepository.findByEmployeeId(employee.getId());

            if (account != null) {
                // Xóa Account tương ứng
                accountRepository.delete(account);
            }

            // Xóa Employee
            employeeRepository.delete(employee);

            redirectAttributes.addFlashAttribute("message", "Xóa nhân viên và tài khoản thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy nhân viên.");
        }

        return "redirect:/admin/employee/listEmployee";
    }

    // ============================ CREATE GET =============================
    @GetMapping("/createEmployee")
    public String createForm(Model model) {
        model.addAttribute("employeeAccountDTO", new EmployeeAccountDTO());
        model.addAttribute("roleList", roleService.getAll());
        return "admin/employee/createEmployee";
    }

    // ============================ CREATE POST ============================
    @PostMapping("/createEmployee")
    public String create(@ModelAttribute("employeeAccountDTO") EmployeeAccountDTO dto,
                         RedirectAttributes redirect,
                         Model model) {

        // Kiểm tra mã nhân viên trùng
        Optional<Employee> existing = employeeRepository.findByCode(dto.getCode());
        if (existing.isPresent()) {
            model.addAttribute("error", "Mã nhân viên đã tồn tại.");
            model.addAttribute("roleList", roleService.getAll());
            return "admin/employee/createEmployee";
        }

        // Kiểm tra vai trò
        Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
        if (role == null) {
            model.addAttribute("error", "Vai trò không hợp lệ.");
            model.addAttribute("roleList", roleService.getAll());
            return "admin/employee/createEmployee";
        }

        Employee employee = new Employee();
        employee.setCode(dto.getCode());
        employee.setFullName(dto.getFullName());
        employee.setDob(dto.getDob());
        employee.setGender(dto.getGender());
        employee.setPhone(dto.getPhone());
        employee.setAddress(dto.getAddress());
        Employee e = employeeService.updateOrCreate(employee);

        Account account = new Account();
        account.setEmail(dto.getEmail());
        account.setPassword(dto.getPassword()); // 👉 Nên mã hóa nếu triển khai thật
        account.setRole(role);
        account.setEmployee(e);
        accountService.save(account);

        // Sau khi tạo xong, cập nhật employee_id trong bảng account
//        updateEmployeeIdInAccount(account.getId(), employee.getId());

        redirect.addFlashAttribute("message", "Thêm nhân viên thành công!");
        return "redirect:/admin/employee/listEmployee";
    }
//    // ============================ UPDATE GET =============================
//    @GetMapping("/updateEmployee")
//    public String updateForm(@RequestParam("id") Long id, Model model) {
//        EmployeeAccountDTO dto = employeeService.findDTOById(id);
//        if (dto != null) {
//            model.addAttribute("employeeAccountDTO", dto);
//            model.addAttribute("roleList", roleService.getAll());
//            return "admin/employee/updateEmployee";
//        }
//
//        model.addAttribute("message", "Không tìm thấy nhân viên.");
//        return "redirect:/admin/employee/listEmployee";
//    }
//
//
//    // ============================ UPDATE POST ============================
//    @PostMapping("/updateEmployee")
//    public String update(@ModelAttribute("employeeAccountDTO") EmployeeAccountDTO dto,
//                         RedirectAttributes redirect,
//                         Model model) {
//
//        // Kiểm tra mã nhân viên trùng (trừ chính nó)
//        Optional<Employee> existing = employeeRepository.findByCode(dto.getCode());
//        if (existing.isPresent() && !existing.get().getId().equals(dto.getEmployeeId())) {
//            model.addAttribute("error", "Mã nhân viên đã tồn tại.");
//            model.addAttribute("roleList", roleService.getAll());
//            return "admin/employee/updateEmployee";
//        }
//
//        // Cập nhật employee
//        Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElse(null);
//        if (employee != null) {
//            employee.setCode(dto.getCode());
//            employee.setFullName(dto.getFullName());
//            employee.setDob(dto.getDob());
//            employee.setGender(dto.getGender());
//            employee.setPhone(dto.getPhone());
//            employee.setAddress(dto.getAddress());
//            employeeRepository.save(employee);
//        }
//
//        // Cập nhật account
//        Account account = accountRepository.findById(dto.getAccountId()).orElse(null);
//        if (account != null) {
//            account.setEmail(dto.getEmail());
//            account.setPassword(dto.getPassword()); // Nên mã hóa mật khẩu ở đây
//            Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
//            account.setRole(role);
//            accountRepository.save(account);
//        }
//
//        // Cập nhật lại employee_id trong bảng accounts
//        updateEmployeeIdInAccount(account.getId(), employee.getId());
//
//        redirect.addFlashAttribute("message", "Cập nhật nhân viên thành công!");
//        return "redirect:/admin/employee/listEmployee";
//    }
//




}
