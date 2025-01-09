package com.kh.bclass.board.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.bclass.board.model.service.BoardService;
import com.kh.bclass.board.model.vo.Board;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RequestMapping("boards")
@RequiredArgsConstructor
@RestController
@Validated
public class BoardController {
	
	private final BoardService service;
	
	@PostMapping
	public ResponseEntity<String> save(@ModelAttribute @Valid Board board, @RequestParam(name="file", required = false) MultipartFile file){
		service.save(board, file);
		return new ResponseEntity("게시글등록성공", HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Board>> findAll(@RequestParam(name="page", defaultValue = "0") int page){
		return ResponseEntity.ok(service.findAll(page));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Board> findById(@PathVariable(name="id") 
										  @Min(value = 1, message = "게시글번호는 최소 1 이상이어야 합니다.") Long boardNo){
		return ResponseEntity.ok(service.findById(boardNo));
	}
	
	@PostMapping("/file")
	public ResponseEntity<?> uploadFiles(@RequestParam(name="file") MultipartFile file){
		String imagePath = service.upfile(file);
		return ResponseEntity.ok(imagePath);
	}
	
	@DeleteMapping("/delete-file")
	public ResponseEntity<?> deleteFile(@RequestParam(name="fileUri") String fileUri){
		service.deleteFile(fileUri);
		return ResponseEntity.ok("잘지움");
	}

}
