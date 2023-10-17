package com.bim.migracion.web.Request;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidadorPoaRequest {

	private Map<Integer, String> filesPoa;
	private String tipeContingency;
	private String nameFile;
	
}
