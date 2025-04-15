package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.entity.Employee;
import com.codegym.airline_tickets.service.impl.EmployeeService;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/listEmployee")
    public String listEmployee(Model model) {
        List<Employee> employeeList = employeeService.getAll();
        model.addAttribute("employeeList", employeeList);
        return "admin/employee/listEmployee";
    }

    @GetMapping("/search")
    public String search( Model model,
                         @RequestParam("filter") String filter,
                         @RequestParam("keyword") String keyword) {
        List<Employee> resultList;
        if ("id".equals(filter)) {
            resultList = employeeService.findByCodeContaining(keyword);
        }else {
                resultList=employeeService.findByFullNameContaining(keyword);
        }
        model.addAttribute("employeeList", resultList);
        return "admin/employee/listEmployee";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") long id) {
         employeeService.remove(id);
         return "redirect:/admin/employee/listEmployee";
    }

    @GetMapping("/createEmployee")
    public String create(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "admin/employee/createEmployee";
    }

    @PostMapping("/createEmployee")
    public String create(@ModelAttribute("employee") Employee employee,
                         RedirectAttributes redirect) {
        employeeService.save(employee);
        redirect.addFlashAttribute("message", "Thêm vào danh sách thành công");
        return "redirect:/admin/employee/listEmployee";
    }

    @GetMapping("/updateEmployee")
    public String update(@RequestParam("id") long id, Model model) {
        Employee employee = employeeService.findById(id);
        if (employee != null) {
            model.addAttribute("employee", employee);
            return "admin/employee/updateEmployee";
        } else {
            model.addAttribute("message", "Không tìm thấy nhân viên.");
            return "redirect:/admin/employee/listEmployee";
        }
    }

    @PostMapping("/updateEmployee")
    public String update(@ModelAttribute("employee") Employee employee,
                         RedirectAttributes redirect) {
        employeeService.save(employee);
        redirect.addFlashAttribute("message", "Cập nhật nhân viên thành công");
        return "redirect:/admin/employee/listEmployee";
    }

}
