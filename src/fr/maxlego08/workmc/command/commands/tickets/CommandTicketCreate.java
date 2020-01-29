package fr.maxlego08.workmc.command.commands.tickets;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.ticket.TicketType;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandTicketCreate extends VCommand {

	public CommandTicketCreate(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("create");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		if (args.length < 4)
			return CommandType.SYNTAX_ERROR;
		TicketType type = TicketType.get(args[1]);
		if (type == null)
			return CommandType.SYNTAX_ERROR;

		String title = "";
		for (int a = 2; a != args.length; a++) {
			title += args[a]+ " ";
		}
		delete();
		main.getTicketManager().createTicket(getGuild(), getUser(), title, "", type);

		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public String getSyntaxe() {
		return "!ticket create <type> <title>";
	}

	@Override
	public String getDescription() {
		return "Créer un ticket";
	}

}
