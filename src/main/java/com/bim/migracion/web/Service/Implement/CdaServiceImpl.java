package com.bim.migracion.web.Service.Implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletContext;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.openssl.PasswordFinder;

import com.bim.migracion.web.Conexion.DataSourceSybase;
import com.bim.migracion.web.Entity.DatosCdaEntity;
import com.bim.migracion.web.Propiedades.PropiedadesSybase;
import com.bim.migracion.web.Request.DataSourceRequest;
import com.bim.migracion.web.Request.GetCDASRequest;
import com.bim.migracion.web.Response.ResponseGetCdas;
import com.bim.migracion.web.Service.CdaService;
import com.bim.migracion.web.Service.DatasourceService;
import com.bim.migracion.web.encriptar.AES256;

@Service
public class CdaServiceImpl implements CdaService {

	@Autowired
	PropiedadesSybase propiedadesSybase;
	
	@Autowired
	private DatasourceService dataService;

	private String pipeDoble = "||";
	private String pipeSencillo = "|";
	// private static int tamañoRegistro = 1832;
	private static int tamanioRegistro = 1857;
	private String File_cve;
	@SuppressWarnings("unused")
	private String File_crt;
	private String File_cer;
	private String Certificado;
	private String Frase;

	@Value("${bim.pass}")
	private String bimPass;

	 @Value("${bim.username}")
	 private String bimUser;
	 
	 @Value("${bim.url}")
	 private String bimUrl;

	// private String orpNumeroIni;
	// private String orpNumeroFin;

	@SuppressWarnings("unused")
	@Override
	public List<ResponseGetCdas> getCDAs(GetCDASRequest request) {
		// TODO Auto-generated method stub
		// Set Conexion Sybase
		System.out.println("Pass: " + request.getDatasource().getPass());

		String decryptedString = AES256.decrypt(bimPass);

		String decryptedString2 = AES256.decrypt(bimUser);

		String decryptedString3 = AES256.decrypt(bimUrl);

		//System.out.println(decryptedString);

		DataSourceRequest dataSourceRequest = dataService.findByIdSbe(request.getDatasource().getId());
		
		System.out.println("Datos a buscar: " + request.getTipoBusqueda());
		
		 /*if(dataSourceRequest.getDescripcion().equals("Produccion")) {
			 System.out.println("Ingresando a produccion");
			 DataSourceSybase.setConexionProd(decryptedString3,decryptedString2,decryptedString);
			 System.out.println("url:" + decryptedString3 + " user: " + decryptedString2 + "pass:" + decryptedString);
			 
			
			 
			 
		 }*/
		if(dataSourceRequest.getDescripcion().equals("Produccion")) {
			System.out.println("Ingresando a produccion");
			 DataSourceSybase.setConexionProd("jdbc:jtds:sybase://172.30.12.43:5000/dbBIM","CDAC",
						decryptedString);
		 }
		 else {

		DataSourceSybase.setConexion(dataSourceRequest.getIp(), dataSourceRequest.getPuerto(),
				dataSourceRequest.getBase(), dataSourceRequest.getUsuario(),
				dataSourceRequest.getPass());
		System.out.println(dataSourceRequest.getUsuario());
		
		}

		DataSourceSybase.getConnection();
		 System.out.println("Enviando conexion");
		
		

		eliminaCarpeta();
		guardaCertificados(request);
		cargaArchivos(request);
		List<ResponseGetCdas> response = generaArchivos(request);

		DataSourceSybase.cerrarConexion();

		return response;
		
		//return null;
	}

