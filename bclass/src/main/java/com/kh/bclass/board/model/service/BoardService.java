package com.kh.bclass.board.model.service;

import java.util.List;

import com.kh.bclass.board.model.vo.Board;

public interface BoardService {
	
	void save(Board board);
	
	List<Board> findAll();
	
	Board findById(Long boardNo);
	

}
