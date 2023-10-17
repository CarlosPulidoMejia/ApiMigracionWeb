package com.bim.migracion.web.Service.Implement;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.bim.migracion.web.Controller.EnvioOrdenesController;
import com.bim.migracion.web.Entity.ReportePoaExcel;
import com.bim.migracion.web.Logs.GuardaLogs;
import com.bim.migracion.web.Logs.TipoLog;
import com.bim.migracion.web.Service.ExcelService;

@Service
public class ExcelServiceImpl implements ExcelService {

	@Override
	public void exportExcelPoa(String nameFile, ArrayList<ReportePoaExcel> listRepotePoaDTO,String tipoContingencia) {
		// TODO Auto-generated method stub
		GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Creando excel", null);
		//String rutaExcel = "C:\\MigracionWeb\\Certificaciones\\" + tipoContingencia + "\\validaciones\\"+nameFile+".xlsx";
		String rutaExcel = "C:\\MigracionWeb\\Certificaciones\\POA\\validaciones\\"+nameFile+".xlsx";
		//System.out.println(rutaExcel);

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Detalle");
		//sheet.setColumnWidth(3, 25 * 256);

		headerExcel(sheet);
		informacionExcel(listRepotePoaDTO, workbook, sheet);
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(rutaExcel);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error excel", e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			GuardaLogs.registrarInfo(EnvioOrdenesController.class, TipoLog.INFO, "Ocurrio un error excel", e);
			e.printStackTrace();
		}
	}

	@Override
	public void headerExcel(XSSFSheet sheet2) {
		// TODO Auto-generated method stub
		Row headerRow = sheet2.createRow(0);

		Cell headerCell = headerRow.createCell(0);
		headerCell.setCellValue("Linea:");

		headerCell = headerRow.createCell(1);
		headerCell.setCellValue("Error en Campo");

		headerCell = headerRow.createCell(2);
		headerCell.setCellValue("Incidencia");

		headerCell = headerRow.createCell(3);
		headerCell.setCellValue("Solucion");

		headerCell = headerRow.createCell(4);
		headerCell.setCellValue("Comentario");
	}

	@Override
	public void informacionExcel(ArrayList<ReportePoaExcel> listRepotePoaDTO, XSSFWorkbook workbook2,
			XSSFSheet sheet2) {
		// TODO Auto-generated method stub
		int rowCount = 1;

		  for (int i = 0; i < listRepotePoaDTO.size(); i++) {
		  //System.out.println(listRepotePoaDTO.get(i).getDetalleError()); 
		  int columnCount = 0; 
		  Row row = sheet2.createRow(rowCount++);
		  
		  
		  Cell cell = row.createCell(columnCount++);
		  cell.setCellValue(listRepotePoaDTO.get(i).getLinea());
		  
		  cell = row.createCell(columnCount++);
		  cell.setCellValue(listRepotePoaDTO.get(i).getDetalleError());
		  
		  
		  cell = row.createCell(columnCount++);
		  cell.setCellValue(listRepotePoaDTO.get(i).getDetalleSolucion());
		  
		  cell = row.createCell(columnCount++);
		  cell.setCellValue(listRepotePoaDTO.get(i).getInstitucion());
		  
		 
		  }
	}
	
	

}
