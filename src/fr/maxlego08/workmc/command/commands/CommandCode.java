package fr.maxlego08.workmc.command.commands;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandCode extends VCommand {

	public CommandCode(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("code");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		main.getAntiBotManager().recreate(getUser(), message);
		delete();
		
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public String getSyntaxe() {
		return "!code";
	}

	@Override
	public String getDescription() {
		return "Générer un nouveau code pour l'antibot";
	}

}
