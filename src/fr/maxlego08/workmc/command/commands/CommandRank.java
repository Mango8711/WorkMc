package fr.maxlego08.workmc.command.commands;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class CommandRank extends VCommand {

	public CommandRank(VCommand parent, boolean consoleCanExecute) {
		super(parent, consoleCanExecute, true);
		this.setAsArgument(true);
		this.addCommand("rank");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		try {
			Member member = (Member) message.getMentionedMembers().get(0);
			main.getLevelManager().sendPlayerLevel(main.getPlayerManager().getPlayer(member.getUser().getIdLong()),
					getTextChannel(), member.getUser());
		} catch (Exception e) {
			main.getLevelManager().sendPlayerLevel(getPlayer(main), getTextChannel(), getUser());
		}
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public String getSyntaxe() {
		return "!rank";
	}

	@Override
	public String getDescription() {
		return "Voir son niveau";
	}

}
