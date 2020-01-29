package fr.maxlego08.workmc.listener;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.CommandManager;
import fr.maxlego08.workmc.config.Config;
import fr.maxlego08.workmc.messages.MessageManager;
import fr.maxlego08.workmc.player.Player;
import fr.maxlego08.workmc.utils.TimerBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

	private final CommandManager manager;

	public CommandListener(CommandManager manager) {
		this.manager = manager;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		onMessage(event);
		super.onMessageReceived(event);
	}

	@Override
	public void onMessageDelete(MessageDeleteEvent event) {
		MessageManager.i.deleteMessage(event.getMessageIdLong(), event.getTextChannel());
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Player player = WorkMc.mc.getPlayerManager().getPlayer(event.getUser().getIdLong());
		if (player.isBan() && !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			event.getUser().openPrivateChannel().complete()
					.sendMessage("Vous êtes bannis du serveur pendant "
							+ TimerBuilder.getStringTime((int) (player.getBan() - System.currentTimeMillis()) / 1000))
					.complete();
			event.getGuild().getController().kick(event.getMember()).complete();
		} else
			player.clearBan();

		try {
			WorkMc.mc.getAntiBotManager().sendPrivateCode(event.getUser());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void onMessage(MessageReceivedEvent event) {

		MessageManager.i.addMessage(event.getMessage());

		if (event.getAuthor().equals(event.getJDA().getSelfUser()))
			return;

		if (Config.muteAll && !event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			event.getMessage().delete().complete();
			event.getMember().getUser().openPrivateChannel().complete()
					.sendMessage("Le discord est actuellement mute !").complete();
		}

		if (WorkMc.mc.getAntiBotManager().hasUser(event.getAuthor())) {
			WorkMc.mc.getAntiBotManager().acceptPlayer(event.getAuthor(), event.getMessage());
			return;
		}

		Player player = WorkMc.mc.getPlayerManager().getPlayer(event.getAuthor().getIdLong());

		if (player.isMute() && !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			event.getAuthor().openPrivateChannel().complete()
					.sendMessage("Vous êtes mute du serveur pendant "
							+ TimerBuilder.getStringTime((int) (player.getMute() - System.currentTimeMillis()) / 1000))
					.complete();
			event.getMessage().delete().complete();
			return;
		} else
			player.clearMute();

		if (event.getChannel().getIdLong() == Config.projectChannel) {

			if (player.getLevel() < 5) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED);
				builder.setDescription(":x: " + "Vous devez être niveau **5** pour pouvoir poster un message ici !");
				Message messages = event.getChannel().sendMessage(builder.build()).complete();
				Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
					@Override
					public void run() {
						messages.delete().complete();
					}
				}, 10, TimeUnit.SECONDS);
				event.getMessage().delete().complete();
				return;
			}

			if (player.isProject() && !event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED);
				builder.setDescription(":x: " + "Vous pouvez reposter un message dans "
						+ ((TextChannel) event.getChannel()).getAsMention() + "  " + TimerBuilder
								.getStringTime((int) (player.getProjetMessage() - System.currentTimeMillis()) / 1000));
				Message messages = event.getChannel().sendMessage(builder.build()).complete();
				Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
					@Override
					public void run() {
						messages.delete().complete();
					}
				}, 10, TimeUnit.SECONDS);
				event.getMessage().delete().complete();
				return;
			} else
				player.clearProjet();

			player.setProjetMessage(System.currentTimeMillis() + 1000 * 60 * 60 * 24);

		}

		WorkMc.mc.getPlayerManager().addExperience(event.getMessage());

		String message = event.getMessage().getContentDisplay();

		if (message.startsWith("!")) {

			message = message.replaceFirst("!", "");

			String command = message.split(" ")[0];
			message = message.replaceFirst(command, "");
			String[] tableau = message.length() != 0 ? get(message.split(" ")) : new String[0];

			manager.onCommand(event.getMessage(), command, tableau);
		}
	}

	private String[] get(String[] tableau) {
		List<String> test = new ArrayList<>();
		for (int a = 1; a != tableau.length; a++) {
			test.add(tableau[a]);
		}
		return test.toArray(new String[0]);
	}

}
