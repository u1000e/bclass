package com.kh.bclass.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.bclass.board.model.vo.Board;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public interface BoardService {
	
	void save(Board board, MultipartFile file);
	
	List<Board> findAll(int page);
	
	Board findById(Long boardNo);

	String upfile(MultipartFile file);

	void deleteFile(String fileUri);

	Board updateBoard(Board board, MultipartFile file);

	void deleteById (Long boardNo);
	

}
