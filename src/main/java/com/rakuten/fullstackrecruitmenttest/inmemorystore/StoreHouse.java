package com.rakuten.fullstackrecruitmenttest.inmemorystore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.stereotype.Component;

import com.rakuten.fullstackrecruitmenttest.pojo.Employee;

// in memory store house of employee
@Component
public class StoreHouse {

	private List<Employee> employeeStore;
	private List<String[]> errorRecords;

	public StoreHouse() {
		this.employeeStore = new ArrayList<Employee>();
		this.errorRecords = new ArrayList<String[]>();
	}

	public List<Employee> getEmployeeBetweenIds(int startId, int endId) {
		List<Employee> employeeList = new ArrayList<Employee>();

		if (endId > employeeStore.size()) {
			throw new ValidationException(
					"End range" + endId + " for request is more than stored count " + employeeStore.size());
		}

		for (int i = startId - 1; i < endId; i++) {
			employeeList.add(employeeStore.get(i));
		}
		return employeeList;

	}

	public void setErrorRecords(List<String[]> errorRecords) {
		this.errorRecords = errorRecords;
	}

	public void addToErrorRecords(String[] emplMalformedTokens) {
		this.errorRecords.add(emplMalformedTokens);
	}

	public void setEmployeeStore(List<Employee> employeeStore) {
		this.employeeStore = employeeStore;
	}

	public List<Employee> getEmployeeStore() {
		return employeeStore;
	}

	public List<String[]> getErrorRecords() {
		return errorRecords;
	}

	public void addEmployeeToStore(Employee empl) {
		this.employeeStore.add(empl);
	}

	@Override
	public String toString() {
		return "StoreHouse [employeeStore=" + employeeStore + "]";
	}

	public void updateEmployee(int id, Employee empl) {
		if (id > this.employeeStore.size())
			throw new ValidationException("Employee with id : " + id + " doesnt exist");
		this.employeeStore.set(id - 1, empl);

	}

}
