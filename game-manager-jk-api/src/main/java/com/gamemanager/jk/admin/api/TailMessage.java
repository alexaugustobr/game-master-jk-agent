package com.gamemanager.jk.admin.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class TailMessage {
	
	private OffsetDateTime dateTime;
	private String message;
	
}
