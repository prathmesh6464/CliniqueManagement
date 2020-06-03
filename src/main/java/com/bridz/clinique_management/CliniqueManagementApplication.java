package com.bridz.clinique_management;

import com.bridz.clinique_management.pattern.GetInstance;

import org.apache.log4j.Logger;

public class CliniqueManagementApplication {

	public static Logger logger = Logger.getLogger(CliniqueManagementApplication.class);

	public static void main(String[] args) {

		logger.info("Main method called");

		GetInstance.INSTANCE.getCliniqueControllerInstance().DisplayUserMenu();

	}

}
