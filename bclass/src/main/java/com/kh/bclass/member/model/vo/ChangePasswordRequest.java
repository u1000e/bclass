package com.kh.bclass.member.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
	
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
	private String currentPassword;
    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
	private String newPassword;

}
