package com.toyota.excel2json.persist;

import org.json.JSONObject;

public interface IJsonPersistanceService {
	void saveJSONObjectToJsonFile(JSONObject jsonObject, String fileName) throws Exception;
	void saveJSONObjectToDatabase(JSONObject jsonObject, String fileName) throws Exception;
}
