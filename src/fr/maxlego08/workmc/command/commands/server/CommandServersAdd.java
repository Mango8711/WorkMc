package fr.maxlego08.workmc.command.commands.server;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.stats.StatsManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandServersAdd extends VCommand {

	public CommandServersAdd(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("add");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		if (args.length != 4)
			return CommandType.SYNTAX_ERROR;

		try {

			String name = args[1];
			String ip = args[2];
			int port = Integer.valueOf(args[3]);

			StatsManager.i.addServer(name, ip, port, getTextChannel());

		} catch (Exception e) {
			return CommandType.SYNTAX_ERROR;
		}

		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public String getSyntaxe() {
		return "!ss add <name> <ip> <port>";
	}

	@Override
	public String getDescription() {
		return null;
	}

}
