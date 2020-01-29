package fr.maxlego08.workmc.stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Server {

	private final String name;
	private final String address;
	private final int port;
	private int maxPlayer;
	private List<Stats> stats = new ArrayList<Stats>();

	public Server(String name, String address, int port) {
		super();
		this.name = name;
		this.address = address;
		this.port = port;
	}

	public List<Stats> getStats() {
		return stats;
	}

	public void addStats(Stats stats) {
		maxPlayer = Math.max(maxPlayer, stats.getPlayer());
		this.stats.add(stats);
		update();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the maxPlayer
	 */
	public int getMaxPlayer() {
		return maxPlayer;
	}

	public int currentPlayer() {
		if (stats.size() == 0)
			return 0;
		return stats.get(stats.size() - 1).getPlayer();
	}
	
	public int currentMaxPlayer() {
		if (stats.size() == 0)
			return 0;
		return stats.get(stats.size() - 1).getMaxPlayer();
	}
	
	private void update(){
		Iterator<Stats> iterator = stats.iterator();
		while(iterator.hasNext()){
			Stats stats = iterator.next();
			if (stats.remove())
				iterator.remove();
		}
	}

}
