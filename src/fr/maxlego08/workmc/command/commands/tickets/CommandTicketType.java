package fr.maxlego08.workmc.command.commands.tickets;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.ticket.TicketType;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandTicketType extends VCommand {

	public CommandTicketType(VCommand parent, boolean consoleCanExecute) {
		super(parent, consoleCanExecute, true);
		this.addCommand("type");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		sendEmbedMessage("Voici les types de ticket: " + TicketType.get());
		
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public String getSyntaxe() {
		return "!ticket type";
	}

	@Override
	public String getDescription() {
		return "Voir les types de ticket";
	}

}
