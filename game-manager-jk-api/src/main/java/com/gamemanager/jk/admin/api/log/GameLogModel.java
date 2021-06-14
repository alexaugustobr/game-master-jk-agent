package com.gamemanager.jk.admin.api.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameLogModel {
	private String fileName;
	private OffsetDateTime date;
}
