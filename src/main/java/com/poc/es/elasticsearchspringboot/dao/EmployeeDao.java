package com.poc.es.elasticsearchspringboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poc.es.elasticsearchspringboot.model.Employee;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Long> {
}
