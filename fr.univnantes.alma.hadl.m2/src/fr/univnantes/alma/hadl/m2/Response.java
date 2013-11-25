package fr.univnantes.alma.hadl.m2;

public class Response {
	
	private final Object value;
	
	/**
	 * Indique si la requête correspondante à été traitée
	 * ou si elle doit être déléguée.
	 */
	private final boolean processed;
	
	public Response(Object value, boolean processed){
		this.value = value;
		this.processed = processed;
	}
	
	public Response(Object value){
		this(value, true);
	}
	
	public Object getValue(){
		return value;
	}

	public boolean getProcessed() {
		return this.processed;
	}
}
