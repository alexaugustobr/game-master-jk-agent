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
@RequestMapping("/api/v1/power-options")
@Slf4j
@AllArgsConstructor
public class PowerOptionsController {
	
	private final ServerRepository serverRepository;
	private final DefaultExecutor executor = new DefaultExecutor();
	
	@PutMapping("/poweron")
	public void poweron() {
		
		GameServerConfig server = serverRepository.loadCurrent();
		
		try {
			executeCommand(server.getPoweronCommand());
			
			String msg = "Power on command send!";
			log.info(msg);
			
		} catch (Exception e) {
			String msg = "Error occurred when sending the power on command to the server!";
			log.error(msg);
			throw  new RuntimeException(msg);
		}
		
	}
	
	@PutMapping("/poweroff")
	public void poweroff() {
		
		GameServerConfig server = serverRepository.loadCurrent();
		
		try {
			executeCommand(server.getPoweroffCommand());
			
			String msg = "Power off command send!";
			log.info(msg);
			
		} catch (Exception e) {
			String msg = "Error occurred when sending the power on command to the server!";
			log.error(msg);
			throw  new RuntimeException(msg);
		}
		
	}
	
	@PutMapping("/restart")
	public void restart() {
		
		GameServerConfig server = serverRepository.loadCurrent();
		
		try {
			executeCommand(server.getRestartCommand());
			
			String msg = "Restart command send!";
			log.info(msg);
			
		} catch (Exception e) {
			String msg = "Error occurred when sending the power on command to the server!";
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
