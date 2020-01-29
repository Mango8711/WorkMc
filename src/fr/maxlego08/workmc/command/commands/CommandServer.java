package fr.maxlego08.workmc.command.commands;

import java.awt.Color;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.player.PlayerManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class CommandServer extends VCommand {

	public CommandServer(VCommand parent, boolean consoleCanExecute) {
		super(parent, consoleCanExecute, true);
		this.addCommand("server");
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.CYAN);
		
		builder.addField("Nom du discord", message.getGuild().getName(), true);
		builder.addField("Fondateur du discord", message.getGuild().getOwner().getAsMention(), true);
		builder.addField("Date de création", String.valueOf(message.getGuild().getCreationTime()), false);		
		builder.addField("Nombre de catégories", String.valueOf(message.getGuild().getCategories().size()), true);
		builder.addField("Nombre de salon textuel", String.valueOf(message.getGuild().getTextChannels().size()), true);
		builder.addField("Nombre de salon vocaux", String.valueOf(message.getGuild().getVoiceChannels().size()), false);
		builder.addField("Joueur bannis", String.valueOf(message.getGuild().getBanList().complete().size()), true);
		builder.addField("Membres", String.valueOf(getGuild().getMembers().size()), true);
		int bot = 0;
		int online = 0;
		for(Member a : message.getGuild().getMembers()){
			if (a.getUser().isBot()) bot++;
			if (a.getOnlineStatus().toString().equals("ONLINE") || a.getOnlineStatus().toString().equals("DO_NOT_DISTURB")
					|| a.getOnlineStatus().toString().equals("IDLE") ){online++;}			
		}
		builder.addField("Humains", String.valueOf(getGuild().getMembers().size()-bot), true);
		builder.addField("Connectés", String.valueOf(online), true);
		builder.addField("Bots", String.valueOf(bot), true);
		builder.addField("Membre unique", String.valueOf(PlayerManager.getPlayers().size()), true);
		
		getTextChannel().sendMessage(builder.build()).queue();
		
		return CommandType.SUCCESS;
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public String getSyntaxe() {
		return "!server";
	}

	@Override
	public String getDescription() {
		return "Voir les informations du serveur";
	}

}
