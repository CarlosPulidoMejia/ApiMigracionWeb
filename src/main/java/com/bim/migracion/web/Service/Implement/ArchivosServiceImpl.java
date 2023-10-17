package com.bim.migracion.web.Service.Implement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bim.migracion.web.Conexion.DataSourceSybase;

import com.bim.migracion.web.Request.ArchivoRequest;
import com.bim.migracion.web.Request.BeneficiarioRequest;
import com.bim.migracion.web.Request.ParametrosMCRequest;
import com.bim.migracion.web.Response.ComparacionCdaResponse;
import com.bim.migracion.web.Response.DetalleErrorResponse;
import com.bim.migracion.web.Response.ValidacionCdaResponse;
import com.bim.migracion.web.Service.ArchivosService;
import com.bim.migracion.web.Service.MatrizCuentasService;

@Service
public class ArchivosServiceImpl implements ArchivosService {

	@Autowired
	private MatrizCuentasService matrizService;

	@Override
	public List<ArchivoRequest> leerArchvivo(File archivo) {
		// TODO Auto-generated method stub
		List<ArchivoRequest> listaArchRequest = new ArrayList<ArchivoRequest>();

		FileReader fr = null;
		BufferedReader br;

		try {
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			String linea = "";
			int numeroLinea = 0;
			while ((linea = br.readLine()) != null) {
				ArchivoRequest archRequest = new ArchivoRequest();
				// System.out.println("Data: " + linea);
				archRequest.setNumeroLinea(numeroLinea);
				archRequest.setDatoLinea(linea);
				listaArchRequest.add(archRequest);
				numeroLinea++;
			}

		} catch (IOException e) {

		} finally {

			try {
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace(System.out);
			}
		}
		return listaArchRequest;

	}

