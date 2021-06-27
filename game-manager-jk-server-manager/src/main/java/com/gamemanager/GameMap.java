package com.gamemanager;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class GameMap {
	
	private String name;
	private OffsetDateTime createdAt;
	
}
