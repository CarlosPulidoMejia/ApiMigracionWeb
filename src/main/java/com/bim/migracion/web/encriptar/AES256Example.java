package com.bim.migracion.web.encriptar;

public class AES256Example {
	public static void main(String[] args) {
	    String originalString = "jdbc:jtds:sybase://172.30.12.43:5000/dbBIM";
	    
	    String mensajeCript = "1jqoJripc2cVLLdvKqsHqrsf0aa4gJ6N/1S325SWIx+V/97E06WKqSywYVD/dN/bvVyEAcAfxRXIrWmER1NN+g==";
	    //QzP8rJsZSaDEzulgah/YGQ==
	    /*Usuario: CDAC
Pwd: pwdCDAC//fVGYx0Aj1hoj1K5ID6n25A==
*/
	 
	    String encryptedString = AES256.encrypt(originalString);
	    String decryptedString = AES256.decrypt(encryptedString);
	    String decryptedString2 = AES256.decrypt(mensajeCript);
	 
	    System.out.println("Mensaje original: " + originalString);
	    System.out.println("Mensaje cifrado: " + encryptedString);
	    System.out.println("Mensaje descifrado: " + decryptedString);
	    System.out.println(decryptedString2);
	  }
}