	@Override
	public List<ResponseGetCdas> generaArchivos(GetCDASRequest request) {
		// TODO Auto-generated method stub
		System.out.println("Generar archivos");
		List<ResponseGetCdas> response = new ArrayList<ResponseGetCdas>();
		ArrayList<DatosCdaEntity> dg = new ArrayList<>();
		try {
			Connection con = DataSourceSybase.getConnection();
			CallableStatement cs = null;
			// cs = con.prepareCall("{" + "CALL SPCODEABCON_POA_COAS_e '', '', '', '" +
			// Certificado
			// + "', 500, '', '', '', '', '', '', '' " + "}");
			System.out.println("INICIO: --" + request.getOrpNumIni());
			
			
			String inicio = request.getOrpNumIni() =="" ? "" : request.getOrpNumIni();
			
			String fin = request.getOrpNumFin() =="" ? "" : request.getOrpNumFin();
			
			
			
			System.out.println(request.getOrpNumFin() + "-" + request.getOrpNumIni());
			
			String query="CALL SPCODEABCON_POA_COAS_e '', '', '', '" + Certificado +"', 500,'"+ request.getTipoBusqueda() +"','"+inicio +"','" + fin + "', '', '', '', '', '', '', '' " +"";
			//String query="CALL SPCODEABCON_POA_COAS_e '', '', '', '" + Certificado +"', 500,'"+inicio +"','" + fin + "', '', '', '', '', '', '', '' " +"";
			
			System.out.println("quer: " + query);
			
			System.out.println("Generar archivos1" + request.getDatasource().getBase());
			cs = con.prepareCall("{"+query+"}");
			ResultSet rs = cs.executeQuery();
			System.out.println("CALL SPCODEABCON_POA_COAS_e '', '', '', '" + Certificado + "', 500,'"
					+ request.getOrpNumIni() + "','" + request.getOrpNumFin() + "','N', '', '', '', '', '', '', ''");
			System.out.println(Certificado);
			while (rs.next()) {

				DatosCdaEntity de = new DatosCdaEntity();
				de.setCda_FecOpe(rs.getString("Cda_FecOpe"));
				de.setRegistros(rs.getString("Registros"));
				de.setFilename(rs.getString("Filename"));
				dg.add(de);
				System.out.println(rs.getString("Cda_FecOpe"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		response = crearArchivo(dg);

		return response;
		// return null;
	}

	private List<ResponseGetCdas> crearArchivo(ArrayList<DatosCdaEntity> datos) {
		List<ResponseGetCdas> listado = new ArrayList<>();

		// ResponseArchivos response = new ResponseArchivos();

		// List<Response_CrearArchivo> listado = new ArrayList<Response_CrearArchivo>();

		int i = 1;

		for (DatosCdaEntity dato : datos) {
			ResponseGetCdas archivo = new ResponseGetCdas();

			archivo = _crearArchivo(dato);
			archivo.setNumero(i);

			// ResponseGetCdas archivo = _crearArchivo(dato);
			// Response_CrearArchivo archivof = new Response_CrearArchivo();

			// archivof.setArchivo(archivo.getArchivo());
			// archivof.setFileName(archivo.getFileName());
			// archivof.setNumero(i);

			listado.add(archivo);
			System.out.println("Archivo # " + i);
			i++;
		}

		// response.setListaArchivos(listado);

		return listado;
	}

	private ResponseGetCdas _crearArchivo(DatosCdaEntity datos) {
		System.out.println("crear archivo");
		ResponseGetCdas response = new ResponseGetCdas();

		String cadena = "";
		String contenidoCDA;
		String fileName = "";
		int i = 1;
		try {

			ResultSet rs = propiedadesSybase.getResultSetStoreGenerico(
					"CALL SPCODEABCON_POA_COAS_f '', '', '" + datos.getFilename() + "' ,'','','','','','',''");

			File fileDir = new File("archivos/" + datos.getFilename());

			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));

			while (rs.next()) {
				System.out.println("Info123");
				contenidoCDA = contenidoCDA(rs);
				cadena = formatoCadena_CerIzq(10, rs.getString("Orp_Paquet"));
				cadena += formatoCadena_CerIzq(5, rs.getString("Orp_ConPaq"));
				cadena += formatoCadena_CerIzq(5, rs.getString("Orp_InsEmi"));
				cadena += formatoCadena_EspacioDer(40, rs.getString("Orp_ClaRas"));
				System.out.println("----------Cadena lenght: " + cadena.length());
				cadena += contenidoCDA;

				System.out.println("------Cadena File: " + cadena);
				System.out.println("----------Cadena2 lenght: " + cadena.length());
				out.append(cadena + "\r\n");
				cadena = "";

				System.out.println("Registro # " + i);
				i++;

//				_relacionCDAGenerada(rs, out_relCDA);

			}

			out.flush();
			out.close();

			fileName = datos.getFilename();
			response.setFileName(fileName);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// AccesoBD.desconectar();
		}

		return response;
	}

	private String formatoCadena_CerIzq(int tamano, String cadena) {
		String repeated = "";
		int numRep = tamano - cadena.length();
		for (int i = 1; i <= numRep; i++) {
			repeated += "0";
		}
		repeated = repeated + cadena;
		System.out.println("Rep: " + numRep);
		System.out.println("formatoCadena_CerIzq: " + repeated);
		return repeated;
	}

	private String formatoCadena_EspacioDer(int tamano, String cadena) {
		String repeated = "";
		System.out.println("----tamanio:" + tamano);
		System.out.println("----cadena:" + cadena.length());
		int numRep = tamano - cadena.length();
		for (int i = 1; i <= numRep; i++) {
			repeated += " ";
		}
		repeated = cadena + repeated;
		System.out.println("Rep: " + numRep);
		System.out.println("formatoCadena_CerDerecha: " + repeated);
		return repeated;
	}

	private String contenidoCDA(ResultSet rs) throws Exception {
		System.out.println("Info");
		String res = "";
		String firma = "";
		try {
			res = pipeDoble + rs.getString("Orp_TipPag") + pipeSencillo + rs.getString("Cda_FecOpe") + pipeSencillo
					+ rs.getString("Cda_FecAbo") + pipeSencillo + rs.getString("Cda_HorAbo") + pipeSencillo
					+ rs.getString("Cda_ClaEmi") + pipeSencillo + rs.getString("Cda_InsEmi") + pipeSencillo
					+ rs.getString("Orp_NomOrd") + pipeSencillo + rs.getString("Orp_TiCuOr") + pipeSencillo
					+ rs.getString("Orp_CueOrd") + pipeSencillo + rs.getString("Orp_DatOrd") + pipeSencillo
					+ rs.getString("Cda_InsRec") + pipeSencillo + rs.getString("Cda_NomBen") + pipeSencillo
					+ rs.getString("Orp_TiCuBe") + pipeSencillo + rs.getString("Orp_CueBen") + pipeSencillo
					+ rs.getString("Cda_DatBen") + pipeSencillo + rs.getString("Orp_ConPag") + pipeSencillo
					+ rs.getString("Orp_IVA") + pipeSencillo + rs.getString("Orp_Cantid") + pipeSencillo +
					// Datos Codi
					rs.getString("Cod_NomBe2") + pipeSencillo + rs.getString("Cod_RFCUR2") + pipeSencillo
					+ rs.getString("Cod_TiCuB2") + pipeSencillo + rs.getString("Cod_CuBen2") + pipeSencillo
					+ rs.getString("Cod_FoEsCD") + pipeSencillo + rs.getString("Cod_TiCoTr") + pipeSencillo
					+ rs.getString("Cod_MoCoTr") + pipeSencillo +
					// Datos Codi

					rs.getString("Cda_NuSeCe") + pipeDoble;

			// rs.getString("Cda_SelDig");
			System.out.println("------Res: " + res.length());
			firma = sign(res);
			boolean verifies = verify(res, firma);
			System.out.println("res cda sign: " + verifies);
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(e.getMessage());
		} catch (CertificateException e) {
			throw new CertificateException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new NoSuchAlgorithmException(e.getMessage());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		System.out.println("SUMA" + res.length() + "--" + firma.length());
		res = res + firma;

		System.out.println("----- Res+Firma:" + res);
		System.out.println("----- Res+Firma LENGHT:" + res.length());
		res = formatoCadena_EspacioDer(tamanioRegistro, res);
		return res;
	}

	private static KeyPair readKeyPair(File privateKey, char[] keyPassword) throws IOException {
		FileReader fileReader = new FileReader(privateKey);
		PEMReader r = new PEMReader(fileReader, new DefaultPasswordFinder(keyPassword));

		try {
			return (KeyPair) r.readObject();
		} catch (IOException ex) {

			ex.printStackTrace();

			throw new IOException(ex.getCause() + ex.getMessage());
			// throw new IOException("La frase proporcionada no corresponde al
			// certificado.", ex);
		} finally {
			r.close();
			fileReader.close();
		}

	}

	private static class DefaultPasswordFinder implements PasswordFinder {

		private final char[] password;

		private DefaultPasswordFinder(char[] password) {
			this.password = password;
		}

		public char[] getPassword() {
			return password;
		}
	}

	private String sign(String message)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		String signedString = "";
		Security.addProvider(new BouncyCastleProvider());
		try {
			// Bytes[] Se obtiene flujo [pkstr]
			File privateKey = new File(File_cve);
			// File privateKey = new File(File_cve);
			KeyPair keyPair;

			System.out.println("File_cve: " + File_cve);

			// RSAKey key Se genera llave RSA
			keyPair = readKeyPair(privateKey, Frase.toCharArray());

			// keyPair = readKeyPair(privateKey, Frase.toCharArray());

			// RSADigestEngine Motor Se crea motor con digestión SHA1 y key
			Signature sig = Signature.getInstance("SHA256withRSA");
			sig.initSign(keyPair.getPrivate());

			// Motor.update Se crea la digestión, Cadena
			byte[] bytes = message.getBytes("UTF-8");
			sig.update(bytes, 0, bytes.length);

			// Digest &sign Obtiene la firma
			byte[] signatureBytes = sig.sign();

			// Str FirmaB64 Obtiene la firma en formatoB64
			// signedString = new sun.misc.BASE64Encoder().encode(signatureBytes);
			signedString = new String(org.bouncycastle.util.encoders.Base64.encode(signatureBytes));
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} catch (InvalidKeyException e) {
			throw new InvalidKeyException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			throw new NoSuchAlgorithmException(e.getMessage());
		} catch (SignatureException e) {
			throw new SignatureException("Ocurrio un Error al firmar el archivo ", e);
		}

		System.out.println("signedString: " + signedString);
		System.out.println("---------Firma lenght:" + signedString.length());

		return signedString;
	}

	private boolean verify(String text, String signature) throws Exception {
		// X509Certificate pubcert Lee el certificado en formato pem
		boolean result = false;
		Security.addProvider(new BouncyCastleProvider());

		// X509Certificate pubcert Lee el certificado en formato pem
		Security.addProvider(new BouncyCastleProvider());

		FileInputStream fin = new FileInputStream(File_cer);
		// FileInputStream fin = new FileInputStream(File_crt);

		CertificateFactory f = CertificateFactory.getInstance("X509");

		X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);

		fin.close();

		// RSAKey key2 Extrae la llave publica del certificado
		PublicKey pk = certificate.getPublicKey();

		// RSADigestEngine Motor2 Se crea motor con digestión SHA1 y key2
		String algorithm = "SHA256withRSA";
		Signature verifier = Signature.getInstance(algorithm);

		try {
			verifier.initVerify(pk); // This one checks key usage in the cert
			verifier.update(text.getBytes("UTF-8"));
			result = verifier.verify(org.bouncycastle.util.encoders.Base64.decode(signature.getBytes("UTF-8")));
		} catch (Exception e) {
			throw new Exception(e.getMessage() + "Error al validar el certificado" + File_cer, e);
		}
		return result;
	}

	public void eliminaCarpeta() {
		String directorio = "certificado";
		File f = new File(directorio);

		if (f.delete())
			System.out.println("El fichero " + directorio + " ha sido borrado correctamente");
		else
			System.out.println("El fichero " + directorio + " no se ha podido borrar");
	}

	public void guardaCertificados(GetCDASRequest request) {
		File directorio = new File("certificado");
		File dirArchivos = new File("archivos");

		if (!directorio.exists()) {
			if (directorio.mkdirs()) {
				System.out.println("Directorio creado");
			} else {
				System.out.println("Error al crear directorio");
			}
		}

		if (!dirArchivos.exists()) {
			if (dirArchivos.mkdirs()) {
				System.out.println("Directorio creado");
			} else {
				System.out.println("Error al crear directorio");
			}
		}

		byte[] dataCer = Base64.decodeBase64(request.getCer());
		byte[] dataCve = Base64.decodeBase64(request.getCve());
		byte[] dataCrt = Base64.decodeBase64(request.getCrt());

		try {
			OutputStream streamCer = new FileOutputStream("certificado/" + request.getNameCer());
			OutputStream streamCve = new FileOutputStream("certificado/" + request.getNameCve());
			OutputStream streamCrt = new FileOutputStream("certificado/" + request.getNameCrt());

			streamCer.write(dataCer);
			streamCve.write(dataCve);
			streamCrt.write(dataCrt);

			streamCer.close();
			streamCve.close();
			streamCrt.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cargaArchivos(GetCDASRequest request) {

		File cer = new File("certificado/" + request.getNameCer());
		File cve = new File("certificado/" + request.getNameCve());
		File crt = new File("certificado/" + request.getNameCrt());

		File_cer = cer.getAbsolutePath();
		File_crt = crt.getAbsolutePath();
		File_cve = cve.getAbsolutePath();
		Certificado = request.getNameCrt().substring(0, request.getNameCrt().indexOf("."));
		Frase = request.getPassword();

	}

	public MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
		// application/pdf
		// application/xml
		// image/gif, ...
		String mineType = servletContext.getMimeType(fileName);
		try {
			MediaType mediaType = MediaType.parseMediaType(mineType);
			return mediaType;
		} catch (Exception e) {
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

}
