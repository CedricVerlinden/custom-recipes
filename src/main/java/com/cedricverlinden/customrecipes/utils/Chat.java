package com.cedricverlinden.customrecipes.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class Chat {

	private final MiniMessage miniMessage;

	public Chat() {
		miniMessage = MiniMessage.builder()
				.tags(TagResolver.builder()
						.resolver(StandardTags.color())
						.resolver(StandardTags.decorations())
						.resolver(StandardTags.rainbow())
						.resolver(StandardTags.gradient())
						.resolver(StandardTags.reset())
						.build()
				)
				.build();
	}

	public static String stripComponent(Component comp) {
		return PlainTextComponentSerializer.plainText().serialize(comp)
				.replace("[", "").replace("]", "");
	}

	public MiniMessage getMiniMessage() {
		return miniMessage;
	}

	public Component color(String str) {
		return miniMessage.deserialize(str);
	}

	public Component debug(Component comp) {
		return color("<dark_green>[DEBUG]</dark_green> <green>" + miniMessage.serialize(comp));
	}

	public Component debug(String str) {
		return debug(miniMessage.deserialize(str));
	}

	public Component warning(Component comp) {
		return color("<gold>[WARNING]</gold> <yellow>" + miniMessage.serialize(comp));
	}

	public Component warning(String str) {
		return warning(Component.text(str));
	}

	public Component error(Component comp) {
		return color("<dark_red>[ERROR]</dark_red> <red>" + miniMessage.serialize(comp));
	}

	public Component error(String str) {
		return error(Component.text(str));
	}

	public String stripString(String str) {
		return miniMessage.stripTags(str, TagResolver.resolver(StandardTags.color(), StandardTags.decorations()));
	}
}
