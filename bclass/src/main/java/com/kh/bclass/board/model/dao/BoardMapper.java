package com.kh.bclass.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.bclass.board.model.vo.Board;

@Mapper
public interface BoardMapper {

	void save(Board board);

	List<Board> findAll();

	Board findById(Long boardNo);

}
