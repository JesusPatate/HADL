package fr.univnantes.alma.hadl.m2.connector;

import fr.univnantes.alma.hadl.m2.Request;

abstract class GlueFunction {
	
	public static GlueFunction identity = null;
	
	static{
		identity = new GlueFunction() {
			
			@Override
			Request execute(Request request){
				return request;
			}
		};
	}
	
	abstract Request execute(Request request);
}
