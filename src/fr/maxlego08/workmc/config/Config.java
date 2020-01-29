package fr.maxlego08.workmc.config;

import fr.maxlego08.workmc.utils.Persist;
import fr.maxlego08.workmc.utils.Saveable;

public class Config implements Saveable{

	public static String TOCKEN = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	public static long commandChannel = 597367522517319681l;
	public static long giveawayChannel = 597367508881899520l;
	public static long logChannel = 597367490405990410l;
	public static long notBotChannel = 597369006919385093l;
	public static long noBotRank = 597369183130615825l;
	public static long giveawayEmoji = 597380394610065428l;
	public static long botId = 597364835461890048l;
	public static long logTicket = 597411545370329108l;
	public static long ticketCategory = 597411533219168256l;
	public static long blacklistChannel = 597487086710423562l;
	public static long defaultChannel = 580065637238439949l;
	public static long guildId = 580065637238439946l;
	public static long projectChannel = 580079145707372559l;
	public static boolean muteAll = true;
	
	public static String[] messages = new String[]{
			":tada: %player% vient de passer au niveau **%level%**",
			"Whoaa :scream: %player% vient de monter au niveau **%level%** un grand fou !",
			"Ptdr %player% vient de passer seulement au niveau **%level%**, vraiment trop nul",
			"System.err.println('%player% vient de passer au niveau %level%')",
			":rage: %player% est encore en train de se boost... il vient de passer au niveau **%level%**",
			};
	
	private static transient Config i = new Config();
	@Override
	public void save(Persist persit) {
		persit.save(i);
	}
	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, Config.class);
	}

}
