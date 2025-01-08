package com.kh.bclass.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.bclass.board.model.vo.Board;

public interface BoardService {
	
	void save(Board board, MultipartFile file);
	
	List<Board> findAll();
	
	Board findById(Long boardNo);

	String upfile(MultipartFile file);

	void deleteFile(String fileUri);
	

}
