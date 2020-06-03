package com.bridz.clinique_management.controller;

import com.bridz.clinique_management.pattern.GetInstance;
import com.bridz.clinique_management.service.CliniqueImplementation;

import java.util.Scanner;

public class CliniqueController {

	Scanner scanner = GetInstance.INSTANCE.getScannerInstance();
	CliniqueImplementation cliniqueImplementation = GetInstance.INSTANCE.getCliniqueInstance();

	public void DisplayUserMenu() {

		System.out.println("Please enter number to click option");
		System.out.println("1. Add Doctor");
		System.out.println("2. Add Patient");
		int chosedOption = scanner.nextInt();

		switch (chosedOption) {
		case 1:

			cliniqueImplementation.addDoctor();
			this.DisplayUserMenu();

			break;

		case 2:

			cliniqueImplementation.addPatient();
			this.DisplayUserMenu();

			break;

		default:

			System.out.println("Please enter valid Option");
			this.DisplayUserMenu();

			break;
		}
	}

}
