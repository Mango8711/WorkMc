package fr.maxlego08.workmc.ticket;

public enum TicketStatus {

	OPEN(1),
	CLOSE(2),
	CLOSE_BY_AUTHOR(3),
	
	;
	
	private final int id;

	private TicketStatus(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}
