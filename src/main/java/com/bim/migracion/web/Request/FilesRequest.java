package com.bim.migracion.web.Request;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class FilesRequest {
	
	private Map<Integer, String> fileReq;
	

	public String Cop_ClPaER = "''";
	public String Cop_FolPaq = "''"; //Antes int 
	public String Cop_TipPag = "''";
	public String Cop_ClPaRE = "''";
	public String Cop_MonPag = "''";//Antes double
	public String Cop_FolPag = "''";//Antes long
	public String Cop_ClaRas = "''";
	public String Cop_NomOrd = "''";
	public String Cop_TiCuOr = "''";
	public String Cop_CueOrd = "''";
	public String Cop_RFCUOr = "''";
	public String Cop_NomBen = "''";
	public String Cop_TiCuBe = "''";
	public String Cop_CueBen = "''";
	public String Cop_RFCUBe = "''";
	public String Cop_ConPag = "''";
	public String Cop_ImpIVA = "''";//Antes double 
	public String Cop_RefNum = "''";
	public String Cop_ReCoUn = "''";
	public String Cop_ClaPag = "''";
	public String Cop_NoBen2 = "''";
	public String Cop_RFCUB2 = "''";
	public String Cop_TiCuB2 = "''";
	public String Cop_CuBeDo = "''";
	public String Cop_CoPaDo = "''";
	public String Cop_TipOpe = "''";
	public String Cop_CauDev = "''";
	public String Cop_FoInOr = "''";
	public String Cop_FoPaOr = "0";
	public String Cop_FeTrOr = "''";
	public String Cop_ClRaOr = "''";
	public String Cop_CoPaUn = "''";
	public String Cop_MoPaOr = "0.0";
	public String Cop_InfFac = "''";
}
