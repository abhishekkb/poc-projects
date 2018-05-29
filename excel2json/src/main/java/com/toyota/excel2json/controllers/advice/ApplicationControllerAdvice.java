package com.toyota.excel2json.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import com.toyota.excel2json.view.model.CustomError;

@ControllerAdvice
public class ApplicationControllerAdvice {

	@ExceptionHandler(MultipartException.class)
    @ResponseBody
    ResponseEntity<?> handleMultipartExceptionException(Throwable ex) {
    	
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        					 .body(CustomError.builder()
 			   								  .code("400")
 			   								  .userMessage("Size Exceeded - limit 10mb")
 			   								  .systemMessage(ex.getMessage())
 			   								  .build());
    }
    

	@ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<?> handleGenericException(Throwable ex) {
    	
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				 			 .body(CustomError.builder()
				 					 		  .code("500")
				 					 		  .userMessage("Internal Error")
				 					 		  .systemMessage(ex.getMessage())
				 					 		  .build());
    }
}
