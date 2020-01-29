package fr.maxlego08.workmc.command;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand.CommandType;
import fr.maxlego08.workmc.command.commands.CommandBan;
import fr.maxlego08.workmc.command.commands.CommandBlacklist;
import fr.maxlego08.workmc.command.commands.CommandClassement;
import fr.maxlego08.workmc.command.commands.CommandClear;
import fr.maxlego08.workmc.command.commands.CommandCode;
import fr.maxlego08.workmc.command.commands.CommandHelp;
import fr.maxlego08.workmc.command.commands.CommandMute;
import fr.maxlego08.workmc.command.commands.CommandMuteAll;
import fr.maxlego08.workmc.command.commands.CommandPing;
import fr.maxlego08.workmc.command.commands.CommandRank;
import fr.maxlego08.workmc.command.commands.CommandReact;
import fr.maxlego08.workmc.command.commands.CommandServer;
import fr.maxlego08.workmc.command.commands.CommandStopConsole;
import fr.maxlego08.workmc.command.commands.CommandUnBan;
import fr.maxlego08.workmc.command.commands.CommandUnMute;
import fr.maxlego08.workmc.command.commands.giveaway.CommandGiveAway;
import fr.maxlego08.workmc.command.commands.giveaway.CommandGiveAwayPerform;
import fr.maxlego08.workmc.command.commands.server.CommandServers;
import fr.maxlego08.workmc.command.commands.server.CommandServersAdd;
import fr.maxlego08.workmc.command.commands.server.CommandServersDraw;
import fr.maxlego08.workmc.command.commands.server.CommandServersPing;
import fr.maxlego08.workmc.command.commands.server.CommandServersRemove;
import fr.maxlego08.workmc.command.commands.server.CommandServersShow;
import fr.maxlego08.workmc.command.commands.tickets.CommandTicket;
import fr.maxlego08.workmc.command.commands.tickets.CommandTicketClose;
import fr.maxlego08.workmc.command.commands.tickets.CommandTicketCreate;
import fr.maxlego08.workmc.command.commands.tickets.CommandTicketType;
import fr.maxlego08.workmc.config.Config;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class CommandManager {

	private final WorkMc main;

	private List<VCommand> commands = new ArrayList<>();

	public CommandManager(WorkMc main) {
		this.main = main;
		this.register();
	}

	/*
	 * Register commands
	 */

	private void register() {

		/**
		 * @param VCOMMAND
		 *            -> NO WORK ON THIS VERSION
		 * @param BOOLEAN
		 *            -> Use by consol e
		 * @param BOOLEAN
		 *            -> Use in every channel
		 */

		addCommand(new CommandHelp(null, false, true));
		addCommand(new CommandStopConsole());

		VCommand giveaway = addCommand(new CommandGiveAway(null, false, true));
		addCommand(new CommandGiveAwayPerform(giveaway, false, true));

		addCommand(new CommandMute(null, false, false));
		addCommand(new CommandUnMute(null, false, false));

		addCommand(new CommandBan(null, false, false));
		addCommand(new CommandUnBan(null, false, false));

		VCommand ticket = addCommand(new CommandTicket(null, false));
		addCommand(new CommandTicketCreate(ticket, false, false));
		addCommand(new CommandTicketClose(ticket, false));
		addCommand(new CommandTicketType(ticket, false));

		addCommand(new CommandCode(null, false, false));
		addCommand(new CommandBlacklist(null, false, true));
		addCommand(new CommandClear(null, false, false));
		addCommand(new CommandServer(null, false));
		addCommand(new CommandMuteAll(null, false, true));
		
		addCommand(new CommandRank(null, false));
		addCommand(new CommandClassement(null, false));
		addCommand(new CommandReact(null, false, false));
		
		addCommand(new CommandPing(null, false, false));
		
		VCommand server = addCommand(new CommandServers(null, false, true));
		addCommand(new CommandServersPing(server, false, true));
		addCommand(new CommandServersShow(server, false, true));
		addCommand(new CommandServersAdd(server, false, true));
		addCommand(new CommandServersRemove(server, false, true));
		addCommand(new CommandServersDraw(server, false, true));
		
	}

	public List<VCommand> getCommands() {
		return commands;
	}

	private VCommand addCommand(VCommand command) {
		commands.add(command);
		return command;
	}

	/*
	 * Execute commande
	 */

	public void onCommand(Message message, String command, String[] args) {

		for (VCommand zCommand : commands) {
			if (zCommand.getSubCommands().contains(command.toLowerCase())
					&& (args.length == 0 || zCommand.asArgument())) {
				processCommand(zCommand, message, command, args);
				return;
			} else if (args.length >= 1 && zCommand.getParent() != null
					&& zCommand.getParent().getSubCommands().contains(command.toLowerCase())) {
				String cmd = args[0].toLowerCase();
				if (zCommand.getSubCommands().contains(cmd)){
					processCommand(zCommand, message, command, args);
					return;
				}
			}
		}
		if (message != null){
			message.delete().complete();
			sendErrorMessage("La commande **" + message.getContentRaw() +"** n'existe pas !", message.getTextChannel());
		}else 
			System.out.println("Cette commande n'existe pas !");

	}

	/*
	 * Exécuter la commande
	 */

	private void processCommand(VCommand zCommand, Message message, String command, String[] args) {

		if (zCommand.consoleCanExecute() && message == null) {

			/* On exécute la console en mode console */

			zCommand.setMessage(null);
			zCommand.perform(null, main, args);

			return;
		} else if (message == null && !zCommand.consoleCanExecute()) {
			System.out.println("Vous ne pouvez pas exécuter cette commande !");
			return;
		}

		/* On exécute la console en mode joueur */

		if (zCommand.onlyUseInCommandChannel())
			if (!canExecuteHere(message))
				return;

		User user = message.getAuthor();
		zCommand.setMessage(message);

		if (zCommand.getPermission() == null
				|| message.getGuild().getMember(user).hasPermission(zCommand.getPermission())) {

			if (zCommand.perform(message, main, args).equals(CommandType.SYNTAX_ERROR)) {

				message.delete().complete();
				sendErrorMessage("Vous devez utiliser la commande comme ceci: " + zCommand.getSyntaxe(),
						message.getTextChannel());

			} else
				logCommand(message, args, command);

			return;
		}

		message.delete().complete();
		sendErrorMessage(message.getAuthor().getAsMention() + " Vous n'avez pas la permission !",
				message.getTextChannel());

	}

	/*
	 * Send message error who remove after 3 seconds
	 */

	public void sendErrorMessage(String message, TextChannel channel) {

		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.RED);
		builder.setDescription(":x: " + message);
		Message messages = channel.sendMessage(builder.build()).complete();
		Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
			@Override
			public void run() {
				messages.delete().complete();
			}
		}, 10, TimeUnit.SECONDS);
	}

	public boolean canExecuteHere(Message message) {
		if (message.getTextChannel().getIdLong() != Config.commandChannel) {
			message.delete().complete();
			sendErrorMessage(
					message.getAuthor().getAsMention() + " Vous devez utiliser cette command dans "
							+ message.getGuild().getTextChannelById(Config.commandChannel).getAsMention(),
					message.getTextChannel());
			return false;
		}
		return true;
	}

	private void logCommand(Message message, String[] args, String command) {

		TextChannel channel = message.getGuild().getTextChannelById(Config.logChannel);

		EmbedBuilder builder = new EmbedBuilder();

		builder.setColor(Color.GREEN);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setTitle(message.getAuthor().getName());
		builder.setFooter("WorkMc - 2019 ", null);
		builder.setDescription("Utilisation de la commande: **!" + command + "** "
				+ (getStringArgs(args) != null ? getStringArgs(args) : "") + " dans le channel "
				+ message.getTextChannel().getAsMention());

		channel.sendMessage(builder.build()).complete();

	}
	
	private String getStringArgs(String[] args) {

		if (args.length != 0) {
			StringBuilder builder = new StringBuilder();
			for (String s : args)
				builder.append(s + " ");
			return "avec les arguments: **" + builder.toString() + "**";
		}
		return null;

	}
}
