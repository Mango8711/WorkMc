package fr.maxlego08.workmc.command.commands.giveaway;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandGiveAwayPerform extends VCommand {

	public CommandGiveAwayPerform(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("create");
		this.addCommand("stop");
		this.addCommand("forceend");
		this.addCommand("test");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		main.getGiveAwayManager().performCommand(args, message, getTextChannel());
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public String getSyntaxe() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

}
