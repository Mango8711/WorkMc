package fr.maxlego08.workmc.stats;

import java.awt.Color;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.utils.Persist;
import fr.maxlego08.workmc.utils.Saveable;
import fr.maxlego08.workmc.utils.ServerPinger;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class StatsManager implements Saveable {

	private static Map<String, Server> servers = new HashMap<String, Server>();

	public void draw(String name, TextChannel channel) {
		if (!servers.containsKey(name)) {
			WorkMc.mc.getCommandManager().sendErrorMessage("Le serveur " + name + " n'existe pas !", channel);
			return;
		}
		try {
			new DrawGraph().draw(servers.get(name), channel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void addServer(String name, String ip, int port, TextChannel channel) {

		if (servers.containsKey(name)) {
			WorkMc.mc.getCommandManager().sendErrorMessage("Le serveur " + name + " existe déjà !", channel);
			return;
		}

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				Map<String, Integer> curentServer = ServerPinger.ping(ip, port);

				if (curentServer.get("max") == 0) {
					WorkMc.mc.getCommandManager().sendErrorMessage("Impossible de ping le serveur !", channel);
					return;
				}

				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.GREEN);
				builder.setTimestamp(OffsetDateTime.now());
				builder.setFooter("WorkMc - 2019 ", null);
				builder.setDescription(":white_check_mark: Vous venez d'ajouter le serveur **" + name + "** avec l'ip: "
						+ ip + ":" + port);
				channel.sendMessage(builder.build()).complete();
				Server server = new Server(name, ip, port);
				server.addStats(
						new Stats(curentServer.get("online"), curentServer.get("max"), System.currentTimeMillis()));
				servers.put(name, server);

			}
		}, 1);

	}

	public void remove(TextChannel channel, String name) {
		if (!servers.containsKey(name)) {
			WorkMc.mc.getCommandManager().sendErrorMessage("Le serveur **" + name + "** n'existe pas !", channel);
			return;
		}
		servers.remove(name);
		channel.sendMessage(":white_check_mark: Vous venez de retirer le serveur **" + name + "**").complete();
	}

	public void show(TextChannel channel) {
		if (channel == null)
			return;
		StringBuilder string = new StringBuilder();
		servers.forEach((n, s) -> {
			string.append("Serveur: **" + s.getName() + "**");
			string.append("\n");
			string.append("En ligne: **" + s.currentPlayer() + "** / **" + s.currentMaxPlayer() + "**");
			string.append("\n");
			string.append("Nombre de joueur maximum: **" + s.getMaxPlayer() + "**");
			string.append("\n");
			string.append("\n");
		});
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setFooter("WorkMc - 2019 ", null);
		builder.setDescription(string.toString());
		channel.sendMessage(builder.build()).complete();
	}

	public boolean ping(TextChannel channel) {
		if (servers.size() == 0) {
			if (channel != null)
				WorkMc.mc.getCommandManager().sendErrorMessage("Aucun serveur disponible !", channel);
			return false;
		}
		long ms = System.currentTimeMillis();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				servers.forEach((name, server) -> {
					Map<String, Integer> curentServer = ServerPinger.ping(server.getAddress(), server.getPort());
					server.addStats(
							new Stats(curentServer.get("online"), curentServer.get("max"), System.currentTimeMillis()));
				});
				String msg = "Ping de " + servers.size() + " serveurs en " + Math.abs(ms - System.currentTimeMillis())
						+ " ms";
				Calendar calendar = Calendar.getInstance();
				System.out.println("[" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR) + ":"
						+ calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "] " + msg);
				if (channel != null)
					WorkMc.mc.getCommandManager().sendErrorMessage(msg, channel);
			}
		}, 1);
		return true;
	}

	public static transient StatsManager i = new StatsManager();

	@Override
	public void save(Persist persit) {
		persit.save(i, "stats");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, StatsManager.class, "stats");
	}

}
