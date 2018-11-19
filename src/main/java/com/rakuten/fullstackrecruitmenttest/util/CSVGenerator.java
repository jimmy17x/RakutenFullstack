package com.rakuten.fullstackrecruitmenttest.util;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.opencsv.CSVWriter;
import com.rakuten.fullstackrecruitmenttest.pojo.Employee;

public class CSVGenerator {

	private CSVWriter csvWriter;
	private String currentFileName;
	private static final String[] CSV_HEADER = { "ID", "Name", "Department", "Designation", "Salary", "Joining Date" };

	public CSVGenerator() throws IOException {
		/*
		 * this.currentFileName = "AllEmployeeRecords" + System.currentTimeMillis() +
		 * ".csv";
		 */ this.currentFileName = "./employee_records/AllEmployeeRecords.csv";
		this.csvWriter = new CSVWriter(new FileWriter(currentFileName), CSVWriter.DEFAULT_SEPARATOR,
				CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
	}

	public void createCSVFile(List<Employee> employeeStore, List<String[]> errorRecords) throws IOException {
		csvWriter.writeNext(CSV_HEADER);

		// add valid employees
		for (Employee empl : employeeStore) {

			String date = formatDate(empl.getJoingingDate(), "yyyy-MM-dd");
			String[] data = { String.valueOf(empl.getEmplId()), empl.getName(), empl.getDept(), empl.getDesignation(),
					String.valueOf(empl.getSalary()), date };

			csvWriter.writeNext(data);
		}

		String[] errorHeader = { "Error Records" };
		csvWriter.writeNext(errorHeader);
		csvWriter.writeAll(errorRecords);

		csvWriter.flush();
		csvWriter.close();

	}

	private String formatDate(Date joingingDate, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String strDate = formatter.format(joingingDate);
		return strDate;

	}
}
