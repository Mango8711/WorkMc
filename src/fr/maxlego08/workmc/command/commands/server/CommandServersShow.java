package fr.maxlego08.workmc.command.commands.server;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.stats.StatsManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandServersShow extends VCommand {

	public CommandServersShow(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("show");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		StatsManager.i.show(getTextChannel());
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSyntaxe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
