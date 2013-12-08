package fr.univnantes.alma.hadl.m2;

public class Response {
	
	private final Object value;
	
	/**
	 * Indique si la requête correspondante à été traitée
	 * ou si elle doit être déléguée.
	 */
	private final boolean processed;
	private final String message;
	
	public Response(Object value, boolean processed, String message){
		this.value = value;
		this.processed = processed;
		this.message = message;
	}
	
	public Response(Object value){
		this(value, true, "");
	}
	
	public Object getValue(){
		return value;
	}

	public boolean getProcessed() {
		return processed;
	}
	
	public String getMessage() {
		return message;
	}
}
