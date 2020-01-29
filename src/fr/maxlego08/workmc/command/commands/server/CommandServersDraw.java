package fr.maxlego08.workmc.command.commands.server;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.stats.StatsManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandServersDraw extends VCommand {

	public CommandServersDraw(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("draw");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		if (args.length != 2)
			return CommandType.SYNTAX_ERROR;
		
		StatsManager.i.draw(args[1], getTextChannel());
		
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public String getSyntaxe() {
		return "!ss draw <name>";
	}

	@Override
	public String getDescription() {
		return null;
	}

}
