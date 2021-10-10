package com.gamemanager.jk.admin.api;

import com.gamemanager.GameServerConfig;
import com.gamemanager.jk.admin.domain.server.ServerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/update")
@Slf4j
@AllArgsConstructor
public class UpdateServerController {
	
	private final ServerRepository serverRepository;
	private final DefaultExecutor executor = new DefaultExecutor();

	@PutMapping("/now")
	public void update() {

		GameServerConfig server = serverRepository.loadCurrent();

		try {
			executeCommand(server.getUpdateCommand());

			String msg = "Update command send!";
			log.info(msg);

		} catch (Exception e) {
			String msg = "Error occurred when sending the update command to the server!";
			log.error(msg);
			throw  new RuntimeException(msg);
		}

	}
	
	private void executeCommand(String cmd) throws IOException {
		CommandLine cmdLine = CommandLine.parse(cmd);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		executor.execute(cmdLine, resultHandler);
	}
	
}
