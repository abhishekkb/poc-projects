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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.toyota.excel2json.schema.CellDetails;
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
		
		convertToJson2(excelSheet, schemaSheet);
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
	
	@Override
	public void convertToJson2(Sheet excelSheet, Sheet schemaSheet) throws Exception {
		Map<String, CellDetails> columnToJsonpathMap = new HashMap<>();
		schemaSheet.forEach(row->{
			CellDetails cellDetails = CellDetails.builder()
												 .columnNumber(dataFormatter.formatCellValue(row.getCell(0)))
												 .jsonPath(dataFormatter.formatCellValue(row.getCell(1)))
												 .dataType(dataFormatter.formatCellValue(row.getCell(2)))
												 .build();
			columnToJsonpathMap.put(dataFormatter.formatCellValue(row.getCell(0)),
									cellDetails);
		});
		
		Iterator<Row> rowIterator = excelSheet.rowIterator();
		for(int rowNumber=0; rowIterator.hasNext(); rowNumber++) {
			saveRowToJsonFile2(rowIterator.next(), columnToJsonpathMap, rowNumber);
		}
		
	}
	private void saveRowToJsonFile2(Row row, Map<String, CellDetails> columnToJsonpathMap, int rowNumber) throws Exception {
		
		JsonNode jsonObject = mapper.createObjectNode();
		
		Iterator<Cell> cellIterator = row.cellIterator();
		
		for(int cellNumber=0; cellIterator.hasNext(); cellNumber++) {
			String cellValue = dataFormatter.formatCellValue(cellIterator.next());
			CellDetails cellDetails = columnToJsonpathMap.get("" + cellNumber);
			if(cellDetails != null) {
				addCellToJsonUsingJsonPath(jsonObject, cellDetails, cellValue);
			}
		}
		
		mapper.writeValue(new File("C:\\dev\\coder-one\\jsons\\"+rowNumber+".json"),jsonObject);
		System.out.println(jsonObject.toString());
	}
	
	@SuppressWarnings("deprecation")
	private void addCellToJsonUsingJsonPath(JsonNode jsonObject, CellDetails cellDetails, String cellValue) {
		String jsonPathForThisCell = cellDetails.getJsonPath();
		String[] pathParts = jsonPathForThisCell.split("\\.");
		String dataType = cellDetails.getDataType();
		JsonNode tempNode = jsonObject;
		for(int i=0; i<pathParts.length - 1; i++) {
			String part = pathParts[i];
			ObjectNode currentNode = (ObjectNode) tempNode.get(part);
			if(currentNode == null) {
				((ObjectNode) tempNode).put(part, mapper.createObjectNode());
				currentNode = (ObjectNode) tempNode.get(part);
			}
			tempNode = currentNode;
		}
		
		if("integer".equalsIgnoreCase(dataType) || "number".equalsIgnoreCase(dataType)) {
			((ObjectNode) tempNode).put(pathParts[pathParts.length-1], Integer.parseInt(cellValue));
		}else if("double".equals(dataType)) {
			((ObjectNode) tempNode).put(pathParts[pathParts.length-1], Double.parseDouble(cellValue));
		}else if("boolean".equalsIgnoreCase(dataType)) {
//			((ObjectNode) tempNode).put(pathParts[pathParts.length-1], Boolean.parseBoolean(cellValue));
			((ObjectNode) tempNode).put(pathParts[pathParts.length-1], "Y".equalsIgnoreCase(cellValue));
		}else {
			((ObjectNode) tempNode).put(pathParts[pathParts.length-1], cellValue);
		}
		
	}
	
	
	private void saveRowToJsonFile(Row row, Map<String, String> columnToJsonpathMap, int rowNumber) throws Exception {
		
		JsonNode jsonObject = mapper.createObjectNode();
//		Map<String, Object> jsonObject = new HashMap<>();
//		JSONObject jsonObject = new JSONObject();
		
		Iterator<Cell> cellIterator = row.cellIterator();
		
		for(int cellNumber=0; cellIterator.hasNext(); cellNumber++) {
			String cellValue = dataFormatter.formatCellValue(cellIterator.next());
			String jsonPathForThisCell = columnToJsonpathMap.get("" + cellNumber);
			if(jsonPathForThisCell != null) {
				addCellToJsonUsingJsonPath(jsonObject, jsonPathForThisCell, cellValue);
			}
		}
		
//		JsonNode jsonNode = mapper.readTree(jsonObject.toString());
		
//		mapper.writeValue(new File("C:\\dev\\coder-one\\jsons\\"+rowNumber+".json"), jsonObject.toString());
		
		mapper.writeValue(new File("C:\\dev\\coder-one\\jsons\\"+rowNumber+".json"),jsonObject);
		System.out.println(jsonObject.toString());
	}
	
//	private void addCellToJsonUsingJsonPath(JSONObject jsonObject, String jsonPathForThisCell, String cellValue) throws JSONException {
//		String[] pathParts = jsonPathForThisCell.split("\\.");
//		JSONObject tempNode = jsonObject;
//		for(int i=0; i<pathParts.length - 1; i++) {
//			String part = pathParts[i];
//			JSONObject currentNode = null;
//			try {
//				currentNode = (JSONObject) tempNode.get(part);
//			}catch (Exception e) {
//				tempNode.put(part, new JSONObject());
//				currentNode = (JSONObject) tempNode.get(part);
//			}
//			tempNode = currentNode;
//		}
//		tempNode.put(pathParts[pathParts.length-1], cellValue);
//		
//	}

	@SuppressWarnings("deprecation")
	private void addCellToJsonUsingJsonPath(JsonNode jsonObject, String jsonPathForThisCell, String cellValue) {
		String[] pathParts = jsonPathForThisCell.split("\\.");
		JsonNode tempNode = jsonObject;
		for(int i=0; i<pathParts.length - 1; i++) {
			String part = pathParts[i];
			ObjectNode currentNode = (ObjectNode) tempNode.get(part);
			if(currentNode == null) {
				((ObjectNode) tempNode).put(part, mapper.createObjectNode());
				currentNode = (ObjectNode) tempNode.get(part);
			}
			tempNode = currentNode;
		}
		((ObjectNode) tempNode).put(pathParts[pathParts.length-1], cellValue);
		
	}
	
//	private void addCellToJsonUsingJsonPath(Map<String, Object> jsonMap, String jsonPathForThisCell, String cellValue) {
//		String[] pathParts = jsonPathForThisCell.split("\\.");
//		String lastpart = "";
//		Map<String, Object> tempNode = jsonMap;
//		for (String part : pathParts) {
//			lastpart = part;
//			Object currentNode = tempNode.get(part);
//			if (currentNode == null) {
//				currentNode = tempNode.put(part, new Object());
//			}
//			currentNode = tempNode;
//		}
//		tempNode.put(lastpart, cellValue);
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
