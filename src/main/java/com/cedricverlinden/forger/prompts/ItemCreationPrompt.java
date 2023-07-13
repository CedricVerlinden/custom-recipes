package com.cedricverlinden.forger.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public final class ItemCreationPrompt implements Prompt {

	@Override
	public String getPromptText(ConversationContext context) {
		return "What is the name of the item?";
	}

	@Override
	public boolean blocksForInput(ConversationContext context) {
		return true;
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		context.getForWhom().sendRawMessage("The item name has been set to: " + input);
		context.setSessionData("displayName", input);

		return new LorePrompt();
	}

	class LorePrompt implements Prompt {

		@Override
		public String getPromptText(ConversationContext context) {
			return "What is the lore of the item?";
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
