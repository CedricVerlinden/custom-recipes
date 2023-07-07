package com.cedricverlinden.customrecipes.managers;

import com.cedricverlinden.customrecipes.utils.Chat;
import com.cedricverlinden.customrecipes.utils.Log;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemManager {

	private static final HashMap<String, ItemManager> items = new HashMap<>();
	private static final Log log = new Log();

	private final Material material;
	private final String displayName;
	private final List<String> lore;

	private final ItemStack itemStack;
	private final YamlConfiguration configuration;

	public ItemManager(Material material, String displayName, List<String> lore) {
		this.material = material;
		this.displayName = displayName;
		this.lore = lore;

		Chat chat = new Chat();

		itemStack = new ItemStack(material);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.displayName(chat.color(displayName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));

		List<Component> coloredLore = lore.stream()
				.map(line -> chat.color(line).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
				.collect(Collectors.toList());
		itemMeta.lore(coloredLore);

		itemStack.setItemMeta(itemMeta);

		String id = chat.stripString(displayName.trim().replace(" ", "-")).toLowerCase();

		FileManager fileManager = new FileManager("items", id);
		configuration = fileManager.getFile();

		configuration.set("material", material.toString());
		configuration.set("displayName", displayName);
		configuration.set("lore", lore);

		fileManager.save();
		items.put(id, this);
	}

	public static void registerItems() {
		List<File> files = FileManager.getFiles("items");
		if (files == null) {
			log.info("Could not find any items, not loading any.");
			return;
		}

		log.info("Loading items...");
		for (File file : files) {
			YamlConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);

			List<String> missingFields = Stream.of("material", "displayName", "lore")
					.filter(field -> !fileConfiguration.contains(field))
					.collect(Collectors.toList());

			if (!missingFields.isEmpty()) {
				String missingFieldsString = missingFields.size() > 1
						? String.join(", ", missingFields.subList(0, missingFields.size() - 1)) + (missingFields.size() > 2 ? "," : "") + " and " + missingFields.get(missingFields.size() - 1)
						: missingFields.get(0);

				log.error("Could not load item \"" + file.getName().substring(0, file.getName().length() - 4) + "\" because it is missing " + (missingFields.size() > 1 ? "the following fields: " : "the following field: ") + missingFieldsString);
				continue;
			}

			Material material = Material.getMaterial(Objects.requireNonNull(fileConfiguration.getString("material")));
			String displayName = fileConfiguration.getString("displayName");
			List<String> lore = fileConfiguration.getStringList("lore");

			new ItemManager(material, displayName, lore);
		}


		int itemSize = items.size();
		log.info("Loaded " + itemSize + " " + (itemSize == 1 ? "item" : "items") + ".");
	}

	public static HashMap<String, ItemManager> getItems() {
		return items;
	}

	public static ItemManager getItem(String id) {
		return items.get(id);
	}

	public YamlConfiguration getConfiguration() {
		return configuration;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public Material getMaterial() {
		return material;
	}

	public String getDisplayName() {
		return displayName;
	}

	public List<String> getLore() {
		return lore;
	}
}
