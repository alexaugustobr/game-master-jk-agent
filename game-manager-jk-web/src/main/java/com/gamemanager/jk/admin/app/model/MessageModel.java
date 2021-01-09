package com.gamemanager.jk.admin.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MessageModel {
	
	private String body;
	private String state;
	private String title;
	
	public static MessageModel success(String body) {
		return MessageModel.builder()
				.body(body)
				.title("Success!")
				.state("success")
				.build();
	}
	
}
