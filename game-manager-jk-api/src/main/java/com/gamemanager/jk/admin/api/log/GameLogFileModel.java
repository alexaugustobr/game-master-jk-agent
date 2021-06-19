package com.gamemanager.jk.admin.api.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameLogFileModel {
	private String name;
	private OffsetDateTime dateTime;
}
