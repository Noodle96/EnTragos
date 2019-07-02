package com.example.demo.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
	//carga la foto
	public Resource load(String filename)throws MalformedURLException;
	
	//copy el nuevo directorio
	public String copy(MultipartFile file)throws IOException;
	
	//para saber si lo elimino y mandar mensaje al flash
	public boolean delete(String filename);
	
	public void deleteAll();
	
	public void init() throws IOException;
}
