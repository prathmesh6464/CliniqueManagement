package com.bridz.clinique_management.service;

import java.io.File;
import java.io.InputStream;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bridz.clinique_management.model.Doctor;
import com.bridz.clinique_management.pattern.GetInstance;
import com.bridz.clinique_management.exception.CliniqueManagementException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class DoctorImplementation {

	private List<Doctor> doctorList = new ArrayList<Doctor>();
	private Scanner scanner = GetInstance.INSTANCE.getScannerInstance();
	private File file = GetInstance.INSTANCE.getFileInstance();
	private ObjectMapper objectMapper = GetInstance.INSTANCE.getObjectMapperInstance();
	private InputStream inputStream;

	public static Logger logger = Logger.getLogger(DoctorImplementation.class);

	public void addDoctor() {

		String name;
		String specialization;
		int hour;
		int minute;
		Doctor doctor = GetInstance.INSTANCE.getDoctorInstance();

		try {
			System.out.println("Please enter doctor information");
			System.out.println("Please enter name of doctor : ");
			name = scanner.next();
			System.out.println("Please enter specialization of doctor : ");
			specialization = scanner.next();
			System.out.println("Please enter Availability time of doctor : ");
			System.out.println("Please enter hour : ");
			hour = scanner.nextInt();
			System.out.println("Please enter minute : ");
			minute = scanner.nextInt();

			doctor.setName(name);
			doctor.setSpecialization(specialization);
			LocalTime availability = LocalTime.of(hour, minute);
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("KK:mm:ss a");
			System.out.println(availability.format(timeFormatter));
			doctor.setAvailability(availability.format(timeFormatter));

		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(500, "Invalid input value");
		}

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			try {
				doctorList = objectMapper.readValue(inputStream, typeReference);
			} catch (MismatchedInputException mismatchedInputException) {
				doctorList.add(doctor);
				objectMapper.writeValue(file, doctorList);
				System.out.println("\nDoctor added successfully\n");
				GetInstance.INSTANCE.getCliniqueInstance().doctor();
			}

			if (doctorList.isEmpty()) {
				doctorList.add(doctor);
			} else {
				doctor.setId(doctorList.size());
				doctorList.add(doctor);
			}

			objectMapper.writeValue(file, doctorList);
			System.out.println("\nDoctor added successfully\n");
			GetInstance.INSTANCE.getCliniqueInstance().doctor();

		} catch (Exception exception) {
			logger.info(exception);
			throw new CliniqueManagementException(509, "Add doctor exception");
		}

	}

	public void showDoctors() {

		System.out.println("\nList of doctors and availability of doctors : \n");

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {
				System.out.println("\nPatient is not available\n");
				GetInstance.INSTANCE.getCliniqueInstance().doctor();
				;
			}

			doctorList.forEach(doctorDetails -> {
				System.out.println("Doctors id : " + doctorDetails.getId() + "            Name : "
						+ doctorDetails.getName() + "            Specialization : " + doctorDetails.getSpecialization()
						+ "            Availability : " + doctorDetails.getAvailability() + "\n");
			});

			GetInstance.INSTANCE.getExtraServicesInstance().onButtonClick();

		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(510, "Show doctor exception");
		}

	}

	public void searchDoctor() {

		System.out.println("Search patien by id, name, specialization : ");
		String searchValue = scanner.next();

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {
				System.out.println("\n Doctors is not available\n");
				GetInstance.INSTANCE.getCliniqueInstance().doctor();
			}

			doctorList.forEach(doctorDetails -> {

				if ((doctorDetails.getId() + "").equals(searchValue)
						|| doctorDetails.getSpecialization().equals(searchValue)
						|| doctorDetails.getName().equals(searchValue)
						|| doctorDetails.getAvailability().equals(searchValue)) {

					System.out.println(
							"Doctors id : " + doctorDetails.getId() + "            Name : " + doctorDetails.getName()
									+ "            Specialization : " + doctorDetails.getSpecialization()
									+ "            Availability : " + doctorDetails.getAvailability() + "\n");
				}
			});

			GetInstance.INSTANCE.getExtraServicesInstance().onButtonClick();

		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(511, "Serach doctor exception");
		}
	}
}
