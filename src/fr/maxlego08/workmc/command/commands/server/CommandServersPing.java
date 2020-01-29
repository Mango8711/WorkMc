package fr.maxlego08.workmc.command.commands.server;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.stats.StatsManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandServersPing extends VCommand {

	public CommandServersPing(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("ping");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		if (StatsManager.i.ping(getTextChannel()))
			message.delete().complete();
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
