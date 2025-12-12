package com.payroll.app.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class ResponseUtil {

	public static Map<String,Object>generateResponse(HttpStatus status,String message, Date timestamp, Object data  ){
	Map<String,Object> responce=new HashMap<>();
	responce.put("Status",status);
	responce.put("Message",message);
	responce.put("Timestamp",timestamp);
	responce.put("Data", data);
	return responce;
	
	}
	
	
	public static Map<String,Object>generateResponse(HttpStatus status,String message, Date timestamp  ){
		Map<String,Object> responce=new HashMap<>();
		responce.put("Status",status);
		responce.put("Message",message);
		responce.put("Timestamp",timestamp);
	
		return responce;
		
		}
}
