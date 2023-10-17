package com.bim.migracion.web.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

	 @Autowired
	    private JavaMailSender mailSender;
	 
	 @GetMapping("mail")
	 public void mail() {
		 System.out.println("TEST");
		 try {
			 String from = "le.lopez@bim.mx";
			 String to = "le.lopez@bim.mx";
			  
			 SimpleMailMessage message = new SimpleMailMessage();
			  
			 message.setFrom(from);
			 message.setTo(to);
			 message.setSubject("This is a plain text email");
			 message.setText("Hello guys! This is a plain text email.");
			  
			 mailSender.send(message);
		 }catch (MailException e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
		 
	 }
	 
	 @GetMapping("mailtest")
	 public void mail1() {
		 
		 System.out.println("TEST123");
		 try {
			 String from = "l.lozano@bim.mx";
			  
			 SimpleMailMessage message = new SimpleMailMessage();
			  
			 message.setFrom(from);
			 message.setTo(new String[] {"l.lozano@bim.mx","db.garcia@bim.mx","deyvid-gol@hotmail.com","luis-lozano96@hotmail.com"});
			 message.setSubject("TEST MAIL SPRING");
			 message.setText("ESTE ES UN DEMO");
			  
			 mailSender.send(message);
		 }catch (MailException e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
		 
		 
	 }
	 
	 
}
