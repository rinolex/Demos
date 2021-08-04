package com.HRManagementSystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping(value="/rest")
public class RestServiceController {
	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * List all employees
	 * 
	 * @return all employees
	 */
	@GetMapping(value = "/showEmployees", produces = MediaType.APPLICATION_JSON_VALUE)
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
	@GetMapping(value = "/getEmployee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> getEmployee(@PathVariable(value = "id") int employeeId)
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
	@RequestMapping(value = "/addEmployee", 
			method={RequestMethod.POST, RequestMethod.GET})
	public ResponseEntity<Employee> addEmployee(@Validated @RequestBody Employee employee) {
		employeeRepository.save(employee);
		return ResponseEntity.ok().body(employee);
	}

	/**
	 * Updates employee entry 
	 * 
	 * @param employeeId
	 * @param employeeDetails
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/updateEmployee/{id}",
			method={RequestMethod.PUT, RequestMethod.GET},
			produces = MediaType.APPLICATION_JSON_VALUE)
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
		return ResponseEntity.ok().body(updatedEmployee);
	}

	/**
	 * Delete employee by ID
	 * 
	 * @param employeeId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@RequestMapping(value = "/deleteEmployee/{id}", 
			method={RequestMethod.DELETE, RequestMethod.GET}, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") int employeeId)
			throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}