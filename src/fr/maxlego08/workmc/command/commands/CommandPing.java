package fr.maxlego08.workmc.command.commands;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.utils.ServerPinger;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class CommandPing extends VCommand {

	public CommandPing(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("ping");
		this.setAsArgument(true);
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {

		if (args.length != 2)
			return CommandType.SYNTAX_ERROR;

		try {

			String ip = args[0];
			int port = Integer.valueOf(args[1]);

			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					Map<String, Integer> server = ServerPinger.ping(ip, port);
					if (server.get("max") == 0) {
						main.getCommandManager().sendErrorMessage("Impossible de ping l'ip **" + ip + ":" + port + "**",
								getTextChannel());
					} else
						getTextChannel().sendMessage("Il y a actuellement **" + server.get("online") + "**/"
								+ server.get("max") + " sur le serveur !").complete();
				}
			}, 1);

		} catch (Exception e) {
			return CommandType.SYNTAX_ERROR;
		}

		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public String getSyntaxe() {
		return "!ping <ip> <port>";
	}

	@Override
	public String getDescription() {
		return "Récupérer le nombre de joueur sur un serveur";
	}

}
