package fr.maxlego08.workmc.command.commands;

import java.awt.Color;
import java.time.OffsetDateTime;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.command.VCommand;
import fr.maxlego08.workmc.player.Player;
import fr.maxlego08.workmc.utils.TimerBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

public class CommandMute extends VCommand {

	public CommandMute(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		super(parent, consoleCanExecute, onlyUseInCommandChannel);
		this.addCommand("mute");
		this.addCommand("tempmute");
		this.setAsArgument(true);
	}

	@Override
	protected CommandType perform(Message message, WorkMc main, String[] args) {
		
		try{
			
			/*
			 * Le code est absolument pas optimisé 
			 * */
			
			User user = message.getMentionedUsers().get(0);
			String m = message.getContentDisplay().replace("@"+message.getMentionedUsers().get(0).getName(), "");
			m = m.replace("!mute ", "");
			args = m.split(" ");
			
			if (args.length != 2) return CommandType.SYNTAX_ERROR;
			
			String time = args[1];
			
			if (!timeSyntaxe(time)) return CommandType.SYNTAX_ERROR;
			
			int timer = getTime(time);
			
			Player player = main.getPlayerManager().getPlayer(user.getIdLong());
			
			player.setMute(timer);
			
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Color.GREEN);
			builder.setTimestamp(OffsetDateTime.now());
			builder.setFooter("WorkMc - 2019 - ", null);
			builder.setDescription(":white_check_mark: Vous venez de mute " + user.getAsMention() + " pendant " + TimerBuilder.getStringTime(timer));
			
			getTextChannel().sendMessage(builder.build()).complete();
			
			return CommandType.SUCCESS;
			
		}catch (Exception e) { }
		
		return CommandType.SYNTAX_ERROR;
	}

	@Override
	public Permission getPermission() {
		return Permission.KICK_MEMBERS;
	}

	@Override
	public String getSyntaxe() {
		return "!mute <joueur> <temps>";
	}

	@Override
	public String getDescription() {
		return "Permet de rendre muet une personne ";
	}
	
	private boolean timeSyntaxe(String time){
		return time.contains("d") || time.contains("m") || time.contains("s") || time.contains("h");
	}
	
	private int getTime(String time){
		try{
		if (time.contains("d")){
			return Integer.valueOf(time.replace("d", "")) * 60 * 60 * 24;
		}else if (time.contains("m")){
			return Integer.valueOf(time.replace("m", "")) * 60;
		}else if (time.contains("s")){
			return Integer.valueOf(time.replace("s", ""));
		}else if (time.contains("h")){
			return Integer.valueOf(time.replace("h", "")) * 60 * 60;
		}
		}catch (NumberFormatException e) { }
		return 0;
		
	}
	
}
