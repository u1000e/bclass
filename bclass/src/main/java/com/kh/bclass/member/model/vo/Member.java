package com.kh.bclass.member.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
	
	private Long userNo;
	
    @NotBlank(message = "ID는 필수 입력 값 입니다.")
    @Size(min = 5, max = 20, message = "ID는 5글자 이상 20글자 이하만 사용할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "ID는 영어/숫자만 사용 가능합니다.")
	private String userName;
    
    @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
    @Size(min = 4, max = 20, message = "비밀번호는 4글자 이상 20글자 이하만 사용할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "비밀번호는 영어/숫자만 사용 가능합니다.")
	private String userPwd;
	private String role;
}
