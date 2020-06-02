package com.bridz.clinique_management.model;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Doctor {

	long id;
	String name;
	String specialization;
	LocalTime availability;// (AM, PM or both)

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
		
		 DateTimeFormatter timeFormatter = DateTimeFormatter
		            .ofPattern("KK:mm:ss a");
		      System.out.println(availability.format(timeFormatter));
		return availability.format(timeFormatter);
	}

	public void setAvailability(int hour, int minute) {

		this.availability = LocalTime.of(hour, minute);

	}

}
