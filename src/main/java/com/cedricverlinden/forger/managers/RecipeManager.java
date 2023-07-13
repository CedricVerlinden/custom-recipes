package com.cedricverlinden.forger.managers;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import com.cedricverlinden.forger.Forger;
import com.cedricverlinden.forger.utils.Log;

public class RecipeManager {

	private static final Log log = new Log();

	private final String recipeName;
	private final List<List<String>> itemStacks;
	private final String result;

	public RecipeManager(String recipeName, List<List<String>> itemStacks, String result) {
		this.recipeName = recipeName;
		this.itemStacks = itemStacks;
		this.result = result;

		if (ItemManager.getItem(result) == null && Material.getMaterial(result) == null) {
			log.error("Item " + result + " does not exist.");
			return;
		}

		ItemStack resultMaterial = (Material.getMaterial(result) == null)
				? ItemManager.getItem(result).getItemStack()
				: new ItemStack(Objects.requireNonNull(Material.getMaterial(result)));

		FileManager fileManager = new FileManager("recipes", recipeName);
		YamlConfiguration configuration = fileManager.getFile();
		configuration.set("result", result);

		NamespacedKey key = new NamespacedKey(Forger.getInstance(), recipeName);
		ShapedRecipe recipe = new ShapedRecipe(key, resultMaterial);
		recipe.shape("ABC", "DEF", "GHI");

		char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };
		int letterIndex = 0;

		for (int i = 0; i < 3; i++) {
			List<String> row = itemStacks.get(i);
			configuration.set("row_" + (i + 1), row);
			for (int j = 0; j < 3; j++) {
				String item = row.get(j);
				if (ItemManager.getItem(item) == null && Material.getMaterial(item) == null) {
					continue;
				}

				ItemStack itemStack = (Material.getMaterial(item) == null)
						? ItemManager.getItem(item).getItemStack()
						: new ItemStack(Objects.requireNonNull(Material.getMaterial(item)));

				recipe.setIngredient(letters[letterIndex], new RecipeChoice.ExactChoice(itemStack));
				letterIndex++;

			}
		}

		Bukkit.addRecipe(recipe);
		fileManager.save();
	}

	public static void registerRecipes() {
		List<File> files = FileManager.getFiles("recipes");
		if (files == null) {
			log.info("Could not find any recipes, not loading any.");
			return;
		}

		int recipeCount = 0;
		log.info("Loading recipes...");
		for (File file : files) {
			YamlConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);

			List<String> missingFields = Stream.of("result", "row_1", "row_2", "row_3")
					.filter(field -> !fileConfiguration.contains(field))
					.collect(Collectors.toList());

			String fileName = file.getName().substring(0, file.getName().length() - 4);
			if (!missingFields.isEmpty()) {
				String missingFieldsString = missingFields.size() > 1
						? String.join(", ", missingFields.subList(0, missingFields.size() - 1))
								+ (missingFields.size() > 2 ? "," : "") + " and "
								+ missingFields.get(missingFields.size() - 1)
						: missingFields.get(0);

				log.error("Could not load recipe \"" + fileName + "\" because it is missing "
						+ (missingFields.size() > 1 ? "the following fields: " : "the following field: ")
						+ missingFieldsString);
				continue;
			}

			List<List<String>> itemStacks = Stream.of("row_1", "row_2", "row_3")
					.map(fileConfiguration::getStringList)
					.toList();
			String result = fileConfiguration.getString("result");

			recipeCount++;
			new RecipeManager(fileName, itemStacks, result);
		}

		log.info("Loaded " + recipeCount + " " + (recipeCount == 1 ? "recipe" : "recipes") + ".");
	}

	public static List<String> getRecipes() {
		return FileManager.getFiles("recipes").stream()
				.map(file -> file.getName().substring(0, file.getName().length() - 4))
				.toList();
	}

	public String getRecipeName() {
		return recipeName;
	}

	public List<List<String>> getItemStacks() {
		return itemStacks;
	}

	public String getResult() {
		return result;
	}
}
