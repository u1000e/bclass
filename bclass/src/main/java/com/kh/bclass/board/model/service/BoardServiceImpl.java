package com.kh.bclass.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.bclass.board.model.dao.BoardMapper;
import com.kh.bclass.board.model.vo.Board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper mapper;

	@Override
	public void save(Board board) {
		log.info("{}", board);
		mapper.save(board);
	}

	@Override
	public List<Board> findAll() {
		return mapper.findAll();
	}

	@Override
	public Board findById(Long boardNo) {
		Board board = mapper.findById(boardNo);
		if(board == null) {
			throw new RuntimeException("존재하지 않는 게시글");
		}
		return board;
	}

}