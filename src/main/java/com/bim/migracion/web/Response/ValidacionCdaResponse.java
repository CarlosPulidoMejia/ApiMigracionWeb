package com.bim.migracion.web.Response;

import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ValidacionCdaResponse {

	Map<Integer, String> mapHeader= new TreeMap<>();
	Map<Integer, String> informacion= new TreeMap<>();
	Map<Integer, String> validacion= new TreeMap<>();
}
