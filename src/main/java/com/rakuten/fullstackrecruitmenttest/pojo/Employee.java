package com.rakuten.fullstackrecruitmenttest.pojo;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.constraints.NotNull;

public class Employee {

	@NotNull
	private String name;
	@NotNull
	private String dept;
	@NotNull
	private String designation;
	@NotNull
	private int salary;
	@NotNull
	private Date joingingDate;
	private int emplId;
	// thread safe counter to genere AI key in employee storehouse
	private static final AtomicInteger employeeCounter = new AtomicInteger();

	public Employee(String name, String dept, String designation, int salary, Date joiningDate) {
		this.name = name;
		this.dept = dept;
		this.designation = designation;
		this.salary = salary;
		this.joingingDate = joiningDate;
		employeeCounter.getAndIncrement();
		emplId = employeeCounter.get();
	}

	public int getEmplId() {
		return emplId;
	}

	public void setEmplId(int emplId) {
		this.emplId = emplId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public int getSalary() {
		return salary;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", dept=" + dept + ", designation=" + designation + ", salary=" + salary
				+ ", joingingDate=" + joingingDate + ", emplId=" + emplId + "]";
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public Date getJoingingDate() {
		return joingingDate;
	}

	public void setJoingingDate(Date joingingDate) {
		this.joingingDate = joingingDate;
	}

}
