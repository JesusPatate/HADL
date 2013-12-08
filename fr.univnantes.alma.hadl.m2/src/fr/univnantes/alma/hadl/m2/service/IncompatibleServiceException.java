package fr.univnantes.alma.hadl.m2.service;


public class IncompatibleServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public IncompatibleServiceException(String message) {
		super(message);
	}
}
