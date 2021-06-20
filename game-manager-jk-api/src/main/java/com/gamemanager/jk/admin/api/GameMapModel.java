package com.gamemanager.jk.admin.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class GameMapModel {
	
	private String name;
	private OffsetDateTime createdAt;
	
}
