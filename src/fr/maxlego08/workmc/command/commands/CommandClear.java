package fr.maxlego08.workmc.command.commands;

import java.util.List;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

public class CommandClear extends VCommand {

	public CommandClear(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("clear");
		this.setAsArgument(true);
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		if (args.length != 1)
			return CommandType.SYNTAX_ERROR;

		delete();
		MessageHistory history = new MessageHistory(getTextChannel());
		if (args[0].equalsIgnoreCase("all")) {
			try {
				while (true) {
					List<Message> msgs = history.retrievePast(1).complete();
					msgs.get(0).delete().queue();
				}
			} catch (Exception e) {
			}
		} else {
			try {
				int messages = Integer.valueOf(args[0]);
				List<Message> msgs = history.retrievePast(messages > 100 ? 100 : messages).complete();
				msgs.forEach(msg -> msg.delete().queue());
			} catch (NumberFormatException e) {
				return CommandType.SYNTAX_ERROR;
			}
		}

		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public String getSyntaxe() {
		return "!clear <1-100/all>";
	}

	@Override
	public String getDescription() {
		return "Supprimer les messages d'un channel";
	}

}
