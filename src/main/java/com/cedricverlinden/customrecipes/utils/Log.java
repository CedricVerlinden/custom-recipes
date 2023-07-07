package com.cedricverlinden.customrecipes.utils;

import org.bukkit.Bukkit;

public class Log {

	private final Chat chat;

	public Log() {
		chat = new Chat();
	}

	public void log(String str) {
		Bukkit.getConsoleSender().sendMessage(chat.getMiniMessage().deserialize(str));
	}

	public void debug(String str) {
		log("<dark_green>[DEBUG] <green>" + str);
	}

	public void info(String str) {
		log("<blue>[INFO]</blue> <aqua>" + str);
	}

	public void warning(String str) {
		log("<gold>[WARNING]</gold> <yellow>" + str);
	}

	public void error(String str) {
		log("<dark_red>[ERROR]</dark_red> <red>" + str);
	}
}
