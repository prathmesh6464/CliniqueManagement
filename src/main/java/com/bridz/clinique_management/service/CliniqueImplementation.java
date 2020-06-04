package com.bridz.clinique_management.service;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.bridz.clinique_management.model.Doctor;
import com.bridz.clinique_management.model.Patient;
import com.bridz.clinique_management.pattern.GetInstance;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

public class CliniqueImplementation implements Clinique {

	List<Doctor> doctorList = new ArrayList<Doctor>();
	List<Patient> patientList = new ArrayList<Patient>();
	Map<String, List<Patient>> dateAndPatient = new HashMap<>();
	List<Map<String, List<Patient>>> appointmentMapList = new ArrayList<>();
	Scanner scanner = GetInstance.INSTANCE.getScannerInstance();
	File file = GetInstance.INSTANCE.getFileInstance();
	ObjectMapper objectMapper = GetInstance.INSTANCE.getObjectMapperInstance();

	public static Logger logger = Logger.getLogger(CliniqueImplementation.class);
	int patientIndex = 0;
	int doctorIndex = 0;

	public void doctor() {

		System.out.println("Please enter number related to option ");
		System.out.println("1. Add doctor");
		System.out.println("2. Show patient list");
		System.out.println("3. Search patient by using id, name or mobile number");
		System.out.println("4. Quite");

		int chosedOption = scanner.nextInt();

		switch (chosedOption) {
		case 1:

			this.addDoctor();
			break;

		case 2:

			break;

		case 3:

			this.addPatient();
			break;

		case 4:

			GetInstance.INSTANCE.getCliniqueControllerInstance().DisplayUserMenu();
			break;

		default:

			System.out.println("Please choose valid option");
			this.patient();
			break;
		}

	}

	public void addDoctor() {

		String name;
		String specialization;
		int hour;
		int minute;
		Doctor doctor = GetInstance.INSTANCE.getDoctorInstance();

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

		doctor.setId(doctorIndex);
		doctor.setName(name);
		doctor.setSpecialization(specialization);
		LocalTime availability = LocalTime.of(hour, minute);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("KK:mm:ss a");
		System.out.println(availability.format(timeFormatter));
		doctor.setAvailability(availability.format(timeFormatter));

		doctorList.add(doctor);
		doctorIndex++;

		try {
			objectMapper.writeValue(file, doctorList);
		} catch (Exception e) {
			logger.info(e);
		}

	}

	public void showPatients() {

	}

	public void patient() {
		System.out.println("Please enter number related to option ");
		System.out.println("1. Show doctor list");
		System.out.println("2. Search doctor by using id, name, specialization or availability");
		System.out.println("3. Add patient");
		System.out.println("4. Quite");
		int chosedOption = scanner.nextInt();

		switch (chosedOption) {
		case 1:

			break;

		case 2:

			break;

		case 3:

			this.addPatient();
			break;
		case 4:

			GetInstance.INSTANCE.getCliniqueControllerInstance().DisplayUserMenu();
			break;

		default:

			System.out.println("Please choose valid option");
			this.patient();
			break;
		}

	}

	public void addPatient() {// date is to checked where doctor available or not

		String name;
		String mobileNumber;
		int age;
		int doctorId;
		int day;
		int month;
		int year;
		LocalDate appointmentDate;
		InputStream inputStream;
		Patient patient = GetInstance.INSTANCE.getPatientInstance();

		System.out.println("Please enter Patient information");
		System.out.println("Please enter name of patient : ");
		name = scanner.next();
		System.out.println("Please enter mobile number of patient : ");
		mobileNumber = scanner.next();
		System.out.print("Please enter age of patient: ");
		age = scanner.nextInt();

		System.out.println("Please enter doctor id to take appointment of doctor");
		doctorId = scanner.nextInt();

		System.out.println("Please enter date for appointment");
		System.out.println("Please enter day of date");
		day = scanner.nextInt();
		System.out.println("Please enter month of date");
		month = scanner.nextInt();
		System.out.println("Please enter year of date");
		year = scanner.nextInt();
		appointmentDate = LocalDate.of(year, month, day);

		patient.setId(patientIndex);
		patient.setName(name);
		patient.setMobileNumber(mobileNumber);
		patient.setAge(age);
		patientIndex++;

		patientList.add(patient);

		dateAndPatient.put(appointmentDate.toString(), patientList);

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {

				System.out.println("Doctor is not available");
				this.addPatient();
			}

			doctorList.forEach(doctorDetails -> {
				if (doctorDetails.getId() == doctorId) {
					appointmentMapList.add(dateAndPatient);
					doctorDetails.setAppointment(appointmentMapList);
					try {
						objectMapper.writeValue(file, doctorList);
					} catch (Exception e) {
						logger.info(e);
					}
				}
			});

		} catch (Exception exception) {
			logger.info(exception, exception);
		}

	}
}
