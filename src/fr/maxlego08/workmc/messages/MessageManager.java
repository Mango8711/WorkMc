package fr.maxlego08.workmc.messages;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.config.Config;
import fr.maxlego08.workmc.utils.Persist;
import fr.maxlego08.workmc.utils.Saveable;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class MessageManager implements Saveable {

	private static Map<Long, Message> messages = new HashMap<Long, Message>();

	public void addMessage(net.dv8tion.jda.core.entities.Message message) {
		messages.put(message.getIdLong(), new Message(message.getIdLong(), message.getContentDisplay(),
				message.getAuthor().getName(), message.getAuthor().getIdLong()));
	}

	public void deleteMessage(long id, TextChannel c) {
		Message message = messages.getOrDefault(id, null);
		if (message == null)
			return;

		TextChannel channel = WorkMc.mc.getJda().getTextChannelById(Config.logChannel);

		EmbedBuilder builder = new EmbedBuilder();

		builder.setColor(Color.GREEN);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setTitle("Message supprimé");
		builder.setFooter("WorkMc - 2019 ", null);
		builder.setDescription("Suppression d'un message dans " + c.getAsMention() + "\n" + getName(message)
				+ " est l'auteur du message\nContenu du message:\n\n" + message.getContent());

		channel.sendMessage(builder.build()).complete();
	}

	public void delete() {
		Iterator<Map.Entry<Long, Message>> itr = messages.entrySet().iterator();
		long ms = System.currentTimeMillis();
		while (itr.hasNext()) {
			Map.Entry<Long, Message> entry = itr.next();
			if (Math.abs(ms - entry.getValue().getCreateMessage()) >= 604800000)
				itr.remove();
		}

	}

	private String getName(Message message) {
		User user = WorkMc.mc.getJda().getUserById(message.getUserId());
		return user != null ? user.getAsMention() : message.getUserName();
	}

	public static transient MessageManager i = new MessageManager();

	@Override
	public void save(Persist persit) {
		persit.save(i, "messages");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, MessageManager.class, "messages");
	}

}
