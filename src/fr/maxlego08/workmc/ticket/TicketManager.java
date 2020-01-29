package fr.maxlego08.workmc.ticket;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.config.Config;
import fr.maxlego08.workmc.utils.Persist;
import fr.maxlego08.workmc.utils.Saveable;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class TicketManager implements Saveable {

	private static List<Ticket> tickets = new ArrayList<>();
	private static int lastId = 1;

	private static transient TicketManager i = new TicketManager();

	@Override
	public void save(Persist persit) {
		persit.save(i, "tickets");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, TicketManager.class, "tickets");
		System.out.println("Loaded " + tickets.size() + " tickets");
	}

	public void createTicket(Guild guild, User user, String title, String desc, TicketType type) {

		if (getTicketByUser(user) != null) {
			WorkMc.mc.getCommandManager().sendErrorMessage("Vous avez déjà un ticket de crée !",
					guild.getTextChannelById(Config.commandChannel));
			return;
		}

		int currentId = lastId++;
		Channel createChannel = guild.getCategoryById(Config.ticketCategory)
				.createTextChannel("ticket-" + getId(currentId))
				.addPermissionOverride(guild.getPublicRole(), 0, Permission.MESSAGE_READ.getRawValue())
				.addPermissionOverride(guild.getRoleById(Config.noBotRank), 0, Permission.MESSAGE_READ.getRawValue())
				.addPermissionOverride(guild.getMember(user), Permission.MESSAGE_READ.getRawValue(), 0).complete();

		Ticket ticket = new Ticket(currentId, user.getIdLong(), createChannel.getIdLong(), System.currentTimeMillis(),
				title, desc, type, TicketStatus.OPEN);

		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);

		builder.setDescription(":white_check_mark: Vous venez de créer un ticket - numéro du ticket: "
				+ getId(ticket.getId()) + " - " + ((TextChannel) createChannel).getAsMention());
		guild.getTextChannelById(Config.commandChannel).sendMessage(builder.build()).queue();
		;

		builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setFooter("WorkMC - 2019", null);
		builder.setDescription("Vous venez d'ouvrir un ticket pour le support.\nLes abus seront sanctionnés.\n");
		builder.setTimestamp(OffsetDateTime.now());
		builder.addField("Titre", title, false);
		builder.addField("Type du ticket", type.name(), false);

		((TextChannel) createChannel).sendMessage(builder.build()).complete();

		builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setFooter("WorkMC - 2019", null);
		builder.setDescription(
				"Le joueut " + user.getAsMention() + " vient de créer un ticket !" + ticket.getChannelId());
		builder.setTimestamp(OffsetDateTime.now());

		sendLog(guild, "Création du ticket " + ((TextChannel) createChannel).getAsMention() +" par " + user.getAsMention());
		
		tickets.add(ticket);
	}

	private Ticket getTicketByChannel(TextChannel channel){
		return tickets.stream().filter(
				ticket -> ticket.getChannelId() == channel.getIdLong() && ticket.getStatus().equals(TicketStatus.OPEN))
				.findFirst().orElse(null);
	}
	
	private Ticket getTicketByUser(User user) {
		return tickets.stream().filter(
				ticket -> ticket.getUserId() == user.getIdLong() && ticket.getStatus().equals(TicketStatus.OPEN))
				.findFirst().orElse(null);
	}

	public void close(User user, Message message) {
		if (getTicketByUser(user) == null && !message.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			message.delete().complete();
			WorkMc.mc.getCommandManager().sendErrorMessage("Vous n'avez pas de ticket crée !",
					message.getTextChannel());
			return;
		}
		if (message.getCategory().getIdLong() != Config.ticketCategory) {
			message.delete().complete();
			WorkMc.mc.getCommandManager().sendErrorMessage("Vous devez exécuter cette commande dans un ticket !",
					message.getTextChannel());
			return;
		}

		if (!message.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			Ticket ticket = getTicketByUser(user);
			ticket.setStatus(TicketStatus.CLOSE);
			message.getGuild().getTextChannelById(ticket.getChannelId()).putPermissionOverride(message.getMember())
					.setPermissions(0, Permission.MESSAGE_READ.getRawValue()).complete();
			sendLog(message.getGuild(), "Le joueur " + user.getAsMention() +" vient de fermet le ticket "
					+ message.getGuild().getTextChannelById(ticket.getChannelId()).getName() + ", vous pouvez le clore définitivement avec le !ticket close");
		} else {
			Ticket ticket = getTicketByChannel(message.getTextChannel());
			if (ticket != null)
				ticket.setStatus(TicketStatus.CLOSE);
			sendLog(message.getGuild(), "Suppresion du channel "
					+ message.getTextChannel().getName() + " par " +user.getAsMention()+ " !");
			message.getTextChannel().delete().complete();
		}
	}

	private void sendLog(Guild guild, String message) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setFooter("WorkMc - 2019 ", null);
		builder.setDescription(message);
		guild.getTextChannelById(Config.logTicket).sendMessage(builder.build()).complete();
	}

	private String getId(int lastId) {
		if (lastId < 10)
			return "000" + lastId;
		else if (lastId < 100)
			return "00" + lastId;
		else if (lastId < 1000)
			return "0" + lastId;
		else
			return "" + lastId;
	}

}
