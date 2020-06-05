package com.bridz.clinique_management.controller;

import com.bridz.clinique_management.pattern.GetInstance;
import com.bridz.clinique_management.service.Clinique;

import java.util.Scanner;

public class CliniqueController {

	Scanner scanner = GetInstance.INSTANCE.getScannerInstance();
	Clinique clinique = GetInstance.INSTANCE.getCliniqueInstance();

	public void DisplayUserMenu() {

		System.out.println("\n*****************Welcome to Jio clinique******************\n");
		System.out.println("Please enter number to click option\n");
		System.out.println("1. Doctor");
		System.out.println("2. Patient");
		System.out.println("3. Doctor patient report");
		System.out.println("4. Popular specialization");
		System.out.println("5. Popular doctor");
		int chosedOption = scanner.nextInt();

		switch (chosedOption) {
		case 1:

			clinique.doctor();
			this.DisplayUserMenu();

			break;

		case 2:

			clinique.patient();
			this.DisplayUserMenu();

			break;

		case 3:

			clinique.doctorPatientReport();
			this.DisplayUserMenu();

			break;

		case 4:

			clinique.popularSpecialization();
			this.DisplayUserMenu();

			break;

		case 5:

			clinique.popularDoctor();
			this.DisplayUserMenu();

			break;

		default:

			System.out.println("Please enter valid Option");
			this.DisplayUserMenu();

			break;
		}
	}

}
