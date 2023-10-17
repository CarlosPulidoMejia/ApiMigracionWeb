package com.bim.migracion.web.Service.Implement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Propiedades.PropiedadesSybase;
import com.bim.migracion.web.Request.FilesRequest;
import com.bim.migracion.web.Service.SibamexService;
import com.bim.migracion.web.SibamexStoreVAPA.ActualizarRegistrosVAPA;



@Service
public class SibamexServiceImpl implements SibamexService {
	
	@Autowired
	PropiedadesSybase propiedades;
	
	@Autowired
	ActualizarRegistrosVAPA actualizacionRegistros;

	@Override
	public void ejecutarStore(String tipArch, FilesRequest FilesRequest) {

		String Acp_Numero = "''";
		String Tar_Numero = tipArch;
		int Cop_TipReg = 0;
		int Cop_NuSeAr = 0;
		int Cop_NumOpe = 0; 
		String Tip_EncInf = "I";
		String EspacioComa = "','";
		String NumTransac = "''";
		String Transaccio = "COP";
		String Usuario = "000662";
		LocalDateTime now = LocalDateTime.now();
	    String FechaSis = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
		String SucOrigen = "001";
		String SucDestino = "001";
		String Modulo = "SP";
		
		//DataSourceSybase.setConexion("172.30.29.183","5551","dbBIM_SPEI","DESA","666666");
		//DataSourceSybase.getConnection();
		String[] datosxlinea;
		
		Iterator<Integer> iterador = FilesRequest.getFileReq().keySet().iterator();
		FilesRequest DatosReq = new FilesRequest();
		while(iterador.hasNext()) {
			String query = "";
			Integer key = iterador.next();
			datosxlinea = FilesRequest.getFileReq().get(key).split("~");		
			if (key == 0) {
				for (int i = 0; i < datosxlinea.length; i++) {
					if(i == 1) {
						DatosReq.setCop_ClPaER(datosxlinea[i]);
					}
				}
			}
			else {
				query += "call SPCOAPOAPRO "; 
				for (int i = 0; i < datosxlinea.length; i++) {
					if (i == 0)
					{ DatosReq.setCop_FolPaq(datosxlinea[i]); }
					if (i == 1)
					{ DatosReq.setCop_TipPag("0"+datosxlinea[i]); }
					if (DatosReq.getCop_TipPag().equals("01") || DatosReq.getCop_TipPag().equals("02") || DatosReq.getCop_TipPag().equals("03") || DatosReq.getCop_TipPag().equals("04") || DatosReq.getCop_TipPag().equals("05") || DatosReq.getCop_TipPag().equals("06") || DatosReq.getCop_TipPag().equals("07")) {
						if (i == 2)
						{ DatosReq.setCop_ClPaRE(datosxlinea[i]); }
						if (i == 3)
						{ DatosReq.setCop_MonPag(datosxlinea[i]); }
						if (i == 4)
						{ DatosReq.setCop_FolPag(datosxlinea[i]); }
						if (i == 5)
						{ DatosReq.setCop_ClaRas(datosxlinea[i]); }
						if (i == 6)
						{ 	if(DatosReq.getCop_TipPag().equals("05") || DatosReq.getCop_TipPag().equals("06"))
							{DatosReq.setCop_NomBen(datosxlinea[i]);}
							else if(DatosReq.getCop_TipPag().equals("07"))
							{DatosReq.setCop_TipOpe(datosxlinea[i]);}
							else
							{DatosReq.setCop_NomOrd(datosxlinea[i]);}
						}
						if (i == 7)
						{
							if(DatosReq.getCop_TipPag().equals("05") || DatosReq.getCop_TipPag().equals("06"))
							{DatosReq.setCop_TiCuBe(datosxlinea[i]);}
							else if(DatosReq.getCop_TipPag().equals("07"))
							{DatosReq.setCop_ConPag(datosxlinea[i]);}
							else
							{DatosReq.setCop_TiCuOr(datosxlinea[i]);} 
						}
						if (i == 8)
						{
							if(DatosReq.getCop_TipPag().equals("05") || DatosReq.getCop_TipPag().equals("06"))
							{DatosReq.setCop_CueBen(datosxlinea[i]);}
							else if(DatosReq.getCop_TipPag().equals("07"))
							{DatosReq.setCop_ImpIVA(datosxlinea[i]);}
							else
							{DatosReq.setCop_CueOrd(datosxlinea[i]);} 
						}
						if (i == 9)
						{ 
							if(DatosReq.getCop_TipPag().equals("05"))
							{DatosReq.setCop_RFCUBe(datosxlinea[i]);}
							else if(DatosReq.getCop_TipPag().equals("06"))
							{DatosReq.setCop_NoBen2(datosxlinea[i]);}
							else if(DatosReq.getCop_TipPag().equals("07"))
							{DatosReq.setCop_RefNum(datosxlinea[i]);}
							else
							{DatosReq.setCop_RFCUOr(datosxlinea[i]);} 
						}
						if (i == 10)
						{ 
							if(DatosReq.getCop_TipPag().equals("05"))
							{DatosReq.setCop_ConPag(datosxlinea[i]);}
							else if(DatosReq.getCop_TipPag().equals("06"))
							{DatosReq.setCop_RFCUB2(datosxlinea[i]);;	}
							else
							{DatosReq.setCop_NomBen(datosxlinea[i]);} 
						}
					}
					if (DatosReq.getCop_TipPag().equals("01")) {
						if (i == 11)
						{ DatosReq.setCop_TiCuBe(datosxlinea[i]); }
						if (i == 12)
						{ DatosReq.setCop_CueBen(datosxlinea[i]); }
						if (i == 13)
						{ DatosReq.setCop_RFCUBe(datosxlinea[i]); }
						if (i == 14)
						{ DatosReq.setCop_ConPag(datosxlinea[i]); }
						if (i == 15)
						{ DatosReq.setCop_ImpIVA(datosxlinea[i]); }
						if (i == 16)
						{ DatosReq.setCop_RefNum(datosxlinea[i]); }
					}
					if (DatosReq.getCop_TipPag().equals("02")) {
						if(i == 11) 
						{ DatosReq.setCop_ConPag(datosxlinea[i]); }
						if(i == 12)
						{ DatosReq.setCop_RefNum(datosxlinea[i]); }
						if(i == 13)
						{ DatosReq.setCop_ImpIVA(datosxlinea[i]); }
					}
					if (DatosReq.getCop_TipPag().equals("03")) {
						if (i == 11)
						{ DatosReq.setCop_TiCuBe(datosxlinea[i]); }
						if (i == 12)
						{ DatosReq.setCop_CueBen(datosxlinea[i]); }
						if(i == 17 ) 				
						{ DatosReq.setCop_ConPag(datosxlinea[i]); 
						  DatosReq.setCop_CoPaDo(datosxlinea[i]);
						}
						if(i == 19 )
						{ DatosReq.setCop_ImpIVA(datosxlinea[i]); }
						if(i == 20 )
						{ DatosReq.setCop_RefNum(datosxlinea[i]); }
					}
					if (DatosReq.getCop_TipPag().equals("04")) {
						DatosReq.setCop_NomBen("");
						if(i == 10)
						{ DatosReq.setCop_TiCuBe(datosxlinea[i]); }
						if(i == 11)
						{ DatosReq.setCop_ConPag(datosxlinea[i]); }
						if(i == 12)
						{ DatosReq.setCop_ImpIVA(datosxlinea[i]); }
						if(i == 13)
						{ DatosReq.setCop_RefNum(datosxlinea[i]);}
					}
					if (DatosReq.getCop_TipPag().equals("05")) {
						if(i == 11)
						{ DatosReq.setCop_ImpIVA(datosxlinea[i]); }
						if(i == 12)
						{ DatosReq.setCop_RefNum(datosxlinea[i]); }
					}
					if(DatosReq.getCop_TipPag().equals("06")) {
						if(i == 11)
						{ DatosReq.setCop_TiCuB2(datosxlinea[i]); }
						if(i == 12)
						{ DatosReq.setCop_CuBeDo(datosxlinea[i]); }
						if(i == 13)
						{ DatosReq.setCop_ConPag(datosxlinea[i]); }
						if(i == 14)
						{ DatosReq.setCop_CoPaDo(datosxlinea[i]); }
						if(i == 15)
						{ DatosReq.setCop_ImpIVA(datosxlinea[i]); }
						if(i == 16)
						{ DatosReq.setCop_RefNum(datosxlinea[i]); }
					}
				}
				if (DatosReq.getCop_TipPag() == "04") { DatosReq.setCop_NomBen("");} 
				if (DatosReq.getCop_TipPag().equals("01") || DatosReq.getCop_TipPag().equals("02") || DatosReq.getCop_TipPag().equals("03") || DatosReq.getCop_TipPag().equals("04")) {
					query += Acp_Numero + "," + Tar_Numero + "," + Cop_TipReg + ",'" 
							+ DatosReq.getCop_ClPaER() + "'," + Cop_NuSeAr + "," 
							+ Cop_NumOpe + ",'" + Tip_EncInf + "'," + DatosReq.getCop_FolPaq() + ",'" 
							+ DatosReq.getCop_TipPag() + EspacioComa + DatosReq.getCop_ClPaRE() + "'," + DatosReq.getCop_MonPag() + ","
							+ DatosReq.getCop_FolPag() + ",'" + DatosReq.getCop_ClaRas() + EspacioComa + DatosReq.getCop_NomOrd() + EspacioComa
							+ DatosReq.getCop_TiCuOr() + EspacioComa + DatosReq.getCop_CueOrd() + EspacioComa + DatosReq.getCop_RFCUOr() + EspacioComa
							+ DatosReq.getCop_NomBen();	
				}
				if (DatosReq.getCop_TipPag().equals("01")) {
					query += EspacioComa + DatosReq.getCop_TiCuBe() + EspacioComa + DatosReq.getCop_CueBen() + EspacioComa
							+ DatosReq.getCop_RFCUBe() + EspacioComa + DatosReq.getCop_ConPag() + "'," + DatosReq.getCop_ImpIVA() + ",'" 
							+ DatosReq.getCop_RefNum() + "'," + DatosReq.getCop_ReCoUn() + "," + DatosReq.getCop_ClaPag() + ","
							+ DatosReq.getCop_NoBen2() +  "," + DatosReq.getCop_RFCUB2() +  "," + DatosReq.getCop_TiCuB2() +  ","
							+ DatosReq.getCop_CuBeDo() +  "," + DatosReq.getCop_CoPaDo() +  "," + DatosReq.getCop_TipOpe() +  ","
							+ DatosReq.getCop_CauDev() +  "," + DatosReq.getCop_FoInOr() + "," + DatosReq.getCop_FoPaOr() + "," 
							+ DatosReq.getCop_FeTrOr() +  "," + DatosReq.getCop_ClRaOr() +  "," + DatosReq.getCop_CoPaUn() + ","
							+ DatosReq.getCop_MoPaOr() + "," + DatosReq.getCop_InfFac() + ", " + NumTransac + ",'" + Transaccio + "','" + Usuario + "','"
							+ FechaSis + "','" + SucOrigen + "','" + SucDestino + "','" + Modulo + "'";
				}
				if (DatosReq.getCop_TipPag().equals("02")) {
					query += "'," + DatosReq.getCop_TiCuBe() + "," + DatosReq.getCop_CueBen() + "," + DatosReq.getCop_RFCUBe() + "," + DatosReq.getCop_CoPaUn() + "," 
							+ DatosReq.getCop_ImpIVA() + ","+ DatosReq.getCop_ClaPag() + "," + DatosReq.getCop_ReCoUn() + ",'"  
							+ DatosReq.getCop_RefNum() + "',"  + DatosReq.getCop_NoBen2() +  "," + DatosReq.getCop_RFCUB2() +  "," 
							+ DatosReq.getCop_TiCuB2() +  "," + DatosReq.getCop_CuBeDo() +  "," + DatosReq.getCop_CoPaDo() +  "," 
							+ DatosReq.getCop_TipOpe() +  "," + DatosReq.getCop_CauDev() +  "," + DatosReq.getCop_FoInOr() + "," 
							+ DatosReq.getCop_FoPaOr() + "," + DatosReq.getCop_FeTrOr() +  "," + DatosReq.getCop_ClRaOr() +  ",'" 
							+ DatosReq.getCop_ConPag() + "'," + DatosReq.getCop_MoPaOr() + "," + DatosReq.getCop_InfFac() + "," 
							+ NumTransac + ",'" + Transaccio + "','" + Usuario + "','" + FechaSis + "','" + SucOrigen + "','" + SucDestino + "','" + Modulo + "'";
				}
				if (DatosReq.getCop_TipPag().equals("03")) {
					query += EspacioComa + DatosReq.getCop_TiCuBe() + EspacioComa + DatosReq.getCop_CueBen() + "','" + DatosReq.getCop_ConPag() + "',"
							+ DatosReq.getCop_RFCUBe() + "," + DatosReq.getCop_ImpIVA() + ",'" + DatosReq.getCop_RefNum() + "',"  
							+ DatosReq.getCop_ReCoUn() + "," + DatosReq.getCop_ClaPag() +  "," + DatosReq.getCop_NoBen2() +  ","  
							+ DatosReq.getCop_RFCUB2() +  ","	+ DatosReq.getCop_TiCuB2() +  "," + DatosReq.getCop_CuBeDo() +  ",'" 
							+ DatosReq.getCop_CoPaDo() +  "'," + DatosReq.getCop_CauDev() +  "," + DatosReq.getCop_FoInOr() + ","
							+ DatosReq.getCop_FoInOr() + "," + DatosReq.getCop_FoPaOr() + "," + DatosReq.getCop_FeTrOr() +  "," 
							+ DatosReq.getCop_ClRaOr() +  ",'" + DatosReq.getCop_ConPag() + "'," + DatosReq.getCop_MoPaOr() + "," + DatosReq.getCop_InfFac() 
							+ "," + NumTransac + ",'" + Transaccio + "','" + Usuario + "','" + FechaSis + "','" + SucOrigen + "','" + SucDestino + "','" + Modulo + "'";
				}
				if (DatosReq.getCop_TipPag().equals("04")) {
					query += EspacioComa + "',"+ DatosReq.getCop_RFCUBe() + "," + DatosReq.getCop_ReCoUn() + "," + DatosReq.getCop_ClaPag() + ","  
							+ DatosReq.getCop_ImpIVA() + ",'" + DatosReq.getCop_RefNum() + "'," + DatosReq.getCop_NoBen2() + "," + DatosReq.getCop_RFCUB2() + ","
							+ DatosReq.getCop_TiCuB2() + "," + DatosReq.getCop_CuBeDo() +  "," + DatosReq.getCop_CoPaDo() + "," + DatosReq.getCop_TipOpe() + ","
							+ DatosReq.getCop_CauDev() + ",'" + DatosReq.getCop_TiCuBe() + "'," + DatosReq.getCop_FoInOr() + "," + DatosReq.getCop_FeTrOr() +  ","
							+ DatosReq.getCop_FoPaOr() + "," + DatosReq.getCop_ClRaOr() +  "," +  DatosReq.getCop_CoPaUn() + ",'" + DatosReq.getCop_ConPag() + "',"
							+ DatosReq.getCop_MoPaOr() + "," + DatosReq.getCop_InfFac() + "," + NumTransac + ",'" + Transaccio + "','" + Usuario + "','"
							+ FechaSis + "','" + SucOrigen + "','" + SucDestino + "','" + Modulo + "'";
				}
				if(DatosReq.getCop_TipPag().equals("05")) {
					query +=  Acp_Numero + "," + Tar_Numero + "," + Cop_TipReg + ",'" 
							+ DatosReq.getCop_ClPaER() + "'," + Cop_NuSeAr + "," + Cop_NumOpe + ",'" + Tip_EncInf + "'," + DatosReq.getCop_FolPaq() + ",'" 
							+ DatosReq.getCop_TipPag() + EspacioComa + DatosReq.getCop_ClPaRE() + "'," + DatosReq.getCop_MonPag() + "," + DatosReq.getCop_FolPag() + ",'" 
						    + DatosReq.getCop_ClaRas() + "'," + DatosReq.getCop_NomOrd() + "," + DatosReq.getCop_TiCuOr() + "," + DatosReq.getCop_CueOrd() + ","
						    + DatosReq.getCop_RFCUOr() + ",'" + DatosReq.getCop_NomBen() + EspacioComa + DatosReq.getCop_TiCuBe() + EspacioComa + DatosReq.getCop_CueBen() + EspacioComa + DatosReq.getCop_RFCUBe() + "',"
						    + DatosReq.getCop_ReCoUn() + "," + DatosReq.getCop_ImpIVA() + ",'" + DatosReq.getCop_RefNum() +"'," + DatosReq.getCop_ReCoUn() + ","
						    + DatosReq.getCop_ClaPag() + "," + DatosReq.getCop_NoBen2() + "," + DatosReq.getCop_RFCUB2() + "," + DatosReq.getCop_TiCuB2() + ","
						    + DatosReq.getCop_CuBeDo() + "," + DatosReq.getCop_CoPaDo() + "," + DatosReq.getCop_TipOpe() + "," + DatosReq.getCop_CauDev() + ","
						    + DatosReq.getCop_FoInOr() + "," + DatosReq.getCop_FoPaOr() + "," + DatosReq.getCop_FeTrOr() + "," + DatosReq.getCop_ClRaOr() + ",'"
						    + DatosReq.getCop_ConPag() + "'," + DatosReq.getCop_MoPaOr() + "," + DatosReq.getCop_CoPaUn() + ","
						    + NumTransac + ",'" + Transaccio + "','" + Usuario + "','" + FechaSis + "','" + SucOrigen + "','" + SucDestino + "','" + Modulo + "'"
						    ;
				}
				if(DatosReq.getCop_TipPag().equals("06")) {
					query +=  Acp_Numero + "," + Tar_Numero + "," + Cop_TipReg + ",'" + DatosReq.getCop_ClPaER() + "'," + Cop_NuSeAr + "," + Cop_NumOpe + ",'" + Tip_EncInf + "'," 
							+ DatosReq.getCop_FolPaq() + ",'" + DatosReq.getCop_TipPag() + EspacioComa + DatosReq.getCop_ClPaRE() + "'," + DatosReq.getCop_MonPag() + ","
							+ DatosReq.getCop_FolPag() + ",'" + DatosReq.getCop_ClaRas() + "'," + DatosReq.getCop_NomOrd() + "," + DatosReq.getCop_TiCuOr() + "," + DatosReq.getCop_CueOrd() + ","
							+ DatosReq.getCop_RFCUOr() + ",'" + DatosReq.getCop_NomBen() + EspacioComa + DatosReq.getCop_TiCuBe() + EspacioComa + DatosReq.getCop_CueBen() + "',"  + DatosReq.getCop_RFCUBe() + ","
							+ DatosReq.getCop_ReCoUn() + "," + DatosReq.getCop_ImpIVA() + ",'" + DatosReq.getCop_RefNum() +"'," + DatosReq.getCop_ClaPag() + ","
							+ DatosReq.getCop_TipOpe() + ",'" + DatosReq.getCop_NoBen2() + EspacioComa + DatosReq.getCop_RFCUB2() + EspacioComa + DatosReq.getCop_TiCuB2() + EspacioComa
							+ DatosReq.getCop_CuBeDo() + EspacioComa + DatosReq.getCop_CoPaDo() + "'," + DatosReq.getCop_TipOpe() + "," + DatosReq.getCop_CauDev() + ","
							+ DatosReq.getCop_FoInOr() + "," + DatosReq.getCop_FoPaOr() + "," + DatosReq.getCop_FeTrOr() + "," + DatosReq.getCop_ClRaOr() + ",'"
							+ DatosReq.getCop_ConPag() + "'," + DatosReq.getCop_MoPaOr() + "," + DatosReq.getCop_CoPaUn() + ","
							+ NumTransac + ",'" + Transaccio + "','" + Usuario + "','" + FechaSis + "','" + SucOrigen + "','" + SucDestino + "','" + Modulo + "'";
				}
				if(DatosReq.getCop_TipPag().equals("07")) {
					query +=  Acp_Numero + "," + Tar_Numero + "," + Cop_TipReg + ",'" 
							+ DatosReq.getCop_ClPaER() + "'," + Cop_NuSeAr + "," + Cop_NumOpe + ",'" + Tip_EncInf + "'," + DatosReq.getCop_FolPaq() + ",'" 
							+ DatosReq.getCop_TipPag() + EspacioComa + DatosReq.getCop_ClPaRE() + "'," + DatosReq.getCop_MonPag() + "," + DatosReq.getCop_FolPag() + ",'" 
						    + DatosReq.getCop_ClaRas() + "'," + DatosReq.getCop_NomOrd() + "," + DatosReq.getCop_TiCuOr() + "," + DatosReq.getCop_CueOrd() + ","
						    + DatosReq.getCop_RFCUOr() + "," + DatosReq.getCop_RFCUOr() + "," + DatosReq.getCop_NomBen() + "," + DatosReq.getCop_TiCuBe() + ","
						    + DatosReq.getCop_CueBen() + "," + DatosReq.getCop_RFCUBe() + "," + DatosReq.getCop_ImpIVA() + ",'" + DatosReq.getCop_RefNum() + "',"
						    + DatosReq.getCop_ReCoUn() + "," + DatosReq.getCop_ClaPag() + "," + DatosReq.getCop_NoBen2() + "," + DatosReq.getCop_RFCUB2() + ","
						    + DatosReq.getCop_TiCuB2() + "," + DatosReq.getCop_CuBeDo() + "," + DatosReq.getCop_CoPaDo() + ",'" + DatosReq.getCop_TipOpe() + "',"
						    + DatosReq.getCop_CauDev() + "," + DatosReq.getCop_FoInOr() + "," + DatosReq.getCop_FoPaOr() + "," + DatosReq.getCop_FeTrOr() + ","
						    + DatosReq.getCop_ClRaOr() + ",'" + DatosReq.getCop_ConPag() + "'," + DatosReq.getCop_MoPaOr() + "," + DatosReq.getCop_CoPaUn() + ","
						    + NumTransac + ",'" + Transaccio + "','" + Usuario + "','" + FechaSis + "','" + SucOrigen + "','" + SucDestino + "','" + Modulo + "'";
				}
				//System.out.println(query);
				propiedades.ejecutaStore(query);
			}			
		}
		if (Transaccio == "COP")
			Transaccio = "''";
		actualizacionRegistros.execVaPa(NumTransac, Transaccio, Usuario, FechaSis, SucOrigen, SucDestino, Modulo);
		//DataSourceSybase.cerrarConexion();
	}
}
