package com.toyota.excel2json.view.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CustomError implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2982278638578787423L;
	
	
	private String code;
	private String userMessage;
	private String systemMessage;
	

	public CustomError(String code, String userMessage, String systemMessage) {
		super();
		this.code = code;
		this.userMessage = userMessage;
		this.systemMessage = systemMessage;
	}
	
}
