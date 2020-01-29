package fr.maxlego08.workmc.command.commands;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandStopConsole extends VCommand {

	public CommandStopConsole() {
		super(null, true, false);
		this.addCommand("stop");
		this.addCommand("end");
		this.addCommand("exit");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		main.setRunning(false);
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public String getSyntaxe() {
		return "!stop";
	}

	@Override
	public String getDescription() {
		return "Eteindre le bot";
	}

}
