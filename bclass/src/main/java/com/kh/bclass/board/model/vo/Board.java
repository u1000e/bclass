package com.kh.bclass.board.model.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Board {
	
    @NotNull(message = "게시글 번호는 필수값입니다.")
    @Min(value = 1, message = "게시글 번호는 최소 1 이상이어야 합니다.")
	private Long boardNo;
    
    @NotBlank(message = "게시글 제목은 비어 있을 수 없습니다.")
	private String boardTitle;
    
    @NotBlank(message = "게시글 내용은 비어 있을 수 없습니다.")
	private String boardContent;
    
    @NotBlank(message = "게시글 작성자는 비어 있을 수 없습니다.")
	private String boardWriter;

}
