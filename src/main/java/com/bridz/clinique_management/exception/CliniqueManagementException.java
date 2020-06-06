package com.bridz.clinique_management.exception;

public class CliniqueManagementException extends RuntimeException {

	private static final long serialVersionUID = -4005000293040820290L;

	private int errorCode;

	// Constructor @param message
	public CliniqueManagementException(int errorCode, String message) {

		super(message);
		this.errorCode = errorCode;

	}

}