	@Override
	public void crearArchivo(StringBuffer texto, String nombreArchivo) {
		// TODO Auto-generated method stub
		try {
			FileWriter file = new FileWriter("C:\\MigracionWeb\\Certificaciones\\POA\\" + nombreArchivo + ".txt");
			file.write(texto.toString());
			file.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public List<BeneficiarioRequest> leerArchivoExcel(MultipartFile file, ParametrosMCRequest parametros) {
		// TODO Auto-generated method stub
		int clave = 0;
		int i = 0;
		List<BeneficiarioRequest> listaBenRequest = new ArrayList<BeneficiarioRequest>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);
			// DataSourceSybase.setConexion("172.30.29.183", "5551", "dbBIM_SPEI", "DESA",
			// "666666");
			for (int rowIndex = parametros.getInicio1(); rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				BeneficiarioRequest beneficiarioRequest = new BeneficiarioRequest();
				// for(int cellIndex =
				// 1;cellIndex<sheet.getRow(rowIndex).getLastCellNum();cellIndex++) {
				System.out.println("--XXX-" + rowIndex);
				clave = (int) sheet.getRow(rowIndex).getCell(parametros.getClabeBancos()).getNumericCellValue();

				// clave = sheet.getRow(rowIndex).getCell(cellIndex).getStringCellValue();

				// nuevaClave = Integer.parseInt(clave);

				i = matrizService.searchInstit(clave);
				if (i == 1) {
					// System.out.println("banco: " + clave);
					beneficiarioRequest.setBanco("" + clave + "");
					listaBenRequest.add(beneficiarioRequest);
				}
			}

			// System.out.println("fn");
			// }
			DataSourceSybase.cerrarConexion();

			/*
			 * for(BeneficiarioRequest ben: listaBenRequest) { //System.out.println("bASNCO"
			 * + ben.getBanco()); }
			 */

			listaBenRequest = leerArchivoExcel2(file, listaBenRequest, parametros);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaBenRequest;
	}

	@SuppressWarnings("unused")
	@Override
	public List<BeneficiarioRequest> leerArchivoExcel2(MultipartFile file, List<BeneficiarioRequest> listaBen,
			ParametrosMCRequest parametros) {
		// TODO Auto-generated method stub
		XSSFWorkbook workbook;
		List<BeneficiarioRequest> listaBenef = new ArrayList<BeneficiarioRequest>();
		Map<Integer, BeneficiarioRequest> mapBen = new HashedMap<Integer, BeneficiarioRequest>();
		// Map<K, V>
		try {
			workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(1);

			int i = 0;
			for (BeneficiarioRequest ben : listaBen) {
				// System.out.println("Banco: " + ben.getBanco());
				BeneficiarioRequest beneficiario = ben;
				for (int rowIndex = parametros.getInicio2(); rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					String status = "";
					String cuentaCliente = "";
					String nombreCliente = "";
					String rfcCliente = "";
					// System.out.println("rowIndex : " +rowIndex + "-" + sheet.getLastRowNum());
					int clave = (int) sheet.getRow(rowIndex).getCell(parametros.getClabeBanco()).getNumericCellValue();

					String nuevaClave = String.valueOf(clave);
					// System.out.println(nuevaClave);
					// System.out.println("Banco: " + ben.getBanco() + "-" + nuevaClave);

					status = sheet.getRow(rowIndex).getCell(parametros.getStatusBanco()).getStringCellValue();
					if (ben.getBanco().equals(nuevaClave) && status.equals("Válida")
							|| ben.getBanco().equals(nuevaClave) && status.equals("VÁLIDA")) {
						// if(status.equals("Válida")) {

						cuentaCliente = sheet.getRow(rowIndex).getCell(parametros.getCuentaBanco()).getStringCellValue()
								.trim();
						cuentaCliente = cuentaCliente.replaceAll("^\\s*", "");
						nombreCliente = "Cliente " + nuevaClave;
						rfcCliente = "XXXX0000009K1";
						// System.out.println("Cuenta valida " + nuevaClave + "-" + cuentaCliente);
						ben.setBanco(nuevaClave);
						ben.setCuenta(cuentaCliente);
						ben.setNombre(nombreCliente);
						ben.setRfc(rfcCliente);
						ben.setStatusBen("CV");
						listaBenef.add(ben);
						mapBen.put(i, ben);
						// System.out.println("---" + cuentaCliente);
						// System.out.println("bSNCO VALIDO: " + nuevaClave + "- " + status +
						// cuentaCliente);
						i++;
						break;
					}

				}

			}

			int ii = i;
			ii++;
			for (BeneficiarioRequest ben : listaBen) {
				BeneficiarioRequest bens = new BeneficiarioRequest();
				for (int rowIndex = parametros.getInicio2(); rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					String status = "";
					String cuentaCliente = "";
					String nombreCliente = "";
					String rfcCliente = "";
					// ystem.out.println("rowIndex : " +rowIndex + "-" + sheet.getLastRowNum());
					int clave = (int) sheet.getRow(rowIndex).getCell(parametros.getClabeBanco()).getNumericCellValue();

					String nuevaClave = String.valueOf(clave);

					status = sheet.getRow(rowIndex).getCell(parametros.getStatusBanco()).getStringCellValue();
					// System.out.println("Row: "+ rowIndex + "-" + status);
					if (ben.getBanco().equals(nuevaClave) && status.equals("Inválida")
							|| ben.getBanco().equals(nuevaClave) && status.equals("INVÁLIDA")) {// INVÁLIDA
						// if(status.equals("Inválida")) {
						// System.out.println("Linea leida " + rowIndex);
						cuentaCliente = sheet.getRow(rowIndex).getCell(parametros.getCuentaBanco()).getStringCellValue()
								.trim();
						cuentaCliente = cuentaCliente.replaceAll("^\\s*", "");
						nombreCliente = "Cliente " + nuevaClave;
						rfcCliente = "XXXX0000009K1";
						if (cuentaCliente.length() == 18) {
							bens.setBanco(nuevaClave);
							bens.setCuenta(cuentaCliente);
							bens.setNombre(nombreCliente);
							bens.setRfc(rfcCliente);
							bens.setStatusBen("CI");
							listaBenef.add(bens);
							// System.out.println("bSNCO INVALIDO: " + nuevaClave + "- " + status +
							// cuentaCliente);
							mapBen.put(ii, bens);
							ii++;
							break;
						} else {
							continue;
						}

						// }
					}
				}
			}

			for (BeneficiarioRequest benfi : listaBenef) {
				// System.out.println("Banco: " + benfi.getBanco() + "-" + benfi.getCuenta() +
				// "-" + benfi.getNombre() + "-" + benfi.getRfc());
			}

			Map<Integer, BeneficiarioRequest> mapBens = new TreeMap<>(mapBen);

			for (Entry<Integer, BeneficiarioRequest> entry : mapBens.entrySet()) {
				// System.out.println("test " + entry.getKey() + "-" +
				// entry.getValue().getBanco() + "-" + entry.getValue().getCuenta() + "-" +
				// entry.getValue().getNombre() + "-" + entry.getValue().getRfc());

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaBenef;
	}

	@Override
	public Boolean generarExcel(ValidacionCdaResponse validacionCda, String nombreArchivo) {
		// TODO Auto-generated method stub

		XSSFWorkbook libroExcel = new XSSFWorkbook();
		libroExcel.createSheet("Datos");

		Sheet hojaExcel = libroExcel.getSheet("Datos");

		Boolean validar = false;

		Row fila = hojaExcel.createRow(0);
		for (Map.Entry<Integer, String> header : validacionCda.getMapHeader().entrySet()) {

			if (header.getKey() > 0) {
				Cell cell = fila.createCell(header.getKey() - 1);

				cell.setCellValue(header.getValue());
			}

		}

		Row info = hojaExcel.createRow(1);
		for (Map.Entry<Integer, String> header : validacionCda.getInformacion().entrySet()) {

			if (header.getKey() > 0) {
				Cell cell = info.createCell(header.getKey() - 1);

				cell.setCellValue(header.getValue());
			}

		}

		FileOutputStream outputStream;

		try {
			System.out.println("eXPORTANDO EXCEL");
			outputStream = new FileOutputStream(nombreArchivo);
			libroExcel.write(outputStream);
			libroExcel.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return validar;

	}

	@Override
	public void generarCsv(Map<Integer, String> mapaCda) {
		// TODO Auto-generated method stub
		String nombreArchivo = "Test1.csv";
		String ruta = "C:\\MigracionWeb\\Validaciones\\Excel\\" + nombreArchivo;

		File file = new File(ruta);

		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter writer = new BufferedWriter(fw);

			for (Map.Entry<Integer, String> map : mapaCda.entrySet()) {
				//System.out.println("Valor: " + map.getValue());
				writer.write(map.getValue());
				writer.newLine();
			}
			writer.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void generarExcelErrores(List<DetalleErrorResponse> listaDetalleError) {
		// TODO Auto-generated method stub
		XSSFWorkbook libroExcel = new XSSFWorkbook();
		libroExcel.createSheet("Incidencias");

		Sheet hojaExcel = libroExcel.getSheet("Incidencias");

		// Row fila = hojaExcel.createRow(0);

		Row headerRow = hojaExcel.createRow(0);

		Cell headerCell = headerRow.createCell(0);
		headerCell.setCellValue("Linea:");

		headerCell = headerRow.createCell(1);
		headerCell.setCellValue("Error en Campo");

		headerCell = headerRow.createCell(2);
		headerCell.setCellValue("Error");

		headerCell = headerRow.createCell(3);
		headerCell.setCellValue("Tipo - Long Maxima");
		/*
		 * headerCell = headerRow.createCell(4); headerCell.setCellValue("Comentario");
		 */

		int rowCount = 1;
		for (int i = 0; i < listaDetalleError.size(); i++) {
			int columnCount = 0;

			Row row = hojaExcel.createRow(rowCount++);

			Cell cell = row.createCell(columnCount++);
			cell.setCellValue(listaDetalleError.get(i).getNumeroLinea());

			cell = row.createCell(columnCount++);
			cell.setCellValue(listaDetalleError.get(i).getCampoError());

			cell = row.createCell(columnCount++);
			cell.setCellValue(listaDetalleError.get(i).getDetalleError());

			cell = row.createCell(columnCount++);
			cell.setCellValue(listaDetalleError.get(i).getSolucion());

		}

		FileOutputStream outputStream;

		try {
			System.out.println("eXPORTANDO EXCEL");
			outputStream = new FileOutputStream("C:\\MigracionWeb\\Cdas\\incidencias\\errores.xlsx");
			libroExcel.write(outputStream);
			libroExcel.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void generarExcelComparacion(List<ComparacionCdaResponse> listaComparacion) {
		// TODO Auto-generated method stub
		XSSFWorkbook libroExcel = new XSSFWorkbook();
		libroExcel.createSheet("Incidencias");

		Sheet hojaExcel = libroExcel.getSheet("Incidencias");

		// Row fila = hojaExcel.createRow(0);

		Row headerRow = hojaExcel.createRow(0);

		Cell headerCell = headerRow.createCell(0);
		headerCell.setCellValue("ClaRas:");

		headerCell = headerRow.createCell(1);
		headerCell.setCellValue("Dato Sybase");

		headerCell = headerRow.createCell(2);
		headerCell.setCellValue("Dato MySQL");

		/*headerCell = headerRow.createCell(3);
		headerCell.setCellValue("Tipo - Long Maxima");*/
		/*
		 * headerCell = headerRow.createCell(4); headerCell.setCellValue("Comentario");
		 */

		int rowCount = 1;
		for (int i = 0; i < listaComparacion.size(); i++) {
			int columnCount = 0;

			Row row = hojaExcel.createRow(rowCount++);

			Cell cell = row.createCell(columnCount++);
			cell.setCellValue(listaComparacion.get(i).getClaRas());

			cell = row.createCell(columnCount++);
			cell.setCellValue(listaComparacion.get(i).getCampoSybase());

			cell = row.createCell(columnCount++);
			cell.setCellValue(listaComparacion.get(i).getCampoMysql());

			/*cell = row.createCell(columnCount++);
			cell.setCellValue(listaComparacion.get(i).getSolucion());*/

		}

		FileOutputStream outputStream;

		try {
			System.out.println("eXPORTANDO EXCEL Comparacion");
			outputStream = new FileOutputStream("C:\\MigracionWeb\\Cdas\\incidencias\\Comparacion.xlsx");
			libroExcel.write(outputStream);
			libroExcel.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
