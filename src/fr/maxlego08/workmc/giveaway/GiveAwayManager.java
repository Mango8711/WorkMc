package fr.maxlego08.workmc.giveaway;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.config.Config;
import fr.maxlego08.workmc.giveaway.ga.GiveAway;
import fr.maxlego08.workmc.utils.Persist;
import fr.maxlego08.workmc.utils.Saveable;
import fr.maxlego08.workmc.utils.TimerBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class GiveAwayManager implements Saveable {

	private static transient List<GiveAway> giveaways = new ArrayList<>();

	public GiveAwayManager() {
		this.taskGiveAway();
	}

	public void sendHelp(TextChannel channel) {

		EmbedBuilder builder = new EmbedBuilder();

		builder.setColor(Color.GREEN);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setFooter("WorkMc - 2019 ", null);

		builder.addField("!giveaway create <temps> <nombre de gagant> <prix à gagner>",
				"Créer un nouveau giveaway !\n" + "Format du temps: s = secondes, m = minutes, h = heure, d = jour"
						+ "\n" + "Exemple: !giveaway create 5h 3 x3 plastrons en diams",
				false);

		builder.addField("!giveaway end <id du message>", "Arrêter un giveaway", false);

		channel.sendMessage(builder.build()).complete();

	}

	public void performCommand(String[] args, Message message, TextChannel channel) {

		if (args[0].equalsIgnoreCase("create")) {

			int time = getTimeSecond(args[1]);
			Date data = getDate(time);
			int winner = 0;
			long remaningTime = System.currentTimeMillis() + (time * 1000l);

			try {

				winner = Integer.valueOf(args[2]);

			} catch (Exception e) {
				message.delete().complete();
				WorkMc.mc.getCommandManager().sendErrorMessage("'" + args[2] + "' n'est pas un nombre !", channel);
				return;
			}

			StringBuilder str = new StringBuilder();
			for (int a = 3; a != args.length; a++) {
				str.append(args[a]);
				str.append(" ");
			}

			OffsetDateTime scheduled = data.toInstant().atOffset(ZoneOffset.UTC);

			EmbedBuilder builder = new EmbedBuilder();

			builder.setColor(Color.GREEN);
			builder.setTimestamp(scheduled);
			builder.setTitle("It's giveaway time !");

			builder.setDescription(
					"A gagner: **x" + winner + " " + str.toString() + "**" + "\n" + "\n" + "Pour participer clique sur l'émojie !\n"
							+ "Il vous reste encore: " + TimerBuilder.getFormatLongDays(time * 1000));

			builder.setFooter("Se termine le", null);

			Message giveAwayMessage = message.getJDA().getTextChannelById(Config.giveawayChannel)
					.sendMessage(builder.build()).complete();

			giveAwayMessage.addReaction(giveAwayMessage.getGuild().getEmoteById(Config.giveawayEmoji)).complete();

			GiveAway giveAway = new GiveAway(Config.giveawayChannel, winner, remaningTime, str.toString());
			giveAway.setMessageId(giveAwayMessage.getIdLong());
			giveAway.setMessage(giveAwayMessage);

			giveaways.add(giveAway);

		} else if (args[0].equalsIgnoreCase("end")) {

			/* WIP */

		} else if (args[0].equalsIgnoreCase("forceend")) {
			try {
				Message giveAwayMessage = message.getJDA().getTextChannelById(Config.giveawayChannel).getMessageById(args[1])
						.complete();
				if (giveAwayMessage == null)
					return;
				int how = Integer.valueOf(args[2]);
				this.forceEnd(how, giveAwayMessage);
			} catch (NumberFormatException e) {
				return;
			}
		} else if (args[0].equalsIgnoreCase("test")) {
			Message giveAwayMessage = message.getJDA().getTextChannelById(Config.giveawayChannel).getMessageById(args[1])
					.complete();
			if (giveAwayMessage == null)
				return;
			
			List<User> users = new ArrayList<>();

			for (User user : giveAwayMessage.getReactions().get(0)
					.getUsers())
				if (user.getIdLong() != Config.botId)
					users.add(user);
			users.forEach(user -> {
				System.out.println(user.isBot() + " -- " + user.isFake());
				if (user.isFake()){
					System.out.println(user.toString());
				}
			});
			
		}

	}

	private int getTimeSecond(String time) {
		try {
			if (time.contains("d"))
				return Integer.valueOf(time.replace("d", "")) * 60 * 60 * 24;
			else if (time.contains("m"))
				return Integer.valueOf(time.replace("m", "")) * 60;
			else if (time.contains("s"))
				return Integer.valueOf(time.replace("s", ""));
			else if (time.contains("h"))
				return Integer.valueOf(time.replace("h", "")) * 60 * 60;
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	private Date getDate(long l) {
		Date date = new Date();
		date.setTime(date.getTime() + (l * 1000));
		return date;
	}

	private void taskGiveAway() {

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				/*
				 * On for tous les giveaway de créé
				 */

				for (Iterator<GiveAway> it = giveaways.iterator(); it.hasNext();) {

					GiveAway giveAway = it.next();

					try {
						Message message = giveAway.getMessage();
						if (giveAway.isFinish()) {
							it.remove();
							end(giveAway, message);
						} else
							message.editMessage(getEmbed(giveAway).build()).queue();

					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
		};

		/**
		 * Toute les 5 seconde la méthode run s'active
		 */

		timer.schedule(task, 0, 5 * 1000);

	}

	private EmbedBuilder getEmbed(GiveAway giveAway) {

		EmbedBuilder builder = new EmbedBuilder();

		int time = giveAway.getRealTime();

		OffsetDateTime scheduled = getDate(time).toInstant().atOffset(ZoneOffset.UTC);

		builder.setColor(Color.GREEN);
		builder.setTimestamp(scheduled);
		builder.setTitle("It's giveaway time !");

		builder.setDescription("A gagner: **x" + giveAway.getWinner() + " " + giveAway.getPrize() + "**" + "\n" + "\n"
				+ "Pour participer clique sur :tada: ! \n" + "Il vous reste encore: "
				+ TimerBuilder.getStringTime((int) time));

		builder.setFooter("Se termine le ", null);

		return builder;

	}

	private EmbedBuilder getEmbedFinish(GiveAway giveAway) {

		EmbedBuilder builder = new EmbedBuilder();

		int time = giveAway.getRealTime();

		OffsetDateTime scheduled = getDate(time).toInstant().atOffset(ZoneOffset.UTC);

		builder.setColor(Color.GREEN);
		builder.setTimestamp(scheduled);
		builder.setTitle("It's Giveaway time !");

		builder.setDescription("A gagner: **x" + giveAway.getWinner() + " " + giveAway.getPrize() + "**" + "\n" + "\n"
				+ "Le GiveAway est terminé ! Félicitation au(x) gagnant(s) !");

		builder.setFooter("Terminé le ", null);

		return builder;

	}

	private List<User> winners = new ArrayList<>();
	private Random random = new Random();

	private void end(GiveAway giveAway, Message message) {

		winners.clear();

		message.editMessage(getEmbedFinish(giveAway).build()).complete();

		List<User> users = new ArrayList<>();

		for (User user : message.getTextChannel().getMessageById(message.getIdLong()).complete().getReactions().get(0)
				.getUsers())
			if (user.getIdLong() != Config.botId)
				users.add(user);

		int how = giveAway.getWinner();
		if (how > users.size())
			how = users.size();

		for (int a = 0; a != how; a++) {

			winners.add(getRandomUser(users));

		}

		StringBuilder str = new StringBuilder();
		for (User winner : winners)
			str.append(winner.getAsMention() + ", ");

		EmbedBuilder builder = new EmbedBuilder();

		builder.setColor(Color.GREEN);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setTitle("It's Giveaway Result time !");
		builder.setFooter("WorkMc - 2019 ", null);

		builder.setDescription((winners.size() == 1 ? "Gagnant: " : "Gagnants: ") + str.toString());

		message.getTextChannel().sendMessage(builder.build()).complete();

	}

	private User getRandomUser(List<User> users) {
		User user = users.get(random.nextInt(users.size() - 1));
		if (user.isFake())
			return getRandomUser(users);
		if (winners.contains(user))
			return getRandomUser(users);
		return user;
	}

	private static transient GiveAwayManager i = new GiveAwayManager();

	@Override
	public void save(Persist persit) {
		persit.save(i, "giveaways");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, GiveAwayManager.class, "giveaways");
	}
	
	private void forceEnd(int how, Message message) {

		winners.clear();

		List<User> users = new ArrayList<>();

		for (User user : message.getTextChannel().getMessageById(message.getIdLong()).complete().getReactions().get(0)
				.getUsers())
			if (user.getIdLong() != Config.botId)
				users.add(user);

		if (how > users.size())
			how = users.size();

		for (int a = 0; a != how; a++) {
			winners.add(getRandomUser(users));
		}

		StringBuilder str = new StringBuilder();
		for (User winner : winners)
			str.append(winner.getAsMention() + ", ");

		EmbedBuilder builder = new EmbedBuilder();

		builder.setColor(Color.GREEN);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setTitle("It's Giveaway Result time !");
		builder.setFooter("WorkMc - 2019 ", null);
		builder.setDescription((winners.size() == 1 ? "Gagnant: " : "Gagnants: ") + str.toString());
		message.getTextChannel().sendMessage(builder.build()).complete();

	}


}
