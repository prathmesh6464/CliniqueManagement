package com.bridz.clinique_management.service;

import java.io.File;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

import com.bridz.clinique_management.exception.CliniqueManagementException;
import com.bridz.clinique_management.model.Doctor;
import com.bridz.clinique_management.model.Patient;
import com.bridz.clinique_management.pattern.GetInstance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

public class CliniqueImplementation implements Clinique {

	List<Doctor> doctorList = new ArrayList<Doctor>();
	Scanner scanner = GetInstance.INSTANCE.getScannerInstance();
	File file = GetInstance.INSTANCE.getFileInstance();
	ObjectMapper objectMapper = GetInstance.INSTANCE.getObjectMapperInstance();

	public static Logger logger = Logger.getLogger(CliniqueImplementation.class);
	int visitedPetientCount = 0;
	int patientVisitorComparater = 0;
	int chosedOption;
	InputStream inputStream;

	public void doctor() {

		System.out.println("Please enter number related to option ");
		System.out.println("1. Add doctor");
		System.out.println("2. Show doctor list");
		System.out.println("3. Search doctor by using id, name, specialization");
		System.out.println("4. Quite");

		try {
			chosedOption = scanner.nextInt();
		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(500, "Invalid input value");
		}

		switch (chosedOption) {
		case 1:

			GetInstance.INSTANCE.getDoctorsInstance().addDoctor();
			break;

		case 2:
			GetInstance.INSTANCE.getDoctorsInstance().showDoctors();
			break;

		case 3:
			GetInstance.INSTANCE.getDoctorsInstance().searchDoctor();
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

	public void patient() {

		System.out.println("Please enter number related to option ");
		System.out.println("1. Add patient");
		System.out.println("2. Show patient list");
		System.out.println("3. Search patient by using id, name or mobile number");
		System.out.println("4. Quite");

		try {
			chosedOption = scanner.nextInt();
		} catch (Exception exception) {
			logger.info(exception, exception);
		}

		switch (chosedOption) {
		case 1:
			GetInstance.INSTANCE.getPatientsInstance().addPatient();
			;
			break;

		case 2:
			GetInstance.INSTANCE.getPatientsInstance().showPatientInformation();
			;
			break;

		case 3:

			GetInstance.INSTANCE.getPatientsInstance().serachPatient();
			;
			break;
		case 4:

			GetInstance.INSTANCE.getCliniqueControllerInstance().DisplayUserMenu();
			break;

		default:

			System.out.println("\nPlease choose valid option\n");
			this.patient();
			break;
		}

	}

	public void doctorPatientReport() {

		System.out.println("Doctors petient report : ");

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {
				System.out.println("\n Doctors is not available\n");
				this.patient();
			}

			doctorList.forEach(doctorDetails -> {
				List<Map<String, List<Patient>>> doctorDetailsMapList = doctorDetails.getAppointment();
				doctorDetailsMapList.forEach(dateAndPatientsDetail -> {
					dateAndPatientsDetail.entrySet().forEach(eachEntry -> {
						eachEntry.getValue().forEach(patientList -> {
							System.out.println("\nAppointment date : " + eachEntry.getKey() + " : ");
							System.out.println("Doctors id : " + doctorDetails.getId() + " : ");
							System.out.println("Doctors name : " + doctorDetails.getName() + " : ");
							System.out.println("Doctors specialization : " + doctorDetails.getSpecialization() + " : ");
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
			throw new CliniqueManagementException(512, "Doctor patient report");
		}

	}

	public void popularSpecialization() {

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {
				System.out.println("\n Doctors is not available\n");
				this.patient();
			}

			Map<String, Integer> temporaryCalculation = new HashMap<>();

			doctorList.forEach(doctorDetails -> {
				visitedPetientCount = 0;
				List<Map<String, List<Patient>>> doctorDetailsMapList = doctorDetails.getAppointment();
				doctorDetailsMapList.forEach(dateAndPatientsDetail -> {
					dateAndPatientsDetail.entrySet().forEach(eachEntry -> {
						visitedPetientCount += eachEntry.getValue().size();
						temporaryCalculation.put(doctorDetails.getSpecialization(),
								temporaryCalculation.getOrDefault(doctorDetails.getSpecialization(),
										-visitedPetientCount) + visitedPetientCount);
					});
				});
			});

			temporaryCalculation.entrySet().forEach(data -> {
				if (Collections.max(temporaryCalculation.values()) == data.getValue()) {
					System.out.println("Popular specialization : " + data.getKey());
				}
			});

			GetInstance.INSTANCE.getExtraServicesInstance().onButtonClick();

		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(513, "Popular specialization exception");
		}

	}

	public void popularDoctor() {

		System.out.println("Popular doctor : ");

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {
				System.out.println("\n Doctors is not available\n");
				this.patient();
			}

			Map<String, Integer> temporaryCalculation = new HashMap<>();

			doctorList.forEach(doctorDetails -> {
				visitedPetientCount = 0;
				List<Map<String, List<Patient>>> doctorDetailsMapList = doctorDetails.getAppointment();
				doctorDetailsMapList.forEach(dateAndPatientsDetail -> {
					dateAndPatientsDetail.entrySet().forEach(eachEntry -> {
						visitedPetientCount += eachEntry.getValue().size();
						temporaryCalculation.put(doctorDetails.getName(), visitedPetientCount);
					});
				});
			});

			temporaryCalculation.entrySet().forEach(data -> {
				if (Collections.max(temporaryCalculation.values()) == data.getValue()) {
					System.out.println("Popular doctor name : " + data.getKey());
				}
			});

			GetInstance.INSTANCE.getExtraServicesInstance().onButtonClick();

		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(514, "Popular doctor exception");
		}
	}
}
