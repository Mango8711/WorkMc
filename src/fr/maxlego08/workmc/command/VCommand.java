package fr.maxlego08.workmc.command;

import java.util.ArrayList;
import java.util.List;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.player.Player;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public abstract class VCommand {

	public enum CommandType{SUCCESS, SYNTAX_ERROR;}
	
	private final VCommand parent;	
	private List<String> subCommands;
	private final boolean consoleCanExecute;
	private final boolean onlyUseInCommandChannel;
	private Message message;
	private boolean asArgument = false;
	
	public VCommand(VCommand parent, boolean consoleCanExecute, boolean onlyUseInCommandChannel) {
		this.parent = parent;
		this.consoleCanExecute = consoleCanExecute;
		this.onlyUseInCommandChannel = onlyUseInCommandChannel;
		subCommands = new ArrayList<>();
	}
	
	protected void addCommand(String command){
		subCommands.add(command);
	}
	
	public boolean asArgument() {
		return asArgument;
	}
	
	public void setAsArgument(boolean asArgument) {
		this.asArgument = asArgument;
	}
	
	public VCommand getParent() {
		return parent;
	}
	
	public List<String> getSubCommands() {
		return subCommands;
	}
	
	public boolean consoleCanExecute() {
		return consoleCanExecute;
	}
	
	public boolean onlyUseInCommandChannel() {
		return onlyUseInCommandChannel;
	}
	
	/*
	 * Set user
	 * */
	
	public void setMessage(Message message) {
		this.message = message;
	}

	/*
	 * Get Message
	 * */
	
	protected Message getMessage() {
		return message;
	}

	/*
	 * Get User
	 * */
	
	protected User getUser(){
		return message.getAuthor();
	}
	
	/*
	 * Get text Channel
	 * */
	
	protected TextChannel getTextChannel(){
		return message.getTextChannel();
	}
	
	/*
	 * Get private Channel
	 * */
	
	protected PrivateChannel getPriveChannel(){
		return message.getPrivateChannel();
	}
	
	/*
	 * Get Guild
	 * */
	
	protected Guild getGuild(){
		return message.getGuild();
	}
	
	/*
	 * Get JDA
	 * */
	
	protected JDA getJDA(){
		return message.getJDA();
	}
	
	/*
	 * Get Message Channel
	 * */
	
	protected MessageChannel getMessageChannel(){
		return message.getChannel();
	}
	
	/*
	 * Get Message id
	 * */
	
	protected long getId(){
		return message.getIdLong();
	}
	
	/*
	 * Get Player
	 * */
	
	protected Player getPlayer(WorkMc mc){
		return mc.getPlayerManager().getPlayer(getUser().getIdLong());
	}
	
	/*
	 * Abstarct methode
	 * */
	
	protected abstract CommandType perform(Message message, WorkMc main, String[] args);
	
	public abstract Permission getPermission();
	
	public abstract String getSyntaxe();
	
	public abstract String getDescription();
	
	protected void sendMessage(String message){
		getTextChannel().sendMessage(message).complete();
	}
	
	protected void sendEmbedMessage(String message){
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(message);
		getTextChannel().sendMessage(builder.build()).complete();
	}
	
	protected void delete(){
		getMessage().delete().complete();
	}
	
}
