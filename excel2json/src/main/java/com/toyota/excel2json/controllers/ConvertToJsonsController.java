package com.toyota.excel2json.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toyota.excel2json.service.IConvertToJsonService;
import com.toyota.excel2json.view.model.MultipleFiles;

@RestController
public class ConvertToJsonsController {

	@Autowired
	public IConvertToJsonService convertToJsonService;
	
	@PostMapping("/{collectionName}/files")
	public ResponseEntity<?> postFileDataToCollection(@ModelAttribute MultipleFiles files, 
													  @PathVariable String collectionName) throws Exception {
		
		convertToJsonService.convertToJson(files);
		
		return ResponseEntity.status(HttpStatus.OK)
							 .body("Successfully converted - ");

	}
	
	
	
}
