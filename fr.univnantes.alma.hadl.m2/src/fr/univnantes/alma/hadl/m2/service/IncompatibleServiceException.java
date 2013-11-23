package fr.univnantes.alma.hadl.m2.service;


public class IncompatibleServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public IncompatibleServiceException(Service s1, Service s2) {
		super(String.format("%s incompatible with %s", s1.getLabel(),
				s2.getLabel()));
	}
}
