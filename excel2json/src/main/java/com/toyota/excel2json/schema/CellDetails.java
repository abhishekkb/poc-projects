package com.toyota.excel2json.schema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CellDetails {
	String jsonPath;
	String dataType;
	String columnNumber;
	
	boolean isAnArray;
	
	public CellDetails() {
		super();
	}
	public CellDetails(String jsonPath, String dataType, String columnNumber, boolean isAnArray) {
		super();
		this.jsonPath = jsonPath;
		this.dataType = dataType;
		this.columnNumber = columnNumber;
		this.isAnArray = isAnArray;
	}
}
