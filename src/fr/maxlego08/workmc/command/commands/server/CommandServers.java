package fr.maxlego08.workmc.command.commands.server;

import java.awt.Color;
import java.time.OffsetDateTime;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandServers extends VCommand {

	public CommandServers(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("ss");
		this.addCommand("servers");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		EmbedBuilder builder = new EmbedBuilder();

		builder.setColor(Color.GREEN);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setFooter("WorkMc - 2019 ", null);

		builder.addField("!ss add <name> <ip> <port>", "Ajouter un serveur", false);
		builder.addField("!ss show", "Voir les serveurs", false);
		builder.addField("!ss ping", "Ping tous les serveurs", false);
		builder.addField("!ss remove <name>", "Retirer un serveur", false);

		getTextChannel().sendMessage(builder.build()).complete();
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public String getSyntaxe() {
		return "!servers";
	}

	@Override
	public String getDescription() {
		return "Voir la liste des commandes";
	}

}
