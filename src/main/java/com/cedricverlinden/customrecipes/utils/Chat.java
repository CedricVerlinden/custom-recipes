package com.cedricverlinden.customrecipes.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class Chat {

	private final MiniMessage miniMessage;
	private final List<TagResolver> tagResolvers = new ArrayList<>(
		Arrays.asList(
			StandardTags.color(),
			StandardTags.decorations(),
			StandardTags.rainbow(),
			StandardTags.gradient(),
			StandardTags.reset()
	));

	public Chat() {
		miniMessage = MiniMessage.builder()
				.tags(TagResolver.builder()
						.resolvers(tagResolvers)
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
		return warning(miniMessage.deserialize(str));
	}

	public Component error(Component comp) {
		return color("<dark_red>[ERROR]</dark_red> <red>" + miniMessage.serialize(comp));
	}

	public Component error(String str) {
		return error(miniMessage.deserialize(str));
	}

	public String stripString(String str) {
		return miniMessage.stripTags(str, TagResolver.resolver(tagResolvers));
	}
}
