package com.toyota.excel2json.config;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ExcelToJsonApplicationConfig {

	@Bean
	public ObjectMapper getMapper() {
		return new ObjectMapper();
	}
	
	@Bean
	public DataFormatter getDataFormatter() {
		return new DataFormatter();
	}
	
}
