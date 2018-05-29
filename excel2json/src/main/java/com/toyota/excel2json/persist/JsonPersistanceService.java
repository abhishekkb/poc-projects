package com.toyota.excel2json.persist;

import java.io.File;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonPersistanceService  implements IJsonPersistanceService {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public void saveJSONObjectToJsonFile(JSONObject jsonObject, String fileName) throws Exception {
		mapper.writeValue(new File("C:\\dev\\coder-one\\jsons\\"+fileName+".json"), jsonObject.toString());
	}
	
	@Override
	public void saveJSONObjectToDatabase(JSONObject jsonObject, String fileName) throws Exception  {
		
	}
	
}
