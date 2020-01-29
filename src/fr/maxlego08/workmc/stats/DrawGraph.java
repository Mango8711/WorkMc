package fr.maxlego08.workmc.stats;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.dv8tion.jda.core.entities.TextChannel;

public class DrawGraph {

	public void draw(Server server, TextChannel channel) throws IOException{
		
		InputStream in = getClass().getResourceAsStream("background.png");
		BufferedImage bufferedImage = ImageIO.read(in);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", os);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
		channel.sendFile(inputStream, "stats_"+server.getName()+".png").complete();
	}
	
}
