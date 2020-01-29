package fr.maxlego08.workmc.ticket;

public enum TicketType {

	INFORMATION,
	RANK,
	BLACKLIST,
	OTHER,
	
	;

	public static TicketType get(String z){
		for(TicketType t : TicketType.values())
			if (t.name().equalsIgnoreCase(z.toUpperCase()))
				return t;
		return null;
	}
	
	public static String get(){
		StringBuilder builder = new StringBuilder();
		for(TicketType t : TicketType.values())
			builder.append(t.name() +", ");
		return builder.toString();
	}
	
}
