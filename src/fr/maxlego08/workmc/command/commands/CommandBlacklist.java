package fr.maxlego08.workmc.command.commands;

import java.awt.Color;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.config.Config;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandBlacklist extends VCommand {

	public CommandBlacklist(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("bl");
		this.addCommand("blacklist");
		this.setAsArgument(true);
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		if (args.length <= 4)
			return CommandType.SYNTAX_ERROR;

		String m = "";
		for (String z : args)
			m += z + " ";
		String[] s = m.split("'");
		if (s.length != 7)
			return CommandType.SYNTAX_ERROR;

		String pseudo = s[1];
		String id = s[3];
		if (id.length() != 18){
			main.getCommandManager().sendErrorMessage("L'id de l'utilisateur doit avoir 18 caractères !", getTextChannel());
			return CommandType.SUCCESS;
		}
		String reason = s[5];
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setDescription(pseudo +" ("+id+") -> " + reason);
		
		getGuild().getTextChannelById(Config.blacklistChannel).sendMessage(builder.build()).complete();
		
		delete();
		
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public String getSyntaxe() {
		return "!blacklist add '<pseudo>' '<id>' '<raison>'";
	}

	@Override
	public String getDescription() {
		return "Ajouter une personne dans la liste des blacklist" + "";
	}

}
