package fr.maxlego08.workmc.giveaway.ga;

import net.dv8tion.jda.core.entities.Message;

public class GiveAway{

	private final long channelId;
	private final int winner;
	private final long remaningTime;
	private final String prize;
	private long messageId;
	private Message message;
	
	public GiveAway(long channelId, int winner, long remaningTime, String prize) {
		super();
		this.channelId = channelId;
		this.winner = winner;
		this.remaningTime = remaningTime;
		this.prize = prize;
	}

	public long getChannelId() {
		return channelId;
	}

	public long getMessageId() {
		return messageId;
	}

	public long getRemaningTime() {
		return remaningTime;
	}

	
	public int getWinner() {
		return winner;
	}

	
	public String getPrize() {
		return prize;
	}

	
	public void setMessageId(long id) {
		this.messageId = id;
	}

	
	public void setMessage(Message message) {
		this.message = message;
	}

	
	public Message getMessage() {
		return message;
	}

	
	public int getRealTime() {
		return (int)(Math.abs(System.currentTimeMillis()-getRemaningTime())) / 1000;
	}

	
	public boolean isFinish() {
		return System.currentTimeMillis() >= getRemaningTime();
	}

}
