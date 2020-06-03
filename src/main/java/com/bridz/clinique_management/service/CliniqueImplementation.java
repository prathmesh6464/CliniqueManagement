package com.bridz.clinique_management.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bridz.clinique_management.CliniqueManagementApplication;
import com.bridz.clinique_management.model.Doctor;
import com.bridz.clinique_management.model.Patient;
import com.bridz.clinique_management.pattern.GetInstance;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

public class CliniqueImplementation implements Clinique {

	private List<Doctor> doctorList = new ArrayList<Doctor>();
	private List<Patient> patientList = new ArrayList<Patient>();
	private Scanner scanner = GetInstance.INSTANCE.getScannerInstance();
	private File file = GetInstance.INSTANCE.getFileInstance();
	private ObjectMapper objectMapper = GetInstance.INSTANCE.getObjectMapperInstance();
	private Doctor doctor = GetInstance.INSTANCE.getDoctorInstance();
	private Patient patient = GetInstance.INSTANCE.getPatientInstance();
	public static Logger logger = Logger.getLogger(CliniqueImplementation.class);
	int patientIndex = 0;
	int doctorIndex = 0;

	public void addDoctor() {

		String name;
		String specialization;
		int hour;
		int minute;

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
		doctor.setAvailability(hour, minute);

		doctorList.add(doctor);
		doctorIndex++;

		try {
			objectMapper.writeValue(file, doctorList);
		} catch (Exception e) {
			logger.info(e);
		}

	}

	public void addPatient() {

		String name;
		String mobileNumber;
		int age;

		System.out.println("Please enter Patient information");
		System.out.println("Please enter name of patient : ");
		name = scanner.next();
		System.out.println("Please enter mobile number of patient : ");
		mobileNumber = scanner.next();
		System.out.print("Please enter age of patient: ");
		age = scanner.nextInt();

		patient.setAge(patientIndex);
		patient.setName(name);
		patient.setMobileNumber(mobileNumber);
		patient.setAge(age);
		patientIndex++;

		patientList.add(patient);
		try {
			objectMapper.writeValue(file, patientList);
		} catch (Exception e) {
			logger.info(e);
		}

	}
}
