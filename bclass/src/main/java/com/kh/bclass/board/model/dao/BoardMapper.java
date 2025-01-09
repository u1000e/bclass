package com.kh.bclass.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.bclass.board.model.vo.Board;

@Mapper
public interface BoardMapper {

	void save(Board board);

	List<Board> findAll(RowBounds rowBounds);

	Board findById(Long boardNo);

	void updateBoard(Board existingBoard);

	void deleteById(Long boardNo);

}
