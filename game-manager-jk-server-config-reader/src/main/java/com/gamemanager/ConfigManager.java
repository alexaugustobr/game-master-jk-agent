package com.gamemanager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@AllArgsConstructor
@Getter
public class ConfigManager {
	
	private final Path file;
	
	public ConfigManager(String filePath) {
		this.file = Paths.get(filePath);
	}
	
	public List<String> readAllLines() throws IOException {
		return Files.readAllLines(this.file, Charset.defaultCharset());
	}
	
	public Optional<String> getConfig(ConfigKey configKey) throws IOException {
		for (String line : readAllLines()) {
			if (skipLine(line)) {
				continue;
			}
			
			Optional<Map.Entry<ConfigKey, String>> optionalEntry = extractValue(line);
			
			if (optionalEntry.isPresent() && optionalEntry.get().getKey().equals(configKey)) {
				return Optional.of(optionalEntry.get().getValue());
			}
		}
		
		return Optional.empty();
	}
	
	public Map<ConfigKey, String> parseAllLines() throws IOException {
		Map<ConfigKey, String> configKeys = new LinkedHashMap<>();
		for (String line : readAllLines()) {
			if (skipLine(line)) {
				continue;
			}
			
			Optional<Map.Entry<ConfigKey, String>> optionalEntry = extractValue(line);
			
			optionalEntry.ifPresent(entry -> configKeys.put(entry.getKey(), entry.getValue()));
		}
		return configKeys;
	}
	
	public Optional<Map.Entry<ConfigKey, String>> extractValue(String line) {
		ConfigKey[] configKeys = ConfigKey.values();
		
		for (ConfigKey configKey : configKeys) {
			//(seta|set) ([a-z_0-9A-Z0-9]+(?:[ \t]+[a-zA-Z0-9_]+)*) ("(.*?)")
			final String regex = "(seta|set) ([a-z_0-9A-Z0-9]+(?:[ \\t]+[a-zA-Z0-9_]+)*) (\"(.*?)\")";
			Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(line);
			
			while (matcher.find()) {
				if (matcher.groupCount() < 3) {
					continue;
				}
				
				try {
					String configName = matcher.group(2);
					String configValue = matcher.group(4);
					
					if (configName.equals(configKey.getName())) {
						
						Map.Entry<ConfigKey, String> entry = new AbstractMap.SimpleEntry<>(configKey, configValue);
						
						return Optional.of(entry);
					}
				} catch (Exception e) {
				
				}
			}
			
			
		}
		
		return Optional.empty();
	}
	
	private boolean skipLine(String line) {
		if (StringUtils.isBlank(line)) {
			return true;
		}
		return line.startsWith("//");
	}
	
	@AllArgsConstructor
	@Getter
	public enum ConfigKey {
		
		RCON_PASSWORD("rconpassword"),
		SV_HOSTNAME("sv_hostname");
		
		private final String name;
		
	}
}