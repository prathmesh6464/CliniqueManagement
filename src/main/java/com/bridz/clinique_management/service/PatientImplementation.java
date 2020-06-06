package com.bridz.clinique_management.service;

import java.io.File;
import java.io.InputStream;

import java.time.LocalDate;

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
import com.bridz.clinique_management.exception.CliniqueManagementException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PatientImplementation implements Patients {

	private Scanner scanner = GetInstance.INSTANCE.getScannerInstance();
	private File file = GetInstance.INSTANCE.getFileInstance();
	private ObjectMapper objectMapper = GetInstance.INSTANCE.getObjectMapperInstance();
	private InputStream inputStream;

	public static Logger logger = Logger.getLogger(PatientImplementation.class);

	public void addPatient() {

		String name;
		String mobileNumber;
		int age;
		int doctorId;
		int day;
		int month;
		int year;
		LocalDate appointmentDate;
		Patient patient = GetInstance.INSTANCE.getPatientInstance();
		List<Patient> patientList = new ArrayList<Patient>();
		List<Map<String, List<Patient>>> appointmentMapList = new ArrayList<>();

		try {
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

		} catch (Exception exception) {
			throw new CliniqueManagementException(500, "Invalid input value");
		}

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
							throw new CliniqueManagementException(501, "Object Mapper write value exception");
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
							throw new CliniqueManagementException(501, "Object Mapper write value exception");
						}
					});
				}
			});
		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(503, "Add patient exception");
		}
	}

	public void showPatientInformation() {

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

			GetInstance.INSTANCE.getExtraServicesInstance().onButtonClick();

		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(504, "Show patient information exception");
		}
	}

	public void serachPatient() {

		System.out.println("Search patien by id, name or mobileNumber");
		String searchValue;

		try {
			searchValue = scanner.next();
		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(500, "Invalid input value");
		}

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

			GetInstance.INSTANCE.getExtraServicesInstance().onButtonClick();

		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(506, "Search patient exception");
		}

	}

}
