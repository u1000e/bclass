package com.kh.bclass.storage.model.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	
	String upload(MultipartFile image);
	
	
	void delete(String fileUri);
	
}
