package fr.maxlego08.workmc.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import fr.maxlego08.workmc.WorkMc;
import fr.maxlego08.workmc.utils.Constant;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class LevelManager implements Constant {

	public LevelManager(WorkMc mc) {
	}

	public void sendPlayerLevel(Player player, TextChannel channel, User user) {

		try {

			InputStream in = getClass().getResourceAsStream("background.png");
			BufferedImage bufferedImage = ImageIO.read(in);

			Graphics g = bufferedImage.getGraphics();
			g.setFont(g.getFont().deriveFont(30f));

			/*
			 * Draw avatar image
			 */

			URL url = new URL(user.getAvatarUrl());
			URLConnection urlc = url.openConnection();
			urlc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

			Image img = ImageIO.read(urlc.getInputStream());

			g.drawImage(img, 25, 25, 150, 150, null);

			/*
			 * Draw "LEVEL"
			 */

			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.setColor(Color.WHITE);
			g.drawString("NIVEAU", 643, 50);

			/*
			 * Draw Player level
			 */

			g.setFont(new Font("Arial", Font.PLAIN, 45));
			g.setColor(Color.WHITE);
			g.drawString(String.valueOf(player.getLevel()), 740, 50);

			String postion = "#" + PlayerManager.getPostion(player);
			int size = getLocationX(postion, PIXEL);

			/*
			 * Draw "RANk"
			 */

			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.setColor(Color.WHITE);
			g.drawString("RANK", (650 - size - 10) - RANK_PIXEL, 50);

			/*
			 * Draw position
			 */

			g.setFont(new Font("Arial", Font.PLAIN, 45));
			g.setColor(Color.WHITE);
			g.drawString(postion, 650 - size - 10, 50);

			/*
			 * Draw player name
			 */

			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.setColor(Color.WHITE);
			g.drawString(getName(user), 200, 90);

			/*
			 * Draw player id
			 */

			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("#" + user.getDiscriminator(), 202 + getLocationXPseudo(getName(user)), 90);

			/*
			 * Draw rectangle
			 */

			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.setColor(Color.DARK_GRAY);
			g.fillRoundRect(200, 110, 575, 65, 20, 20);

			/*
			 * Draw xp rectangle
			 */

			int percent = getPercent(player);
			int x = getX(percent);

			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.setColor(Color.GRAY);
			g.fillRoundRect(200, 110, x, 65, 20, 20);

			/*
			 * Draw player xp
			 */

			g.setFont(new Font("Arial", Font.PLAIN, 22));
			g.setColor(Color.WHITE);
			g.drawString(getXp(player.getXp()) + "xp", 580, 95);

			/*
			 * Draw max player xp
			 */
			g.setFont(new Font("Arial", Font.PLAIN, 22));
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("/ " + getXp(PlayerManager.getLevels().get(player.getLevel() + 1)) + "xp",
					585 + getLocationX(getXp(player.getXp()) + "xp", PIXEL_INT), 95);

			/*
			 * Draw graphic
			 */

			g.dispose();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", os);

			ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
			channel.sendFile(inputStream, "rank.png").complete();

			// System.out.println(PREFIX + " Send rank for " + player.getName()
			// + " in channel " + channel.getName());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getName(User user) {
		return user.getName().length() > 15 ? user.getName().substring(0, 15) + "..." : user.getName();
	}

	private int getLocationX(String text, int size) {
		int lenght = 0;
		lenght = text.length() * size;
		return lenght;
	}

	private int getLocationXPseudo(String pseudo) {

		return pseudo.length() * 14;

	}

	private int getPercent(Player player) {

		int xp = player.getXp();
		long next = PlayerManager.getLevels().get(player.getLevel() + 1);

		int percent = (int) (xp * 100 / next);

		return percent;
	}

	private int getX(int percent) {
		int size = 575;
		float f = (float) percent / 100;
		float p = (float) size * f;
		return (int) p;
	}

	private String getXp(long xp) {
		String strXp = "" + xp;
		double d;
		if (xp >= 1000 && xp <= 10000) {
			d = xp / 1000;
			strXp = d + "";
			String string = xp + "";
			string = string.substring(0, 1);
			string += ".";
			String z = xp + "";
			string += z.substring(1, 3);
			return string + "k ";
		} else if (xp > 10000 && xp <= 100000) {
			d = xp / 1000;
			strXp = d + "";
			String string = xp + "";
			string = string.substring(0, 2);
			string += ".";
			String z = xp + "";
			string += z.substring(2, 3);
			return string + "k ";
		}

		return strXp;
	}

}
