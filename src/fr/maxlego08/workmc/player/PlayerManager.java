package fr.maxlego08.workmc.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.config.Config;
import fr.maxlego08.workmc.utils.Persist;
import fr.maxlego08.workmc.utils.Saveable;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class PlayerManager implements Saveable {

	private static List<Player> players = new ArrayList<>();
	private static Map<Integer, Long> levels = new HashMap<>();
	private Map<Long, Long> cooldownMessage = new HashMap<>();

	public static Map<Integer, Long> getLevels() {
		return levels;
	}

	static {
		long start = 250;
		for (int level = 1; level != 500; level++) {
			levels.put(level, start);
			start = (long) (start + (start / 2) * 0.5);
		}
	}

	public static List<Player> getPlayers() {
		return players;
	}

	public boolean exit(long id) {
		return players.stream().filter(player -> player.getUserId() == id).findFirst().isPresent();
	}

	public Player getPlayer(long id) {
		for (Player player : players)
			if (player.getUserId() == id)
				return player;
		return players.stream().filter(player -> player.getUserId() == id).findFirst().orElse(createPlayer(id));
	}

	private Player createPlayer(long id) {
		if (exit(id))
			return getPlayer(id);
		Player player = new Player(id);
		players.add(player);
		WorkMc.mc.save();
		return player;
	}

	public static int getPostion(Player player) {

		List<Player> playerss = new ArrayList<>(players);
		Collections.sort(playerss, Comparator.comparing(Player::getLevel).thenComparingInt(Player::getXp));
		AtomicInteger i = new AtomicInteger(players.size());
		players.forEach(p -> p.setPosition(players.size() + 1 - i.getAndDecrement()));
		return player.getPosition();

	}

	/*
	 * Get player list by map with id in key and level in value
	 */

	public static Map<Long, Integer> getPlayerByMap() {
		Map<Long, Integer> map = new HashMap<>();
		for (Player player : players) {
			map.put(player.getUserId(), player.getLevel());
		}
		return map;
	}

	/*
	 * Add exeperience
	 */

	public void addExperience(Message message) {

		User user = message.getAuthor();
		Player player = getPlayer(user.getIdLong());

		if (message.getTextChannel().getIdLong() == (Config.commandChannel))
			return;

		/* On verif si le joueur n'ai pas en cooldown */

		if (!isCooldown(user)) {

			int messageSize = message.getContentDisplay().length();

			if (messageSize >= 750)
				return;

			int xp = (messageSize * 5) / 20;

			player.addXp(xp);

			updateLevel(player, message.getTextChannel());

			addCooldown(user, 3);
		}

	}

	/*
	 * Verif if player is in cooldown
	 */

	public boolean isCooldown(User user) {
		if (!cooldownMessage.containsKey(user.getIdLong()))
			return false;
		return (System.currentTimeMillis() <= (Long) (cooldownMessage.get(user.getIdLong()).longValue()));
	}

	/*
	 * Add cooldown
	 */

	public void addCooldown(User user, int time) {
		long next = System.currentTimeMillis() + time * 1000l;
		cooldownMessage.put(user.getIdLong(), next);
	}

	/*
	 * UpDate player level
	 */

	public void updateLevel(Player player, TextChannel channel) {

		int nextLevel = player.getLevel() + 1;

		if (player.getXp() >= levels.get(nextLevel)) {

			player.resetXp();
			player.addLevel(1);

			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Color.DARK_GRAY);
			builder.setDescription(Config.messages[new Random().nextInt(Config.messages.length)]
					.replace("%player%", player.getUser().getAsMention()).replace("%level%", nextLevel + ""));

			channel.sendMessage(builder.build()).complete();

		}

	}

	private static transient PlayerManager i = new PlayerManager();

	@Override
	public void save(Persist persit) {
		persit.save(i, "players");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, PlayerManager.class, "players");
		System.out.println("Nombre de joueur: " + players.size());
	}

}
