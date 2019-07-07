package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.models.service.IUploadFileService;

@SpringBootApplication
public class ClashRoyale1Application implements CommandLineRunner {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	IUploadFileService uploadFileService;
	public static void main(String[] args) {
		SpringApplication.run(ClashRoyale1Application.class, args);
	}

	//this is a commenter
	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
		
		String password = "12345";
		for(int e = 0 ;e < 2 ; e++) {
			String BCryptPass = passwordEncoder.encode(password);
			System.out.println(BCryptPass);
		}
	}

}
