package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

// NotNull = Null값 체크
// NotEmpty = 빈값이거나 Null 체크
//  NotBlank = 빈값이거나 null 체크 그리고 빈 공백까지

@Data
public class CommentDto {
	@NotNull
	private Integer imageId;
	@NotBlank
	private String content;
	
	//toEntity가 필요 없음.
}
