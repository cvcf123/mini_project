package com.example.mini_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
	  private Long answerId; //어떤 답변(Answer)에 대한 댓글인지 식별.
	  private String content; //댓글 내용.
	 
}