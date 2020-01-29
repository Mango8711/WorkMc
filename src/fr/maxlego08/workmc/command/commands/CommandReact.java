package fr.maxlego08.workmc.command.commands;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.utils.TextEmoji;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandReact extends VCommand {

	public CommandReact(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("react");
		this.setAsArgument(true);
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		
		if (args.length != 1) return CommandType.SYNTAX_ERROR;
		
		delete();
		
		Message last = getTextChannel().getHistory().retrievePast(2).complete().get(0);
		TextEmoji[] reactions = TextEmoji.toEmoji(String.join("", args));
		
		for(TextEmoji emoji : reactions) last.addReaction(emoji.getUnicode()).queue();
		
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public String getSyntaxe() {
		return "!react <votre mot>";
	}

	@Override
	public String getDescription() {
		return "Ecrit votre reaction en emoji (ne pas mettre plusieurs fois le même caractère !)";
	}
	
}
