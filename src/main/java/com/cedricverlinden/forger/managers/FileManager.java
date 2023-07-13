package com.cedricverlinden.forger.managers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import com.cedricverlinden.forger.Forger;
import com.cedricverlinden.forger.utils.Log;

public class FileManager {

	private static final File dataFolder = Forger.getInstance().getDataFolder();

	private final Log log;
	private final File file;
	private final YamlConfiguration editableFile;

	public FileManager(String path, String fileName) {
		log = new Log();
		file = new File(dataFolder + File.separator + path.replace("/", File.separator), fileName + ".yml");
		file.getParentFile().mkdirs();

		if (!(file.exists())) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		editableFile = YamlConfiguration.loadConfiguration(file);
	}

	public FileManager(String fileName) {
		this(dataFolder.getPath(), fileName);
	}

	public static List<File> getFiles(String folder) {
		String path = dataFolder + File.separator + folder.replace("/", File.separator);
		File[] files = new File(path).listFiles();
		if (files != null && files.length > 0) {
			return List.of(files);
		}

		return null;
	}

	public static File getFile(String folder, String fileName) {
		String path = dataFolder + File.separator + folder.replace("/", File.separator);
		File file = new File(path, fileName + ".yml");
		if (file.exists()) {
			return file;
		}

		return null;
	}

	public YamlConfiguration getFile() {
		return editableFile;
	}

	public void save() {
		try {
			editableFile.save(file);
		} catch (Exception e) {
			log.error("Could not save file " + file.getName() + ".");
		}
	}
}
