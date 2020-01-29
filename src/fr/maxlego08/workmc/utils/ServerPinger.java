package fr.maxlego08.workmc.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerPinger {

	public static Map<String, Integer> ping(String ip, int port) {
		int onlinePlayers = 0;
		int maxPlayers = 0;
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), 1 * 1000);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());
			if (in == null || out == null) {
				return null;
			}
			out.write(0xFE);
			StringBuilder str = new StringBuilder();
			int b;
			while ((b = in.read()) != -1) {
				if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
					str.append((char) b);
				}
			}
			String[] data = str.toString().split("§");
//			motd = data[0];
			onlinePlayers = Integer.valueOf(data[1]);
			maxPlayers = Integer.valueOf(data[2]);
		} catch (IOException e) {
			return null;
		}
		Map<String, Integer> map = new HashMap<>();
		map.put("online", onlinePlayers);
		map.put("max", maxPlayers);
		return map;
	}

}
