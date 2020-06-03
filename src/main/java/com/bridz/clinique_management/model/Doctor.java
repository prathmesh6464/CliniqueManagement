package com.bridz.clinique_management.model;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Doctor {

	private long id;
	private String name;
	private String specialization;
	private LocalTime availability;// (AM, PM or both)
	private List<Map> appointment = new ArrayList<>();
	private Map<LocalDate, Integer> datePatientId = new HashMap<>();

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

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("KK:mm:ss a");
		System.out.println(availability.format(timeFormatter));
		return availability.format(timeFormatter);
	}

	public void setAvailability(int hour, int minute) {

		this.availability = LocalTime.of(hour, minute);

	}

	public List<Map> getAppointment() {
		return appointment;
	}

	public void setAppointment(List<Map> appointment) {
		this.appointment = appointment;
	}

	public Map<LocalDate, Integer> getDatePatientId() {
		return datePatientId;
	}

	public void setDatePatientId(Map<LocalDate, Integer> datePatientId) {
		this.datePatientId = datePatientId;
	}

}
