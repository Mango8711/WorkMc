package fr.maxlego08.workmc.player;

import fr.maxlego08.workmc.WorkMc;
import net.dv8tion.jda.core.entities.User;

public class Player implements Comparable<Player>{

	private final long userId;

	private long mute = 0;
	private long ban = 0;
	private long projetMessage = 0;
	private int level;
	private int xp;
	private transient int position = -1;
	private String lastName;
	
	public Player(long userId) {
		this.userId = userId;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setBan(long ban) {
		this.ban = ban * 1000l + System.currentTimeMillis();
	}
	
	public void setMute(long mute) {
		this.mute = mute * 1000l + System.currentTimeMillis();
	}
	
	public long getBan() {
		return ban;
	}
	
	public long getMute() {
		return mute;
	}
	
	public boolean isMute(){
		return mute != 0 && System.currentTimeMillis() <= mute;
	}

	public boolean isBan(){
		return ban != 0 && System.currentTimeMillis() <= ban;
	}

	public void clearMute() {
		mute = 0;
	}
	
	public void clearBan() {
		ban = 0;
	}
	
	public void clearProjet() {
		projetMessage = 0;
	}
	
	public long getProjetMessage() {
		return projetMessage;
	}
	
	public void setProjetMessage(long projetMessage) {
		this.projetMessage = projetMessage;
	}
	
	public boolean isProject(){
		return projetMessage != 0 && System.currentTimeMillis() <= projetMessage;
	}
	
	public int getXp() {
		return xp;
	}
	
	public int getLevel() {
		return level;
	}

	public void addXp(int xp) {
		this.xp += xp;
	}

	public void addLevel(int level) {
		this.level += level;
	}
	
	public void resetLevel() {
		level = 0;
	}

	public void resetXp() {
		xp = 0;
	}
	
	public User getUser(){
		return WorkMc.mc.getJda().getUserById(userId);		
	}

	public String getLastName() {
		if (getUser() != null) lastName = getUser().getName();
		else lastName = userId+"";
		return lastName;
	}

	@Override
	public int compareTo(Player o) {
		return Integer.valueOf(o.getLevel()).compareTo(Integer.valueOf(getLevel()));
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getPosition() {
		return position;
	}
}
