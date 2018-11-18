package com.rakuten.fullstackrecruitmenttest.inmemorystore;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.stereotype.Component;

import com.rakuten.fullstackrecruitmenttest.pojo.Employee;

// in memory store house of employee
@Component
public class StoreHouse {

	private List<Employee> employeeStore;

	public List<Employee> getEmployeeStore() {
		return employeeStore;
	}

	@Override
	public String toString() {
		return "StoreHouse [employeeStore=" + employeeStore + "]";
	}

	public void setEmployeeStore(List<Employee> employeeStore) {
		this.employeeStore = employeeStore;
	}

	public StoreHouse() {
		this.employeeStore = new ArrayList<Employee>();
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

	public void addEmployeeToStore(Employee empl) {
		this.employeeStore.add(empl);
	}
}
