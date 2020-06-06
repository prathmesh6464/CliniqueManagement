package com.bridz.clinique_management.service;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bridz.clinique_management.model.Patient;
import com.bridz.clinique_management.model.Doctor;
import com.bridz.clinique_management.pattern.GetInstance;
import com.bridz.clinique_management.service.Patients;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class PatientImplementation implements Patients {

	List<Doctor> doctorList = new ArrayList<Doctor>();
	Scanner scanner = GetInstance.INSTANCE.getScannerInstance();
	File file = GetInstance.INSTANCE.getFileInstance();
	ObjectMapper objectMapper = GetInstance.INSTANCE.getObjectMapperInstance();

	public static Logger logger = Logger.getLogger(CliniqueImplementation.class);
	int visitedPetientCount = 0;
	int patientVisitorComparater = 0;

	public void addPatient() {

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
		List<Patient> patientList = new ArrayList<Patient>();
		List<Map<String, List<Patient>>> appointmentMapList = new ArrayList<>();

		System.out.println("Please enter Patients information");
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

		patient.setName(name);
		patient.setMobileNumber(mobileNumber);
		patient.setAge(age);

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);
			
			if (doctorList.isEmpty()) {

				System.out.println("\nDoctor is not available\n");
				GetInstance.INSTANCE.getCliniqueInstance().patient();
			}

			doctorList.forEach(doctorDetails -> {
				if (doctorDetails.getId() == doctorId) {

					if (doctorDetails.getAppointment().isEmpty()) {

						patient.setId(doctorDetails.getAppointment().size());
						patientList.add(patient);
						Map<String, List<Patient>> dateAndPatientDetails = new HashMap<>();
						dateAndPatientDetails.put(appointmentDate.toString(), patientList);

						appointmentMapList.add(dateAndPatientDetails);
						doctorDetails.setAppointment(appointmentMapList);

						try {
							objectMapper.writeValue(file, doctorList);
							System.out.println("\nPatient added successfully\n");
							GetInstance.INSTANCE.getCliniqueInstance().patient();
						} catch (Exception exception) {
							logger.info(exception, exception);
						}

					}

					doctorDetails.getAppointment().forEach(dateAndPatientDetails -> {

						if (dateAndPatientDetails.containsKey(appointmentDate.toString())) {

							if (dateAndPatientDetails.get(appointmentDate.toString()).size() >= 5) {
								System.out.println("\n Appointment is full please choose next date of appointment \n");
								GetInstance.INSTANCE.getCliniqueInstance().patient();
							} else {
								patient.setId(dateAndPatientDetails.get(appointmentDate.toString()).size());
								dateAndPatientDetails.get(appointmentDate.toString()).add(patient);

							}
						} else {

							if (dateAndPatientDetails.get(appointmentDate.toString()) == null) {
								patient.setId(0);
							} else {
								patient.setId(dateAndPatientDetails.get(appointmentDate.toString()).size());
							}
							patientList.add(patient);
							dateAndPatientDetails.put(appointmentDate.toString(), patientList);

						}

						appointmentMapList.add(dateAndPatientDetails);
						doctorDetails.setAppointment(appointmentMapList);

						try {
							objectMapper.writeValue(file, doctorList);
							System.out.println("\nPatient added successfully\n");
							GetInstance.INSTANCE.getCliniqueInstance().patient();
						} catch (Exception exception) {
							logger.info(exception, exception);
						}

					});
				}
			});

		} catch (Exception exception) {
			logger.info(exception, exception);
		}

	}

	public void showPatientInformation() {
		InputStream inputStream;

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {

				System.out.println("\nPatient is not available\n");
				GetInstance.INSTANCE.getCliniqueInstance().patient();
			}

			doctorList.forEach(doctorDetails -> {
				List<Map<String, List<Patient>>> doctorDetailsMapList = doctorDetails.getAppointment();
				doctorDetailsMapList.forEach(dateAndPatientsDetail -> {
					dateAndPatientsDetail.entrySet().forEach(eachEntry -> {
						eachEntry.getValue().forEach(patientList -> {
							System.out.println("Appointment date : " + eachEntry.getKey() + " : ");
							System.out.println("Patients id :" + patientList.getId());
							System.out.println("Patients Name :" + patientList.getName());
							System.out.println("Patients mobile number :" + patientList.getMobileNumber());
							System.out.println("Patients age :" + patientList.getAge());
							System.out.println("\n#############################################\n");
						});
					});
				});
			});

			if (System.in.read() != -1) {
				GetInstance.INSTANCE.getCliniqueInstance().patient();
			}

		} catch (Exception exception) {
			logger.info(exception, exception);
		}

	}

	public void serachPatient() {

		InputStream inputStream;

		System.out.println("Search patien by id, name or mobileNumber");
		String searchValue = scanner.next();

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {

				System.out.println("\nPatient is not available\n");
				GetInstance.INSTANCE.getCliniqueInstance().patient();
			}

			doctorList.forEach(doctorDetails -> {

				List<Map<String, List<Patient>>> doctorDetailsMapList = doctorDetails.getAppointment();
				doctorDetailsMapList.forEach(dateAndPatientsDetail -> {

					dateAndPatientsDetail.entrySet().forEach(eachEntry -> {

						eachEntry.getValue().forEach(patientList -> {

							long isValuePresentOrNot = patientList.getId();
							if ((patientList.getId() + "").equals(searchValue)
									|| patientList.getName().equals(searchValue)
									|| patientList.getMobileNumber().equals(searchValue)) {
								System.out.println("\nAppointment date : " + eachEntry.getKey() + " : ");
								System.out.println("Patients id :" + patientList.getId());
								System.out.println("Patients Name :" + patientList.getName());
								System.out.println("Patients mobile number :" + patientList.getMobileNumber());
								System.out.println("Patients age :" + patientList.getAge());
								System.out.println("\n#############################################\n");
								isValuePresentOrNot++;
							}
						});
					});
				});
			});

			if (System.in.read() != -1) {
				GetInstance.INSTANCE.getCliniqueInstance().patient();
			}

		} catch (Exception exception) {
			logger.info(exception, exception);
		}

	}

}
