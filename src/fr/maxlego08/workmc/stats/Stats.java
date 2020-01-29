package fr.maxlego08.workmc.stats;

public class Stats {

	private final int player;
	private final int maxPlayer;
	private final long date;

	public Stats(int player, int maxPlayer, long date) {
		super();
		this.player = player;
		this.maxPlayer = maxPlayer;
		this.date = date;
	}

	/**
	 * @return the player
	 */
	public int getPlayer() {
		return player;
	}

	/**
	 * @return the maxPlayer
	 */
	public int getMaxPlayer() {
		return maxPlayer;
	}

	/**
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	public boolean remove() {
		return Math.abs(date - System.currentTimeMillis()) >= 1000 * 60 * 3600 * 24;
	}

}
