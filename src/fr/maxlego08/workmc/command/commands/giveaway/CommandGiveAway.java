package fr.maxlego08.workmc.command.commands.giveaway;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandGiveAway extends VCommand {

	public CommandGiveAway(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("giveaway");
		this.addCommand("g");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		main.getGiveAwayManager().sendHelp(getTextChannel());
		
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public String getSyntaxe() {
		return "!g";
	}

	@Override
	public String getDescription() {
		return "Créer un giveaway";
	}

}
