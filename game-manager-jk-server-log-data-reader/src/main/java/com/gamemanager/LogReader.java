package com.gamemanager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@Slf4j
@AllArgsConstructor
@Getter
public class LogReader {
	
	private final File file;
	
	public LogReader.Chat onChat() {
		
		return new LogReader.Chat(this.file);
	}
	
	public static class Chat {
		
		private File file;
		
		public void watchChanges(@NotNull Consumer<Message> onChangeCallBack) {
			
			try {
				Path directory = Paths.get(file.getParent());
				WatchService watcher = FileSystems.getDefault().newWatchService();
				
				directory.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
				
				long lastIndex = getLastLineFromFile(file);
				
				while (true) {
					
					WatchKey key = watcher.take();
					
					for (WatchEvent event : key.pollEvents()) {
						
						switch (event.kind().name()) {
							case "ENTRY_CREATE": log.info("Entry was created on log dir."); break;
							case "ENTRY_DELETE": log.info("Entry was deleted from log dir."); break;
							case "ENTRY_MODIFY": {
								List<String> lines = readLinesFromIndex(file, lastIndex);
								//update the last index
								lastIndex += lines.size();
								createChatMessageFromLines(lines).forEach(onChangeCallBack);
								break;
							}
						}
					}
					
					key.reset();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			
		}
		
		private List<Message> createChatMessageFromLines(List<String> lines) {
			return lines.stream().filter(it -> it.contains(": say: "))
					.map(it -> toChatMessage(removeColorTags(it)))
					.collect(Collectors.toList());
		}
		
		private String removeColorTags(String str) {
			return str.replaceAll("\\^+[0-9]", "");
		}
		
		private Message toChatMessage(String str) {
			
			int firstSeparator = str.indexOf(":");
			int secondSeparator = str.indexOf(":", firstSeparator + 1);
			int thirdSeparator = str.indexOf(":", secondSeparator + 1);
			int fourthSeparator = str.indexOf(":", thirdSeparator + 1);
			
			String playerName = str.substring(thirdSeparator + 2, fourthSeparator);
			
			String message = str.substring(fourthSeparator + 2).replace("\"", "");
			
			return new Message(playerName, message);
		}
		
		private long getLastLineFromFile(File file) throws FileNotFoundException {
			long lastLine = 0L;
			
			for(Scanner fileScanner = new Scanner(file); fileScanner.hasNextLine(); ++lastLine) {
				fileScanner.nextLine();
			}
			
			return lastLine;
		}
		
		private List<String> readLinesFromIndex(File file, Long initalIndex) throws FileNotFoundException {
			long lastLine = 0L;
			Scanner fileScanner = new Scanner(file);
			List<String> lines = new ArrayList<>();
			
			while (fileScanner.hasNextLine()) {
				if (lastLine >= initalIndex) {
					lines.add(fileScanner.nextLine());
				} else {
					fileScanner.nextLine();
				}
				lastLine++;
			}
			
			return lines;
		}
		
		private List<Message> readAllMessages() throws IOException {
			return this.createChatMessageFromLines(Files.readAllLines(this.file.toPath(), (Charset.defaultCharset())));
		}
		
		@NotNull
		public File getFile() {
			return this.file;
		}
		
		public Chat(@NotNull File file) {
			super();
			this.file = file;
		}
		
		@Getter
		@Setter
		@AllArgsConstructor
		@NoArgsConstructor
		public static class Message {
			
			private String playerName;
			private String message;
			
			@Override
			public String toString() {
				return "Message(playerName=" + this.playerName + ", message=" + this.message + ")";
			}
			
			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				Message message1 = (Message) o;
				return playerName.equals(message1.playerName) &&
						message.equals(message1.message);
			}
			
			@Override
			public int hashCode() {
				return Objects.hash(playerName, message);
			}
		}
	}
	
}