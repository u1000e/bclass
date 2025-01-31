package com.kh.bclass.board.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	
	private final Path fileStorageLocation;
    
    public FileService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("파일 업로드 디렉토리를 생성할 수 없습니다.", ex);
        }
    }
    
    public String storeFile(MultipartFile file) {
        String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        try {
            // 파일 이름에 유효하지 않은 경로 시퀀스가 있는지 확인
            if(fileName.contains("..")) {
                throw new RuntimeException("잘못된 파일 이름: " + fileName);
            }
            // 타겟 위치 설정
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            // 파일 복사 (기존 파일 덮어쓰기)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return "http://localhost/uploads/" + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName, ex);
        }
    }

}
