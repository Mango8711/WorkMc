package fr.maxlego08.workmc.antibot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.config.Config;
import fr.maxlego08.workmc.utils.Persist;
import fr.maxlego08.workmc.utils.Saveable;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class AntiBotManager implements Saveable {

	private static Map<Long, String> antibotCode = new HashMap<>();

	private final transient List<Color> colors = Arrays.asList(Color.WHITE, Color.RED, Color.YELLOW, Color.GRAY,
			Color.GREEN, Color.PINK, Color.BLUE);

	public void sendPrivateCode(User user) throws IOException {

		char[] randomString = getRandomString();

		InputStream in = getClass().getResourceAsStream("background.png");
		BufferedImage bufferedImage = ImageIO.read(in);

		Graphics g = bufferedImage.getGraphics();
		g.setFont(g.getFont().deriveFont(30f));

		int posX = 20;
		int posY = 150;

		g.setFont(new Font("Arial", Font.BOLD, 70 + ThreadLocalRandom.current().nextInt(-10, 10)));
		StringBuilder builder = new StringBuilder();

		for (char c : randomString) {
			g.setColor(getRandomColor());
			g.drawString(String.valueOf(c), posX, posY + ThreadLocalRandom.current().nextInt(-30, 30));
			posX += 70;
			builder.append(c);
		}

		antibotCode.put(user.getIdLong(), builder.toString());

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", os);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
		user.openPrivateChannel().complete().sendFile(inputStream, "antibot_create_by_maxlego08.png").complete();
	}

	public void acceptPlayer(User user, Message message) {
		if (!hasUser(user))
			return;
		if (message == null || message.getContentRaw() == null)
			return;

		if (message.getChannel().getIdLong() == Config.defaultChannel)
			return;

		if (message.getMember() != null && message.getMember().getRoles().contains(message.getGuild().getRoleById(Config.noBotRank))){
			antibotCode.remove(user.getIdLong());
			return;
		}
		
		String currentCode = antibotCode.get(user.getIdLong());

		if (message.isFromType(ChannelType.PRIVATE)) {
			message.getChannel().sendMessage("Vous devez envoyer le code dans #verification sur le discord !")
					.complete();
			return;
		}

		message.delete().complete();

		if (currentCode.equalsIgnoreCase(message.getContentRaw())) {
			antibotCode.remove(user.getIdLong());
			message.getGuild().getController()
					.addSingleRoleToMember(message.getMember(), message.getGuild().getRoleById(Config.noBotRank))
					.complete();
			user.openPrivateChannel().complete().sendMessage("Bienvenue sur WorkMc !").complete();
			log(message);

		} else {
			WorkMc.mc.getCommandManager().sendErrorMessage(
					"Vous avez rentrez le mauvais code ! Faite !code pour générer un nouveau code ! Vous devez avoir vos messages privée d'ouvert !",
					message.getTextChannel());
		}
	}

	private void log(Message message) {

		TextChannel channel = message.getGuild().getTextChannelById(Config.logChannel);
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.GREEN);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setTitle(message.getAuthor().getName());
		builder.setFooter("WorkMc - 2019 ", null);
		builder.setDescription(message.getAuthor().getAsMention() + " vient de valider le code !");
		channel.sendMessage(builder.build()).complete();

	}

	public boolean hasUser(User user) {
		return antibotCode.containsKey(user.getIdLong());
	}

	private Color getRandomColor() {
		return colors.get(ThreadLocalRandom.current().nextInt(0, colors.size()));
	}

	public char[] getRandomString() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();
		char[] c = new char[targetStringLength];
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			c[i] = (char) randomLimitedInt;
		}
		return c;
	}

	private static transient AntiBotManager i = new AntiBotManager();

	@Override
	public void save(Persist persit) {
		persit.save(i, "antibot");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, AntiBotManager.class, "antibot");
	}

	public void recreate(User user, Message message) {

		if (message.getTextChannel().getIdLong() != Config.notBotChannel) {
			WorkMc.mc.getCommandManager().sendErrorMessage("Vous ne pouvez pas faire cela ici !",
					message.getTextChannel());
			return;
		}

		try {
			sendPrivateCode(user);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
