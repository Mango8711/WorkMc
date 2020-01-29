package fr.maxlego08.workmc.command.commands;

import java.awt.Color;
import java.time.OffsetDateTime;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.player.Player;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public class CommandUnBan extends VCommand {

	public CommandUnBan(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("unban");
		this.setAsArgument(true);
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		try {

			User user = message.getMentionedUsers().get(0);
			Player player = main.getPlayerManager().getPlayer(user.getIdLong());

			if (player.isMute()) {
				player.clearBan();

				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.GREEN);
				builder.setTimestamp(OffsetDateTime.now());
				builder.setFooter("WorkMc - 2019 ", null);
				builder.setDescription(":white_check_mark: Vous venez d'unban " + user.getAsMention() + " !");

				getTextChannel().sendMessage(builder.build()).complete();

			} else {

				message.delete().complete();
				main.getCommandManager().sendErrorMessage("Ce joueur n'est pas ban !", getTextChannel());

			}

			return CommandType.SUCCESS;

		} catch (Exception e) {
		}

		return CommandType.SYNTAX_ERROR;
	}

	@Override
	public Permission getPermission() {
		return Permission.MESSAGE_MANAGE;
	}

	@Override
	public String getSyntaxe() {
		return "!unban <joueur>";
	}

	@Override
	public String getDescription() {
		return "Unban un joueur";
	}

}
