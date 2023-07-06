package com.cedricverlinden.customrecipes.managers;

import com.cedricverlinden.customrecipes.CustomRecipes;
import com.cedricverlinden.customrecipes.utils.Log;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class FileManager {

	private static final File dataFolder = CustomRecipes.getInstance().getDataFolder();

	private final Log log;
	private final File file;
	private final YamlConfiguration editableFile;

	public FileManager(String path, String fileName) {
		log = new Log();
		file = new File(dataFolder + File.separator + path.replace("/", File.separator), fileName + ".yml");
		file.getParentFile().mkdirs();

		if (!(file.exists())) {
			CustomRecipes.getInstance().saveResource(path.replace("/", File.separator) + File.separator + fileName, false);
		}

		editableFile = YamlConfiguration.loadConfiguration(file);
	}

	public FileManager(String fileName) {
		this(dataFolder.getPath(), fileName);
	}

	public static List<File> getFiles(String folder) {
		String path = dataFolder + File.separator + folder.replace("/", File.separator);
		File[] files = new File(path).listFiles();
		if (files != null) {
			return List.of(files);
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
