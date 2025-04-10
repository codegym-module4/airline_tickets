package com.codegym.airline_tickets.controller.Admin;

import com.codegym.airline_tickets.entity.Employee;
import com.codegym.airline_tickets.service.impl.EmployeeService;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


}
