package com.bridz.clinique_management.model;

public class Patient {

	long id;
	String name;
	String mobileNumber;
	long Age;

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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public long getAge() {
		return Age;
	}

	public void setAge(long age) {
		Age = age;
	}

}
