package fr.univnantes.alma.hadl.m2;

public class Response {
	private final Object value;
	
	public Response(Object value){
		this.value = value;
	}
	
	public Response(){
		this(null);
	}
	
	public Object getValue(){
		return value;
	}
}
