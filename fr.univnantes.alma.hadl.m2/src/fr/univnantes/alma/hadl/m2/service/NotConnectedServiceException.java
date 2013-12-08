package fr.univnantes.alma.hadl.m2.service;


public class NotConnectedServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotConnectedServiceException(String message) {
		super(message);
	}
}
