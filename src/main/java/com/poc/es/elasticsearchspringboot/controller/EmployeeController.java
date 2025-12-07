package com.poc.es.elasticsearchspringboot.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.es.elasticsearchspringboot.dao.EmployeeDao;
import com.poc.es.elasticsearchspringboot.exception.ResourceNotFoundException;
import com.poc.es.elasticsearchspringboot.model.Audit;
import com.poc.es.elasticsearchspringboot.model.Employee;
import com.poc.es.elasticsearchspringboot.service.AuditService;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private AuditService auditService;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() throws IOException {
        List<Employee> employees = employeeDao.findAll();
		Audit audit = new Audit("2", "12345", "tenant", "op", "cat", "proc", "nodod", "oooo");
		System.out.println(audit);
		String status = auditService.insertAudit(audit);
		System.out.println("Status" + status);
        return employees;
    }

    @PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {
        System.out.println("Creating a new Employee!");
		return employeeDao.save(employee);
	}

	@GetMapping("/employees/{id}")
	public Employee getEmployeeById(@PathVariable(value = "id") Long employeeId) {
		return employeeDao.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
	}

	@PutMapping("/employees/{id}")
	public Employee updateEmployee(@PathVariable(value = "id") Long employeeId, @RequestBody Employee employeeDetails) {

		Employee employee = employeeDao.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

		Employee updatedEmployee = employeeDao.save(employee);
		return updatedEmployee;
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable(value = "id") Long employeeId) {
		Employee employee = employeeDao.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

		employeeDao.delete(employee);

		return ResponseEntity.ok().build();
	}
}
