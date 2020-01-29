package fr.maxlego08.workmc.command.commands.tickets;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandTicketClose extends VCommand {

	public CommandTicketClose(VCommand parent, boolean consoleCanExecute) {
		super(parent, consoleCanExecute, false);
		this.addCommand("close");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		message.delete().complete();
		main.getTicketManager().close(getUser(), getMessage());
		
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public String getSyntaxe() {
		return "!ticket close";
	}

	@Override
	public String getDescription() {
		return "Fermer un ticket";
	}

}
