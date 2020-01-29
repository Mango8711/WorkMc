package fr.maxlego08.workmc.command.commands;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.player.Player;
import fr.maxlego08.workmc.player.PlayerManager;
import fr.maxlego08.workmc.utils.Pagination;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandClassement extends VCommand {

	public CommandClassement(VCommand parent, boolean consoleCanExecute) {
		super(parent, consoleCanExecute, true);
		this.addCommand("classement");
		this.addCommand("top");
		this.setAsArgument(true);
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		int page = 1;

		if (args.length == 1) {
			try {
				page = Integer.parseInt(args[0]);
			} catch (Exception e) {
			}
		}

		if (page == 0)
			page = 1;
		if (page > getTotalPage())
			page = getTotalPage();

		EmbedBuilder builder = new EmbedBuilder();

		Random random = new Random();
		builder.setColor(new Color(random.nextInt(200) + 50, random.nextInt(200) + 50, random.nextInt(200) + 50));
		builder.setTimestamp(OffsetDateTime.now());
		builder.setFooter("WorkMC - 2019 ", null);

		StringBuilder str = new StringBuilder();

		List<Player> player = new ArrayList<>(PlayerManager.getPlayers());
		Collections.sort(player, Comparator.comparing(Player::getLevel).thenComparingInt(Player::getXp));
		
		str.append("Voici le classement par niveaux ! **" + page + "**/" + getTotalPage());
		str.append("\n");
		str.append(" ");
		str.append("\n");
		
		AtomicInteger position = new AtomicInteger(10 * (page - 1));
		Pagination<Player> pagination = new Pagination<>();
		pagination.paginateReverse(player, 10, page).forEach(p -> {
			String name = p.getLastName();
			if (p.getUser() != null)
				name = p.getUser().getAsMention();

			str.append(
					"#" + (position.getAndIncrement() + 1) + " - " + name + " avec un niveau de " + p.getLevel() +" et " + p.getXp() +" xp");
			str.append("\n");
			str.append("\n");
		});
		
		builder.setDescription(str.toString());

		getTextChannel().sendMessage(builder.build()).complete();

		return CommandType.SUCCESS;
	}

	public int getTotalPage() {
		return PlayerManager.getPlayers().size() / 10 + 1;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public String getSyntaxe() {
		return "!classement";
	}

	@Override
	public String getDescription() {
		return "Voir le top 10 des niveaux du discord";
	}

}
