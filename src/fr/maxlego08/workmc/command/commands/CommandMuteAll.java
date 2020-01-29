package fr.maxlego08.workmc.command.commands;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.config.Config;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandMuteAll extends VCommand {

	public CommandMuteAll(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("muteall");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		Config.muteAll = !Config.muteAll;
		
		sendEmbedMessage(":white_check_mark: Vous venez " + (Config.muteAll ? "de mute tout le discord" : "d'unmute tout le discord"));
		
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return Permission.MESSAGE_MANAGE;
	}

	@Override
	public String getSyntaxe() {
		return "!muteall";
	}

	@Override
	public String getDescription() {
		return "Mute tous le discord";
	}

}
