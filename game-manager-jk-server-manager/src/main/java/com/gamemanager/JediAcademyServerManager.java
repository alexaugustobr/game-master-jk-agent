package com.gamemanager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Slf4j
public class JediAcademyServerManager {

	public AnonymousCommandSender asAnonymous(JediAcademyServerConnector connector) {
		return new AnonymousCommandSender(connector);
	}
	
	public SmodCommandSender asSmod(JediAcademyServerConnector connector, String pass) {
		return new SmodCommandSender(connector, pass);
	}
	
	public RootCommandSender asRoot(JediAcademyServerConnector connector, String pass) {
		return new RootCommandSender(connector, pass);
	}

	public LocalCommandSender runLocally(GameServerConfig config) {
		return new LocalCommandSender(config);
	}

	@AllArgsConstructor
	@Getter
	public static class LocalCommandSender {
		
		private final GameServerConfig config;

		public List<GameMap> getGameMaps() {

			Path mb2Path = Paths.get(config.getMb2Path());

			File[] gamePk3Files = mb2Path.toFile().listFiles((dir, name) -> name.toLowerCase().endsWith(".pk3"));
			Set<GameMap> gameMapSet = new HashSet<>();

			if (gamePk3Files != null) {
				for (File pk3File : gamePk3Files) {
					try {
						FileTime creationTime = (FileTime) Files.getAttribute(pk3File.toPath(), "creationTime");

						ZipFile zipFile = new ZipFile(pk3File);

						Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();

						while (entries.hasMoreElements()) {
							ZipArchiveEntry zipArchiveEntry = entries.nextElement();
							if (zipArchiveEntry.getName().contains(".bsp")) {
								gameMapSet.add(new GameMap(pk3File.getName(),
										OffsetDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault())
								));
							}
						}
						zipFile.close();
						
					} catch (IOException e) {
						log.error("Exception occurred while trying to read the pk3 file", e);
					}
				}
			}
			List<GameMap> gameMaps = new ArrayList<>(gameMapSet);
			gameMaps.sort(Comparator.comparing(GameMap::getCreatedAt));
			return gameMaps;
		}

		public List<GameFile> getGameFiles() {

			Path mb2Path = Paths.get(config.getMb2Path());

			File[] files = mb2Path.toFile().listFiles(File::isFile);
			List<GameFile> gameFiles = new ArrayList<>();

			if (files != null) {
				for (File file : files) {
					try {
						FileTime creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
						gameFiles.add(new GameFile(file.getName(),
								OffsetDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault())
						));
					} catch (IOException e) {
						log.error("Exception occurred while trying to read the file", e);
					}
				}
			}

			return gameFiles;
		}

		public void deleteFileByName(String fileName) {
			Path mb2Path = Paths.get(config.getMb2Path());

			Optional<File> gamePk3File = Arrays.stream(mb2Path.toFile()
					.listFiles())
					.filter(o -> o.getName().equalsIgnoreCase(fileName)).findAny();

			File file = gamePk3File.orElseThrow(RuntimeException::new);
			try {
				Files.deleteIfExists(file.toPath());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void deleteMapByName(String fileName) {
			Path mb2Path = Paths.get(config.getMb2Path());

			Optional<File> gamePk3File = Arrays.stream(mb2Path.toFile()
					.listFiles((dir, name) -> name.toLowerCase().endsWith(".pk3")))
					.filter(o -> o.getName().equalsIgnoreCase(fileName)).findAny();

			File file = gamePk3File.orElseThrow(RuntimeException::new);
			try {
				Files.deleteIfExists(file.toPath());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
	}

	@AllArgsConstructor
	@Getter
	public static class AnonymousCommandSender {
		
		private final JediAcademyServerConnector connector;
		
		private static final String BASIC_INFO_COMMAND = "getinfo";
		private static final String DETAILED_INFO_COMMAND = "getstatus";
		
		public Map<ServerStatusType, String> getStatus() throws CommunicatingWithServerException {
			Map<ServerStatusType, String> status = new LinkedHashMap<>();
			status.putAll(getBasicInfo());
			status.putAll(getDetailedInfo());
			return status;
		}
		
		public Map<ServerStatusType, String> getBasicInfo() throws CommunicatingWithServerException {
			try {
				byte[] outputMessage = connector.executeCommand(BASIC_INFO_COMMAND);
				return ServerStatusTranslator.translate(outputMessage);
			} catch (Exception e) {
				throw new CommunicatingWithServerException(ErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER, e);
			}
		}
		
		public Map<ServerStatusType, String> getDetailedInfo() throws CommunicatingWithServerException {
			try {
				byte[] outputMessage = connector.executeCommand(DETAILED_INFO_COMMAND);
				return ServerStatusTranslator.translate(outputMessage);
			} catch (Exception e) {
				throw new CommunicatingWithServerException(ErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER, e);
			}
		}
		
		public int getPlayerCount() throws CommunicatingWithServerException {
			Map<ServerStatusType, String> status = this.getBasicInfo();
			return Integer.parseInt(status.getOrDefault(ServerStatusType.CLIENTS, "0"));
		}
		
		public List<String> getPLayers() throws CommunicatingWithServerException {
			try {
				byte[] outputMessage = connector.executeCommand(DETAILED_INFO_COMMAND);
				return ServerStatusTranslator.playerList(outputMessage);
			} catch (Exception e) {
				throw new CommunicatingWithServerException(ErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER, e);
			}
		}
	}
	
	@Getter
	public static class SmodCommandSender extends AnonymousCommandSender {
		
		private final String pass;
		
		public SmodCommandSender(JediAcademyServerConnector connector, String pass) {
			super(connector);
			this.pass = pass;
		}
		
		public void changeMap(String mapName) {
			//connector()
		}
		
	}
	
	@Getter
	public static class RootCommandSender extends SmodCommandSender {
		
		public RootCommandSender(JediAcademyServerConnector connector, String pass) {
			super(connector, pass);
		}
		
		public void resetServer() {
			//connector()
		}
	
	}
}
