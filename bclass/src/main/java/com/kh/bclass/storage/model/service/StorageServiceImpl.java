package com.kh.bclass.storage.model.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.kh.bclass.exception.FileStorageException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService{
	
	private final AmazonS3 storage;
	
	@Value("${cloud.aws.s3.bucketName}")
	private String bucketName;
	
	private void validateImage(String filename) {
		  int lastDotIndex = filename.lastIndexOf(".");
		  if (lastDotIndex == -1) {
		    throw new RuntimeException("확장자 없는파일");
		  }
		  String extention = filename.substring(lastDotIndex + 1).toLowerCase();
		  List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

		  if (!allowedExtentionList.contains(extention)) {
		    throw new RuntimeException("이미지 형식 아닌뎅~");
		  }
	}

	@Override
	public String upload(MultipartFile image) {
		
		if(image == null || "".equals(image.getOriginalFilename())) {
			throw new FileStorageException("이상요상한 파일");
		}
		validateImage(image.getOriginalFilename());
		
		return uploadImageToStorage(image);
	}

	private String uploadImageToStorage(MultipartFile image) {
	    String originalFilename = image.getOriginalFilename();
	    String ext = originalFilename.substring(originalFilename.lastIndexOf("."));

	    String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename;

	    try (InputStream is = image.getInputStream();
	         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(IOUtils.toByteArray(is))) {

	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentType("image/" + ext);
	        metadata.setContentLength(byteArrayInputStream.available());

	        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
	                .withCannedAcl(CannedAccessControlList.PublicRead);

	        storage.putObject(putObjectRequest);
	    } catch (IOException e) {
	        throw new FileStorageException("파일 업로드 실패");
	    }

	    return storage.getUrl(bucketName, s3FileName).toString();
	}

	@Override
	public void delete(String fileUri) {
		String key = "";
		try {
			URL url = new URL(fileUri);
			key = URLDecoder.decode(url.getPath(), "UTF-8");
		}catch(IOException e) {
			throw new FileStorageException("파일 삭제 실패");
		}
		storage.deleteObject(new DeleteObjectRequest(bucketName, key.substring(key.lastIndexOf("/") + 1)));
	}

}
