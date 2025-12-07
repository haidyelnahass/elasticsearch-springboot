package com.poc.es.elasticsearchspringboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.es.elasticsearchspringboot.dao.EmployeeDao;
import com.poc.es.elasticsearchspringboot.model.Employee;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    
    
}
