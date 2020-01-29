package fr.maxlego08.workmc.ticket;

public class Ticket {

	private final int id;
	private final long userId;
	private final long channelId;
	private final long createTime;
	private final String title;
	private final String description;
	private final TicketType type;
	private TicketStatus status;

	public Ticket(int id, long userId, long channelId, long createTime, String title, String description,
			TicketType type, TicketStatus status) {
		super();
		this.id = id;
		this.userId = userId;
		this.channelId = channelId;
		this.createTime = createTime;
		this.title = title;
		this.description = description;
		this.type = type;
		this.status = status;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @return the channelId
	 */
	public long getChannelId() {
		return channelId;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the type
	 */
	public TicketType getType() {
		return type;
	}

	/**
	 * @return the status
	 */
	public TicketStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(TicketStatus status) {
		this.status = status;
	}

}
