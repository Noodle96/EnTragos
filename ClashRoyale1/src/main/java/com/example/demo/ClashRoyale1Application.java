package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.models.service.IUploadFileService;

@SpringBootApplication
public class ClashRoyale1Application implements CommandLineRunner {

	@Autowired
	IUploadFileService uploadFileService;
	public static void main(String[] args) {
		SpringApplication.run(ClashRoyale1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
	}

}
