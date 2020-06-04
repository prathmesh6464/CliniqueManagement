package com.bridz.clinique_management.model;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Doctor {

	private long id;
	private String name;
	private String specialization;
	private String availability;// (AM, PM or both)
	private List<Map<String, List<Patient>>> appointment = new ArrayList<Map<String, List<Patient>>>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {

		this.availability = availability;
	}

	public List<Map<String, List<Patient>>> getAppointment() {
		return appointment;
	}

	public void setAppointment(List<Map<String, List<Patient>>> appointment) {
		this.appointment = appointment;
	}

}
