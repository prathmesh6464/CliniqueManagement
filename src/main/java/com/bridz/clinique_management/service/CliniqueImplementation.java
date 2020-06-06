package com.bridz.clinique_management.service;

import java.io.File;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.bridz.clinique_management.model.Doctor;
import com.bridz.clinique_management.model.Patient;
import com.bridz.clinique_management.pattern.GetInstance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import org.apache.log4j.Logger;

public class CliniqueImplementation implements Clinique {

	List<Doctor> doctorList = new ArrayList<Doctor>();
	Scanner scanner = GetInstance.INSTANCE.getScannerInstance();
	File file = GetInstance.INSTANCE.getFileInstance();
	ObjectMapper objectMapper = GetInstance.INSTANCE.getObjectMapperInstance();

	public static Logger logger = Logger.getLogger(CliniqueImplementation.class);
//	int patientIndex = 0;
	int visitedPetientCount = 0;
	int patientVisitorComparater = 0;

	public void doctor() {

		System.out.println("Please enter number related to option ");
		System.out.println("1. Add doctor");
		System.out.println("2. Show doctor list");
		System.out.println("3. Search doctor by using id, name, specialization");
		System.out.println("4. Quite");

		int chosedOption = scanner.nextInt();

		switch (chosedOption) {
		case 1:

			this.addDoctor();
			break;

		case 2:
			this.showDoctors();
			break;

		case 3:
			this.searchDoctor();
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

		int chosedOption = scanner.nextInt();

		switch (chosedOption) {
		case 1:
			this.addPatient();
			;
			break;

		case 2:
			this.showPatientInformation();
			;
			break;

		case 3:

			this.serachPatient();
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

	public void addDoctor() {

		String name;
		String specialization;
		int hour;
		int minute;
		Doctor doctor = GetInstance.INSTANCE.getDoctorInstance();
		InputStream inputStream;

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

		doctor.setName(name);
		doctor.setSpecialization(specialization);
		LocalTime availability = LocalTime.of(hour, minute);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("KK:mm:ss a");
		System.out.println(availability.format(timeFormatter));
		doctor.setAvailability(availability.format(timeFormatter));

		try {

			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			try {
				List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);
			} catch (MismatchedInputException mismatchedInputException) {
				logger.info(mismatchedInputException, mismatchedInputException);
				doctorList.add(doctor);
				objectMapper.writeValue(file, doctorList);
				System.out.println("\nDoctor added successfully\n");
				this.doctor();
			}

			if (doctorList.isEmpty()) {
				doctorList.add(doctor);
			} else {
				doctor.setId(doctorList.size());
				doctorList.add(doctor);
			}

			objectMapper.writeValue(file, doctorList);
			System.out.println("\nDoctor added successfully\n");
			this.doctor();
		} catch (Exception e) {
			logger.info(e);
		}

	}

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

//		patient.setId(patientIndex);
		patient.setName(name);
		patient.setMobileNumber(mobileNumber);
		patient.setAge(age);
//		patientIndex++;

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {

				System.out.println("\nDoctor is not available\n");
				this.patient();
			}

			doctorList.forEach(doctorDetails -> {
				if (doctorDetails.getId() == doctorId) {

					if (doctorDetails.getAppointment().isEmpty()) {

						patient.setId(doctorDetails.getAppointment().size());
						patientList.add(patient);
						Map<String, List<Patient>> dateAndPatientDetails = new HashMap<>();
						dateAndPatientDetails.put(appointmentDate.toString(), patientList);
//						patientIndex++;

						appointmentMapList.add(dateAndPatientDetails);
						doctorDetails.setAppointment(appointmentMapList);

						try {
							objectMapper.writeValue(file, doctorList);
							System.out.println("\nPatient added successfully\n");
							this.patient();
						} catch (Exception exception) {
							logger.info(exception, exception);
						}

					}

					doctorDetails.getAppointment().forEach(dateAndPatientDetails -> {

						if (dateAndPatientDetails.containsKey(appointmentDate.toString())) {

							if (dateAndPatientDetails.get(appointmentDate.toString()).size() >= 5) {
								System.out.println("\n Appointment is full please choose next date of appointment \n");
								this.patient();
							} else {
								patient.setId(dateAndPatientDetails.get(appointmentDate.toString()).size());
								dateAndPatientDetails.get(appointmentDate.toString()).add(patient);
//								patientIndex++;
							}
						} else {

							if (dateAndPatientDetails.get(appointmentDate.toString()) == null) {
								patient.setId(0);
							} else {
								patient.setId(dateAndPatientDetails.get(appointmentDate.toString()).size());
							}
							patientList.add(patient);
							dateAndPatientDetails.put(appointmentDate.toString(), patientList);
//							patientIndex++;
						}

						appointmentMapList.add(dateAndPatientDetails);
						doctorDetails.setAppointment(appointmentMapList);

						try {
							objectMapper.writeValue(file, doctorList);
							System.out.println("\nPatient added successfully\n");
							this.patient();
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
				this.patient();
			}

			doctorList.forEach(doctorDetails -> {
				List<Map<String, List<Patient>>> doctorDetailsMapList = doctorDetails.getAppointment();
				doctorDetailsMapList.forEach(dateAndPatientsDetail -> {
					dateAndPatientsDetail.entrySet().forEach(eachEntry -> {
						eachEntry.getValue().forEach(patientList -> {
							System.out.println("Appointment date : " + eachEntry.getKey() + " : ");
							System.out.println("Patient id :" + patientList.getId());
							System.out.println("Patient Name :" + patientList.getName());
							System.out.println("Patient mobile number :" + patientList.getMobileNumber());
							System.out.println("Patient age :" + patientList.getAge());
							System.out.println("\n#############################################\n");
						});
					});
				});
			});

			if (System.in.read() != -1) {
				this.patient();
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
				this.patient();
			}

			doctorList.forEach(doctorDetails -> {

				List<Map<String, List<Patient>>> doctorDetailsMapList = doctorDetails.getAppointment();
				doctorDetailsMapList.forEach(dateAndPatientsDetail -> {

					dateAndPatientsDetail.entrySet().forEach(eachEntry -> {

						eachEntry.getValue().forEach(patientList -> {

							int index = 0;
							long isValuePresentOrNot = patientList.getId();
							if ((patientList.getId() + "").equals(searchValue)
									|| patientList.getName().equals(searchValue)
									|| patientList.getMobileNumber().equals(searchValue)) {
								System.out.println("\nAppointment date : " + eachEntry.getKey() + " : ");
								System.out.println("Patient id :" + patientList.getId());
								System.out.println("Patient Name :" + patientList.getName());
								System.out.println("Patient mobile number :" + patientList.getMobileNumber());
								System.out.println("Patient age :" + patientList.getAge());
								System.out.println("\n#############################################\n");
								isValuePresentOrNot++;
							}
						});
					});
				});
			});

			if (System.in.read() != -1) {
				this.doctor();
			}

		} catch (Exception exception) {
			logger.info(exception, exception);
		}

	}

	public void showDoctors() {

		System.out.println("\nList of doctors and availability of doctors : \n");

		InputStream inputStream;

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {

				System.out.println("\nPatient is not available\n");
				this.patient();
			}

			doctorList.forEach(doctorDetails -> {

				System.out.println("Doctor id : " + doctorDetails.getId() + "            Name : "
						+ doctorDetails.getName() + "            Specialization : " + doctorDetails.getSpecialization()
						+ "            Availability : " + doctorDetails.getAvailability() + "\n");
			});

			if (System.in.read() != -1) {
				this.doctor();
			}

		} catch (Exception exception) {
			logger.info(exception, exception);
		}

	}

	public void searchDoctor() {

		InputStream inputStream;

		System.out.println("Search patien by id, name, specialization : ");
		String searchValue = scanner.next();

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {

				System.out.println("\n Doctor is not available\n");
				this.patient();
			}

			doctorList.forEach(doctorDetails -> {

				if ((doctorDetails.getId() + "").equals(searchValue)
						|| doctorDetails.getSpecialization().equals(searchValue)
						|| doctorDetails.getName().equals(searchValue)
						|| doctorDetails.getAvailability().equals(searchValue)) {

					System.out.println(
							"Doctor id : " + doctorDetails.getId() + "            Name : " + doctorDetails.getName()
									+ "            Specialization : " + doctorDetails.getSpecialization()
									+ "            Availability : " + doctorDetails.getAvailability() + "\n");
				}
			});

			if (System.in.read() != -1) {
				this.patient();
			}

		} catch (Exception exception) {
			logger.info(exception, exception);
		}

	}

	public void doctorPatientReport() {

		InputStream inputStream;

		System.out.println("Doctor petient report : ");

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {

				System.out.println("\n Doctor is not available\n");
				this.patient();
			}

			doctorList.forEach(doctorDetails -> {
				List<Map<String, List<Patient>>> doctorDetailsMapList = doctorDetails.getAppointment();
				doctorDetailsMapList.forEach(dateAndPatientsDetail -> {
					dateAndPatientsDetail.entrySet().forEach(eachEntry -> {
						eachEntry.getValue().forEach(patientList -> {
							System.out.println("\nAppointment date : " + eachEntry.getKey() + " : ");
							System.out.println("Doctor id : " + doctorDetails.getId() + " : ");
							System.out.println("Doctor name : " + doctorDetails.getName() + " : ");
							System.out.println("Doctor specialization : " + doctorDetails.getSpecialization() + " : ");
							System.out.println("Patient id :" + patientList.getId());
							System.out.println("Patient Name :" + patientList.getName());
							System.out.println("Patient mobile number :" + patientList.getMobileNumber());
							System.out.println("Patient age :" + patientList.getAge());
							System.out.println("\n#############################################\n");
						});
					});
				});
			});

			if (System.in.read() != -1) {
				GetInstance.INSTANCE.getCliniqueControllerInstance().DisplayUserMenu();
			}

		} catch (Exception exception) {
			logger.info(exception, exception);
		}

	}

	public void popularSpecialization() {

		InputStream inputStream;

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {

				System.out.println("\n Doctor is not available\n");
				this.patient();
			}

			Map<String, Integer> temporaryCalculation = new HashMap<>();

			doctorList.forEach(doctorDetails -> {
				visitedPetientCount = 0;
				List<Map<String, List<Patient>>> doctorDetailsMapList = doctorDetails.getAppointment();
				doctorDetailsMapList.forEach(dateAndPatientsDetail -> {
					dateAndPatientsDetail.entrySet().forEach(eachEntry -> {
						visitedPetientCount += eachEntry.getValue().size();
						int previousValue = temporaryCalculation.getOrDefault(doctorDetails.getSpecialization(), 0);
						int value = 0;
						if (previousValue == 0) {
							value = -visitedPetientCount;
							System.out.println();
						}
						temporaryCalculation.put(doctorDetails.getSpecialization(),
								temporaryCalculation.getOrDefault(doctorDetails.getSpecialization(), value)
										+ visitedPetientCount);

					});
				});
			});

			temporaryCalculation.entrySet().forEach(data -> {
				if (Collections.max(temporaryCalculation.values()) == data.getValue()) {

					System.out.println("Popular specialization : " + data.getKey() + "  value : " + data.getValue());
				}
			});

			if (System.in.read() != -1) {
				GetInstance.INSTANCE.getCliniqueControllerInstance().DisplayUserMenu();
			}

		} catch (Exception exception) {
			logger.info(exception, exception);
		}

	}

	public void popularDoctor() {

		InputStream inputStream;

		System.out.println("Popular doctor : ");

		try {
			inputStream = GetInstance.INSTANCE.getFileInputStreamInstance();
			TypeReference<List<Doctor>> typeReference = new TypeReference<List<Doctor>>() {
			};

			List<Doctor> doctorList = objectMapper.readValue(inputStream, typeReference);

			if (doctorList.isEmpty()) {

				System.out.println("\n Doctor is not available\n");
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

			if (System.in.read() != -1) {
				GetInstance.INSTANCE.getCliniqueControllerInstance().DisplayUserMenu();
			}

		} catch (Exception exception) {
			logger.info(exception, exception);
		}
	}
}
