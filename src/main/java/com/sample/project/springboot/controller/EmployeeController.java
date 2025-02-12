package com.sample.project.springboot.controller;

import com.sample.project.springboot.dto.EmployeeRequest;
import com.sample.project.springboot.dto.EmployeeResponse;
import com.sample.project.springboot.model.Employee;
import com.sample.project.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employeeList = employeeService.getAllEmployees();
        return entityToEmployeeResponse(employeeList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public EmployeeResponse createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeRequestToEntity(employeeRequest);
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return entityToEmployeeResponse(List.of(savedEmployee)).get(0);
    }

    private Employee employeeRequestToEntity(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setName(employeeRequest.getName());
        employee.setAge(employeeRequest.getAge());
        employee.setSalary(employeeRequest.getSalary());
        employee.setAddress(employeeRequest.getAddress());
        employee.setDateOfBirth(employeeRequest.getDateOfBirth());
        return employee;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            Employee updatedEmployee = employee.get();
            updatedEmployee.setName(employeeDetails.getName());
            updatedEmployee.setAge(employeeDetails.getAge());
            updatedEmployee.setSalary(employeeDetails.getSalary());
            updatedEmployee.setAddress(employeeDetails.getAddress());
            updatedEmployee.setDateOfBirth(employeeDetails.getDateOfBirth());
            return ResponseEntity.ok(employeeService.saveEmployee(updatedEmployee));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    private List<EmployeeResponse> entityToEmployeeResponse(List<Employee> employeeList) {
        return employeeList.stream().map(employee -> {
            EmployeeResponse employeeResponse = new EmployeeResponse();
            employeeResponse.setId(employee.getId());
            employeeResponse.setName(employee.getName());
            employeeResponse.setAge(employee.getAge());
            employeeResponse.setSalary(employee.getSalary());
            employeeResponse.setAddress(employee.getAddress());
            employeeResponse.setDateOfBirth(employee.getDateOfBirth());
            return employeeResponse;
        }).collect(Collectors.toList());

    }
}