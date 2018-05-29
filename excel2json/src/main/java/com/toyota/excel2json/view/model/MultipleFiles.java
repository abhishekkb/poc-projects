package com.toyota.excel2json.view.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class MultipleFiles {
	private String someField;

    private MultipartFile schemaFile;
    private MultipartFile excelFile;
    
	public MultipleFiles(String someField, MultipartFile schemaFile, MultipartFile excelFile) {
		super();
		this.someField = someField;
		this.schemaFile = schemaFile;
		this.excelFile = excelFile;
	}
    
    

}
