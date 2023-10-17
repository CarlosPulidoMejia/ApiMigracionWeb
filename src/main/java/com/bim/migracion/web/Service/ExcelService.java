package com.bim.migracion.web.Service;

import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bim.migracion.web.Entity.ReportePoaExcel;

public interface ExcelService {

	public void exportExcelPoa(String nameFile, ArrayList<ReportePoaExcel> listRepotePoaDTO,String tipoContingencia);
	
	public void headerExcel(XSSFSheet sheet2);
	
	public void informacionExcel(ArrayList<ReportePoaExcel> listRepotePoaDTO, XSSFWorkbook workbook2, XSSFSheet sheet2);
}
