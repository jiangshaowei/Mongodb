package org.jerry.mongo.coder;

public class BaseManipulate {

	
	
	/***
	 * Check param is not valid
	 * @param param
	 * @return
	 */
	public boolean isParamIsNotValid(String param){
		if(param == null || param.isEmpty()){
			return true;
		}
		return false;
	}
}
