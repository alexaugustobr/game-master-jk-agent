package com.gamemanager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GameMap {
	
	//TODO FIX ME
	@EqualsAndHashCode.Include
	private String name;
	private OffsetDateTime createdAt;
	
}
