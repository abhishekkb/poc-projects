package com.toyota.excel2json.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.toyota.excel2json.view.model.MultipleFiles;

public interface IConvertToJsonService {

	void convertToJson(MultipleFiles files) throws Exception;

	void convertToJson(Workbook excelWorkbook, Workbook schemaWorkbook) throws Exception;

	void convertToJson(Sheet excelSheet, Sheet schemaSheet) throws Exception;

	void convertToJson2(Sheet excelSheet, Sheet schemaSheet) throws Exception;
}
