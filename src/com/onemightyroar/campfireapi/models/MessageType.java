package com.onemightyroar.campfireapi.models;

/**
 * MessageType
 * 
 * Enum for the different types of Campfire messages.
 *
 * @author brianmuse
 */
public enum MessageType {
	
	/** The text. */
	TEXT("TextMessage"),
	
	/** The paste. */
	PASTE("PasteMessage"),
	
	/** The sound. */
	SOUND("SoundMessage"),
	
	/** The enter. */
	ENTER("EnterMessage"),
	
	/** The leave. */
	LEAVE("LeaveMessage"),
	
	/** The kick. */
	KICK("KickMessage"),
	
	/** The system. */
	SYSTEM("SystemMessage"),
	
	/** The timestamp. */
	TIMESTAMP("TimestampMessage"),
	
	/** The topic change. */
	TOPIC_CHANGE("TopicChangeMessage"),
	
	/** The upload. */
	UPLOAD("UploadMessage"),
	
	/** The advertisement. */
	ADVERTISEMENT("AdvertisementMessage"),
	
	/** The allow guest. */
	ALLOW_GUEST("AllowGuestsMessage"),
	
	/** The disallow guest. */
	DISALLOW_GUEST("DisallowGuestsMessage"),
	
	/** The idle. */
	IDLE("IdleMessage"),
	
	/** The unidle. */
	UNIDLE("UnidleMessage"),
	
	/** The unlock. */
	UNLOCK("UnlockMessage"),
	
	/** The tweet. */
	TWEET("TweetMessage"),
	
	/** The unknown. */
	UNKNOWN("Unknown");

	/** The text. */
	private String text;

	/**
	 * Instantiates a new message type.
	 *
	 * @param text the text
	 */
	MessageType(String text) {
		this.text = text;
	}

	/**
	 * Get the string version.
	 *
	 * @return the string
	 */
	public String toString() {
		return this.text;
	}

	/**
	 * Get a MessageType from a string (eg. TweetMessage)
	 *
	 * @param text the text
	 * @return the message type
	 */
	public static MessageType fromString(String text) {
		if (text != null) {
			for (MessageType type : MessageType.values()) {
				if (text.equalsIgnoreCase(type.text)) {
					return type;
				}
			}
		}
		return UNKNOWN;
	}
}