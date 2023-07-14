package com.cedricverlinden.forger;

import java.util.Objects;

import org.bukkit.plugin.java.JavaPlugin;

import com.cedricverlinden.forger.commands.DebugCommand;
import com.cedricverlinden.forger.commands.ForgerCommand;
import com.cedricverlinden.forger.commands.ItemCommand;
import com.cedricverlinden.forger.commands.RecipeCommand;
import com.cedricverlinden.forger.listeners.InventoryListener;
import com.cedricverlinden.forger.listeners.PlayerListener;
import com.cedricverlinden.forger.managers.FileManager;
import com.cedricverlinden.forger.managers.ItemManager;
import com.cedricverlinden.forger.managers.RecipeManager;
import com.cedricverlinden.forger.utils.Log;

public final class Forger extends JavaPlugin {

	private static Forger instance;
	public static Log log;

	@Override
	public void onEnable() {
		instance = this;
		log = new Log();

		if (FileManager.getFiles("items") == null || FileManager.getFiles("recipes") == null) {
			log.info("No items or recipes found, creating default ones...");
			saveResource("items/test-item.yml", true);
			saveResource("recipes/test-recipe.yml", true);
		}

		ItemManager.registerItems();
		RecipeManager.registerRecipes();
		registerCommands();
		registerListeners();
	}

	@Override
	public void onDisable() {
		log.info("Disabling Forger, bye!");
	}

	private void registerCommands() {
		log.info("Registering commands...");
		Objects.requireNonNull(getCommand("forger")).setExecutor(new ForgerCommand());
		Objects.requireNonNull(getCommand("debug")).setExecutor(new DebugCommand());
		Objects.requireNonNull(getCommand("item")).setExecutor(new ItemCommand());
		Objects.requireNonNull(getCommand("recipe")).setExecutor(new RecipeCommand());
		log.info("Commands registered.");
	}

	private void registerListeners() {
		log.info("Registering listeners...");
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		log.info("Listeners registered.");
	}

	public static Forger getInstance() {
		return instance;
	}
}
