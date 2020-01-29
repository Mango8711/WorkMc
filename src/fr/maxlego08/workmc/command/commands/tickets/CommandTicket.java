package fr.maxlego08.workmc.command.commands.tickets;

import java.awt.Color;
import java.time.OffsetDateTime;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.CommandManager;
import fr.maxlego08.workmc.command.VCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandTicket extends VCommand {

	public CommandTicket(VCommand object, boolean b) {
		super(object, b, true);
		this.addCommand("ticket");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		CommandManager manager = main.getCommandManager();
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("WorkMc");
		builder.setColor(Color.getHSBColor(25, 25, 25));
		builder.setTimestamp(OffsetDateTime.now());
		builder.setFooter("WorkMc - 2019 ", null);
		for(VCommand command : manager.getCommands())
			if (!command.consoleCanExecute() && command.getParent() != null && command.getParent().getSubCommands().contains("ticket")){
				command.setMessage(message);
				builder.addField(command.getSyntaxe(), command.getDescription(), false);
			}
		
		getTextChannel().sendMessage(builder.build()).complete();
		
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public String getSyntaxe() {
		return "!ticket";
	}

	@Override
	public String getDescription() {
		return "Créer/Supprimer des tickets";
	}

}
