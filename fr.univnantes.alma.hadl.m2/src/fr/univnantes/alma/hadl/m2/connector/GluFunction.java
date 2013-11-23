package fr.univnantes.alma.hadl.m2.connector;

import fr.univnantes.alma.hadl.m2.Request;

abstract class GluFunction {
	
	public static GluFunction identity = null;
	
	static{
		identity = new GluFunction() {
			
			@Override
			Request execute(Request request){
				return request;
			}
		};
	}
	
	abstract Request execute(Request request);
}
