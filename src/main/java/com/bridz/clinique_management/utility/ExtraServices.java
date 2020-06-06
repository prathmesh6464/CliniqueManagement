package com.bridz.clinique_management.utility;

import org.apache.log4j.Logger;

import com.bridz.clinique_management.exception.CliniqueManagementException;
import com.bridz.clinique_management.pattern.GetInstance;
import com.bridz.clinique_management.service.CliniqueImplementation;

public class ExtraServices {

	public static Logger logger = Logger.getLogger(ExtraServices.class);

	public void onButtonClick() {
		try {
			if (System.in.read() != -1) {
				GetInstance.INSTANCE.getCliniqueControllerInstance().DisplayUserMenu();
			}
		} catch (Exception exception) {
			logger.info(exception, exception);
			throw new CliniqueManagementException(520, "On button click exception");
		}
	}
}
