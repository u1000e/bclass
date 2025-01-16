package com.kh.bclass.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.bclass.comment.model.service.CommentService;
import com.kh.bclass.comment.model.vo.Comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("comments")
@Validated
public class CommentController {
	
	private final CommentService service;
	
	@GetMapping("/{boardNo}")
	public ResponseEntity<List<Comment>> findByBoardNo(@PathVariable(name="boardNo") @Valid @Min(value = 1, message = "게시글 번호는 1 이상이어야 합니다.") Long boardNo){
		List<Comment> list = service.findByBoardNo(boardNo);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping
	public ResponseEntity<?> insertComment(@RequestBody @Valid Comment comment){
		service.insertComment(comment);
		return ResponseEntity.status(HttpStatus.CREATED).body("댓글 작성에 성공했습니다.");
	}

}
