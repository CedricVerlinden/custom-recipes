package com.cedricverlinden.forger.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.cedricverlinden.forger.utils.Chat;

public final class ItemCreationPrompt implements Prompt {

	Chat chat = new Chat();

	@Override
	public String getPromptText(ConversationContext context) {
		Player player = (Player) context.getForWhom();
		player.sendMessage(chat.color("<dark_gray>[<red>ITEM CREATION<dark_gray>]<white> What name should be given to the item?"));
		return "";
	}

	@Override
	public boolean blocksForInput(ConversationContext context) {
		return true;
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		context.setSessionData("displayName", input);
		return new LorePrompt();
	}

	class LorePrompt implements Prompt {

		@Override
		public String getPromptText(ConversationContext context) {
			Player player = (Player) context.getForWhom();
			player.sendMessage(chat.color("<dark_gray>[<red>ITEM CREATION<dark_gray>]<white> What lore should be given to the item? (seperate lines with <newline>)"));
			return "";
		}

		@Override
		public boolean blocksForInput(ConversationContext context) {
			return true;
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String input) {
			context.setSessionData("lore", input);
			return END_OF_CONVERSATION;
		}
	}
}
