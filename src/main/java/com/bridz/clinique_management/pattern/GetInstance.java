package com.bridz.clinique_management.pattern;

import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.bridz.clinique_management.model.Doctor;
import com.bridz.clinique_management.model.Patient;
import com.bridz.clinique_management.service.CliniqueImplementation;
import com.bridz.clinique_management.service.DoctorImplementation;
import com.bridz.clinique_management.service.PatientImplementation;
import com.bridz.clinique_management.controller.CliniqueController;

public enum GetInstance {

	INSTANCE;

	private String urlOfJsonFile = "C:\\Users\\King\\Documents\\workspace-spring-tool-suite-4-4.6.0.RELEASE\\CliniqueManagement\\CliniqueManagementJsonFile\\CliniqueManagement.json";

	public Scanner getScannerInstance() {
		return new Scanner(System.in);
	}

	public File getFileInstance() {

		return new File(this.urlOfJsonFile);
	}

	public FileInputStream getFileInputStreamInstance() throws FileNotFoundException {
		return new FileInputStream(this.getFileInstance());
	}

	public ObjectMapper getObjectMapperInstance() {
		return new ObjectMapper();
	}

	public Patient getPatientInstance() {
		return new Patient();
	}

	public Doctor getDoctorInstance() {
		return new Doctor();
	}

	public PatientImplementation getPatientsInstance() {
		return new PatientImplementation();
	}

	public DoctorImplementation getDoctorsInstance() {
		return new DoctorImplementation();
	}

	public CliniqueImplementation getCliniqueInstance() {
		return new CliniqueImplementation();
	}

	public CliniqueController getCliniqueControllerInstance() {
		return new CliniqueController();
	}
}
