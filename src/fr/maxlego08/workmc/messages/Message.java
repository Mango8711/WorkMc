package fr.maxlego08.workmc.messages;

public class Message {

	private final long id;
	private final String content;
	private final String userName;
	private final long userId;
	private long createMessage;

	public Message(long id, String content, String userName, long userId) {
		super();
		this.id = id;
		this.content = content;
		this.userName = userName;
		this.userId = userId;
		this.createMessage = System.currentTimeMillis();
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @return the createMessage
	 */
	public long getCreateMessage() {
		if (createMessage == 0)
			createMessage = System.currentTimeMillis();
		return createMessage;
	}
	
}
