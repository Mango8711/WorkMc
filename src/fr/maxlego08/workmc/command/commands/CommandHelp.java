package fr.maxlego08.workmc.command.commands;

import java.awt.Color;
import java.time.OffsetDateTime;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.CommandManager;
import fr.maxlego08.workmc.command.VCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandHelp extends VCommand {

	public CommandHelp(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("help");
		this.addCommand("aide");
		this.addCommand("?");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		CommandManager manager = main.getCommandManager();
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("WorkMC");
		builder.setColor(Color.getHSBColor(25, 25, 25));
		builder.setTimestamp(OffsetDateTime.now());
		builder.setFooter("WorkMC - 2019 - Développé par Maxlego08 ", null);
		for(VCommand command : manager.getCommands())
			if (!command.consoleCanExecute() && command.getParent() == null) {
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
		return "!help";
	}

	@Override
	public String getDescription() {
		return "Voir toutes les commandes";
	}

}
