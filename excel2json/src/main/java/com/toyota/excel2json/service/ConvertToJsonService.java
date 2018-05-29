package com.toyota.excel2json.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.excel2json.view.model.MultipleFiles;

@Service
public class ConvertToJsonService implements IConvertToJsonService {
	
	private DataFormatter dataFormatter = new DataFormatter();
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void convertToJson(MultipleFiles files) throws Exception {
		
		MultipartFile excelFile = files.getExcelFile();
		MultipartFile schemaFile = files.getSchemaFile();
		
		Workbook excelWorkbook = WorkbookFactory.create(excelFile.getInputStream());
		Workbook schemaWorkbook = WorkbookFactory.create(schemaFile.getInputStream());
		
		convertToJson(excelWorkbook, schemaWorkbook);
		
	}
	
	@Override
	public void convertToJson(Workbook excelWorkbook, Workbook schemaWorkbook) throws Exception {
		Sheet excelSheet = excelWorkbook.getSheetAt(1);//excelWorkbook.sheetIterator().next();
		Sheet schemaSheet = schemaWorkbook.getSheetAt(1);//schemaWorkbook.sheetIterator().next();
		
		convertToJson(excelSheet, schemaSheet);
	}
	
	@Override
	public void convertToJson(Sheet excelSheet, Sheet schemaSheet) throws Exception {
		Map<String, String> columnToJsonpathMap = new HashMap<>();
		schemaSheet.forEach(row->{
			columnToJsonpathMap.put(dataFormatter.formatCellValue(row.getCell(0)),
									dataFormatter.formatCellValue(row.getCell(1)));
		});
		
		Iterator<Row> rowIterator = excelSheet.rowIterator();
		for(int rowNumber=0; rowIterator.hasNext(); rowNumber++) {
			saveRowToJsonFile(rowIterator.next(), columnToJsonpathMap, rowNumber);
		}
		
	}
	
	private void saveRowToJsonFile(Row row, Map<String, String> columnToJsonpathMap, int rowNumber) throws Exception {
		
//		JsonNode jsonObject = mapper.createObjectNode();
//		Map<String, Object> jsonObject = new HashMap<>();
		JSONObject jsonObject = new JSONObject();
		
		Iterator<Cell> cellIterator = row.cellIterator();
		
		for(int cellNumber=0; cellIterator.hasNext(); cellNumber++) {
			String cellValue = dataFormatter.formatCellValue(cellIterator.next());
			String jsonPathForThisCell = columnToJsonpathMap.get("" + cellNumber);
			if(jsonPathForThisCell != null) {
				addCellToJsonUsingJsonPath(jsonObject, jsonPathForThisCell, cellValue);
			}
		}
		
		mapper.writeValue(new File("C:\\dev\\coder-one\\jsons\\"+rowNumber+".json"), jsonObject.toString());
		System.out.println(jsonObject.toString());
	}
	
	private void addCellToJsonUsingJsonPath(JSONObject jsonObject, String jsonPathForThisCell, String cellValue) throws JSONException {
		String[] pathParts = jsonPathForThisCell.split("\\.");
		JSONObject tempNode = jsonObject;
		for(int i=0; i<pathParts.length - 1; i++) {
			String part = pathParts[i];
			JSONObject currentNode = null;
			try {
				currentNode = (JSONObject) tempNode.get(part);
			}catch (Exception e) {
				tempNode.put(part, new JSONObject());
				currentNode = (JSONObject) tempNode.get(part);
			}
			tempNode = currentNode;
		}
		tempNode.put(pathParts[pathParts.length-1], cellValue);
		
	}
	
//	private void addCellToJsonUsingJsonPath(Map<String, Object> jsonMap, String jsonPathForThisCell, String cellValue) {
//		String[] pathParts = jsonPathForThisCell.split("\\.");
//		String lastpart="";
//		Map<String, Object> tempNode = jsonMap;
//		for(String part : pathParts) {
//			lastpart = part;
//			Object currentNode = tempNode.get(part);
//			if(currentNode == null) {
//				currentNode = tempNode.put(part, new Object());
//			}
//			currentNode = tempNode;
//		}
//		tempNode.put(lastpart, cellValue);
//	}

//	@SuppressWarnings("deprecation")
//	private void addCellToJsonUsingJsonPath(JsonNode rootNode, 
//											  String jsonPathForThisCell, 
//											  String cellValue) {
//		String[] pathParts = jsonPathForThisCell.split("\\.");
//		JsonNode tempNode = rootNode;
//		String lastpart="";
//		for(String part : pathParts) {
//			lastpart = part;
//			ObjectNode currentNode = (ObjectNode) tempNode.get(part);
//			if(currentNode == null) {
//				currentNode = (ObjectNode) ((ObjectNode) tempNode).put(part, mapper.createObjectNode());
//			}
//			tempNode = currentNode;
//		}
//		((ObjectNode) tempNode).put(lastpart, cellValue);
//		
//	}
	


	public static void main(String[] args) throws Exception {
		
		String excelFilePath="C:\\dev\\coder-one\\excel.xls";
		String schemaFilePath="C:\\dev\\coder-one\\schema.xls";
		Workbook excelWorkbook = WorkbookFactory.create(new FileInputStream(new File(excelFilePath)));
		Workbook schemaWorkbook = WorkbookFactory.create(new FileInputStream(new File(schemaFilePath)));
		
		IConvertToJsonService convertToJsonService = new ConvertToJsonService();
		
		convertToJsonService.convertToJson(excelWorkbook, schemaWorkbook);
		
	}
	
}
