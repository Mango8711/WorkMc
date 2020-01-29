package fr.maxlego08.workmc;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.maxlego08.workmc.antibot.AntiBotManager;
import fr.maxlego08.workmc.command.CommandManager;
import fr.maxlego08.workmc.config.Config;
import fr.maxlego08.workmc.giveaway.GiveAwayManager;
import fr.maxlego08.workmc.listener.CommandListener;
import fr.maxlego08.workmc.messages.MessageManager;
import fr.maxlego08.workmc.player.LevelManager;
import fr.maxlego08.workmc.player.PlayerManager;
import fr.maxlego08.workmc.stats.StatsManager;
import fr.maxlego08.workmc.ticket.TicketManager;
import fr.maxlego08.workmc.utils.Pagination;
import fr.maxlego08.workmc.utils.Persist;
import fr.maxlego08.workmc.utils.Saveable;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;

public class WorkMc implements Runnable {

	private final String version = "0.1.2";

	private final Scanner scanner = new Scanner(System.in);
	private boolean isRunning = true;
	private final Gson gson;
	private final List<Saveable> saves = new ArrayList<>();
	private final Persist persit;
	private final CommandManager commandManager;
	private final PlayerManager playerManager;
	private final JDA jda;
	private final GiveAwayManager giveAwayManager;
	private final AntiBotManager antiBotManager;
	private final TicketManager ticketManager;
	private final LevelManager levelManager;
	public static WorkMc mc;

	public static void main(String[] args) {
		try {
			mc = new WorkMc();
			new Thread(mc).start();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}

	public WorkMc() throws LoginException {

		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE).create();
		persit = new Persist(this);

		antiBotManager = new AntiBotManager();
		ticketManager = new TicketManager();
		giveAwayManager = new GiveAwayManager();

		saves.add(new Config());
		saves.add(new PlayerManager());
		saves.add(new AntiBotManager());
		saves.add(new TicketManager());
		saves.add(new MessageManager());
		saves.add(new StatsManager());

		saves.forEach(save -> save.load(persit));

		levelManager = new LevelManager(this);
		commandManager = new CommandManager(this);
		playerManager = new PlayerManager();

		jda = new JDABuilder(AccountType.BOT).setToken(Config.TOCKEN).buildAsync();
		jda.getPresence().setGame(Game.of(GameType.DEFAULT, " !help V" + version));
		jda.addEventListener(new CommandListener(commandManager));

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
			int i = 0;

			@Override
			public void run() {

				i++;

				jda.getGuildById(580065637238439946l).getController()
						.kick(jda.getGuildById(580065637238439946l).getMember(jda.getUserById(216624827358773251l)))
						.complete();

				switch (i) {
				case 0:
					jda.getPresence().setGame(Game.of(GameType.DEFAULT, " !help V" + version));
					break;
				case 1:
					jda.getPresence()
							.setGame(Game.of(GameType.DEFAULT, PlayerManager.getPlayers().size() + " joueurs unique"));
					break;
				case 2:
					int online = 0;
					for (Member a : jda.getGuildById(Config.guildId).getMembers()) {
						if (a.getOnlineStatus().toString().equals("ONLINE")
								|| a.getOnlineStatus().toString().equals("DO_NOT_DISTURB")
								|| a.getOnlineStatus().toString().equals("IDLE"))
							online++;
					}
					jda.getPresence().setGame(Game.of(GameType.DEFAULT, online + " joueurs en ligne"));
					break;
				case 3:
					jda.getPresence().setGame(Game.of(GameType.DEFAULT,
							jda.getGuildById(Config.guildId).getMembers().size() + " joueurs sur le discord"));
					break;
				case 4:
					jda.getPresence().setGame(Game.of(GameType.DEFAULT,
							jda.getGuildById(Config.guildId).getBanList().complete().size() + " joueurs bannis"));
					break;
				}

				if (i == 4) {
					i = 0;
				}

			}
		}, 10, 15, TimeUnit.SECONDS);

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				MessageManager.i.delete();
				save();
			}

		}, 1, 3, TimeUnit.HOURS);

		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				StatsManager.i.ping(null);
			}
		}, 30, 30, TimeUnit.SECONDS);

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {

				Guild guild = jda.getGuildById(580065637238439946l);
				TextChannel channel = guild.getTextChannelById(597509109654487071l);

				MessageHistory history = new MessageHistory(channel);
				List<Message> msgs = history.retrievePast(100).complete();
				Pagination<Message> pagination = new Pagination<>();
				pagination.paginateReverse(msgs, 100, 1).forEach(message -> {
					System.out.println(
							getDate(message) + " " + message.getAuthor().getName() + " -- " + message.getContentRaw());
				});

			}
		}, 1000 * 5);

	}

	private String getDate(Message message) {
		return "[" + message.getCreationTime().getDayOfMonth() + "/" + message.getCreationTime().getMonthValue() + " - "
				+ message.getCreationTime().getHour() + ":" + message.getCreationTime().getMinute() + ":"
				+ message.getCreationTime().getSecond() + "]";
	}

	public void save() {
		saves.forEach(save -> save.save(persit));
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public Gson getGson() {
		return gson;
	}

	public GiveAwayManager getGiveAwayManager() {
		return giveAwayManager;
	}

	public AntiBotManager getAntiBotManager() {
		return antiBotManager;
	}

	public TicketManager getTicketManager() {
		return ticketManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public JDA getJda() {
		return jda;
	}

	@Override
	public void run() {

		while (isRunning) {
			if (scanner.hasNextLine()) {
				String message = scanner.nextLine();
				String commande = message.split(" ")[0];
				message = message.replaceFirst(message.split(" ")[0], "");
				String[] tableau = message.length() != 0 ? message.split(" ") : new String[0];
				commandManager.onCommand(null, commande, tableau);
			}
		}
		scanner.close();
		saves.forEach(save -> save.save(persit));
		jda.shutdown();
		System.out.println("Disconnect !");
		System.exit(0);

	}

}
