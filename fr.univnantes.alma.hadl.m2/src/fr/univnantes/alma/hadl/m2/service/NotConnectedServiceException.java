package fr.univnantes.alma.hadl.m2.service;


public class NotConnectedServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotConnectedServiceException(Service service, 
			String element) {
		super(String.format("%s not added in %s",
				service.getLabel(), element));
	}
}
