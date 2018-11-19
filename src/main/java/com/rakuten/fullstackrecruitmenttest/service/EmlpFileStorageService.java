package com.rakuten.fullstackrecruitmenttest.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.rakuten.fullstackrecruitmenttest.config.FileStorageProperties;
import com.rakuten.fullstackrecruitmenttest.controller.FileController;
import com.rakuten.fullstackrecruitmenttest.exception.FileStorageException;
import com.rakuten.fullstackrecruitmenttest.exception.MyFileNotFoundException;
import com.rakuten.fullstackrecruitmenttest.inmemorystore.StoreHouse;
import com.rakuten.fullstackrecruitmenttest.pojo.Employee;
import com.rakuten.fullstackrecruitmenttest.util.CSVGenerator;
import com.rakuten.fullstackrecruitmenttest.util.Constants;

@Service
public class EmlpFileStorageService {

	private final Path fileStorageLocation;
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	@Autowired
	private StoreHouse emplStoreHouse;

	@Autowired
	public EmlpFileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public StoreHouse getEmplStoreHouse() {
		return emplStoreHouse;
	}

	public void setEmplStoreHouse(StoreHouse emplStoreHouse) {
		this.emplStoreHouse = emplStoreHouse;
	}

	public String storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			addCSVtoEmplStore(file);
			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public void addCSVtoEmplStore(MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			BufferedReader fileReader = null;
			String[] tokens = null;
			try {
				// read file
				fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				fileReader.readLine();// read header
				String row = null;
				// read employee row
				while ((row = fileReader.readLine()) != null) {
					tokens = row.split(",");
					// validate employee
					Employee empl = validategAndGetEmployeeFromTokens(tokens);
					// add employee to in memory store
					this.emplStoreHouse.addEmployeeToStore(empl);
				}
			} catch (ValidationException validationExcept) {
				// if validations fails then create an error record entry of malformed tokens

				// add extra column for failure message
				int currentTokensLen = (tokens != null) ? tokens.length + 1 : 1;
				String[] malformedTokens = new String[currentTokensLen];

				int i = 0;
				for (; i < currentTokensLen - 1; ++i) {
					malformedTokens[i] = tokens[i];
				}
				malformedTokens[i] = validationExcept.getMessage();

				this.emplStoreHouse.addToErrorRecords(malformedTokens);
				throw validationExcept;
			}

			finally {
				if (fileReader != null)
					fileReader.close(); // close reader
			}
		} else {
			throw new ValidationException("Uploaded file is empty");
		}

	}

	private Employee validategAndGetEmployeeFromTokens(String[] tokens) {
		String name = tokens[Constants.EMPL_NAME_IDX];
		String dept = tokens[Constants.EMPL_DEPT_IDX];
		String designation = tokens[Constants.EMPL_DESG_IDX];
		String salary = tokens[Constants.EMPL_SALARY_IDX];
		String date = tokens[Constants.EMPL_JOINING_DATE_IDX];

		if (!name.matches("[A-Za-z0-9]+")) {
			throw new ValidationException("Employee name : " + name + " should contain only alphanumeric characters");
		}
		;
		if (!dept.matches("[A-Za-z0-9-_*]+")) {
			throw new ValidationException(
					"Employee department : " + dept + " should contain only alphanumeric or -_* special characters");
		}
		if (!Constants.DESGINATION_SET.contains(designation)) {
			throw new ValidationException("Employee designation : " + designation + " not found");
		}
		if (!salary.matches("^[1-9]\\d*$")) {
			throw new ValidationException("Employee salary : " + salary + " is invalid");
		}

		int emplSalary = Integer.parseInt(salary);

		// validate date format
		SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
		sdfrmt.setLenient(false);
		Date joiningDate;
		try {
			joiningDate = sdfrmt.parse(date);
		} catch (ParseException e) {
			throw new ValidationException(date + " is Invalid Date format .Acceptable format is : yyyy-MM-dd");
		}

		return new Employee(name, dept, designation, emplSalary, joiningDate);
	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			try {
				CSVGenerator csvGen = new CSVGenerator();
				csvGen.createCSVFile(emplStoreHouse.getEmployeeStore(), emplStoreHouse.getErrorRecords());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}

		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}

	public Employee updateEmployee(int id, Employee empl) {
		// TODO Auto-generated method stub
		String emplTokens[] = new String[Constants.TOTAL_COL];
		emplTokens[Constants.EMPL_NAME_IDX] = empl.getName();
		emplTokens[Constants.EMPL_DEPT_IDX] = empl.getDept();
		emplTokens[Constants.EMPL_DESG_IDX] = empl.getDesignation();
		emplTokens[Constants.EMPL_SALARY_IDX] = String.valueOf(empl.getSalary());

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = formatter.format(empl.getJoingingDate());
		emplTokens[Constants.EMPL_JOINING_DATE_IDX] = strDate;

		empl = validategAndGetEmployeeFromTokens(emplTokens);
		empl.setEmplId(id);
		this.emplStoreHouse.updateEmployee(id, empl);
		return empl;

	}
}