package com.kh.bclass.comment.model.vo;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class Comment {
	
	private Long commentNo;
	private Long boardNo;
	private String userNo;
	@NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
	private String content;
	private LocalDateTime createDate;

}
