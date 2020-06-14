package com.HRManagementSystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.HRManagementSystem.exceptions.ResourceNotFoundException;
import com.HRManagementSystem.model.Employee;
import com.HRManagementSystem.repository.EmployeeRepository;

/**
 * RESTful API for the HR Manangement System
 * 
 * @author Roman Nikolov
 *
 */
@RestController
public class RestServiceController {
	@Autowired
	private EmployeeRepository employeeRepository;


	/**
	 * List all employees
	 * 
	 * @return all employees
	 */
	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		return (List<Employee>) employeeRepository.findAll();
	}

	/**
	 * Get employee by Id
	 * 
	 * @param employeeId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") int employeeId)
			throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		return ResponseEntity.ok().body(employee);
	}

	/**
	 * Add new employee to the database
	 * 
	 * @param employee
	 * @return
	 */
	@PostMapping("/employees")
	public Employee createEmployee(@Validated @RequestBody Employee employee) {
		return employeeRepository.save(employee);
	}

	/**
	 * Updates employee entry 
	 * 
	 * @param employeeId
	 * @param employeeDetails
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") int employeeId,
			@Validated @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employee.setEmail(employeeDetails.getEmail());
		employee.setLastName(employeeDetails.getLastName());
		employee.setFirstName(employeeDetails.getFirstName());
		employee.setAddress(employeeDetails.getAddress());
		employee.setCity(employeeDetails.getCity());
		employee.setCountry(employeeDetails.getCountry());
		employee.setPostcode(employeeDetails.getPostcode());
		employee.setSalary(employeeDetails.getSalary());
		employee.setTelephone(employeeDetails.getTelephone());
		
		final Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}

	/**
	 * Delete employee by ID
	 * 
	 * @param employeeId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") int employeeId)
			throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}