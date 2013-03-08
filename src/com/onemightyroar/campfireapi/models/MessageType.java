package com.onemightyroar.campfireapi.models;

public enum MessageType {
	TEXT("TextMessage"),
	PASTE("PasteMessage"),
	SOUND("SoundMessage"),
	ENTER("EnterMessage"),
	LEAVE("LeaveMessage"),
	KICK("KickMessage"),
	SYSTEM("SystemMessage"),
	TIMESTAMP("TimestampMessage"),
	TOPIC_CHANGE("TopicChangeMessage"),
	UPLOAD("UploadMessage"),
	ADVERTISEMENT("AdvertisementMessage"),
	ALLOW_GUEST("AllowGuestsMessage"),
	DISALLOW_GUEST("DisallowGuestsMessage"),
	IDLE("IdleMessage"),
	UNIDLE("UnidleMessage"),
	UNLOCK("UnlockMessage"),
	TWEET("TweetMessage"),
	UNKNOWN("Unknown");

	private String text;

	MessageType(String text) {
		this.text = text;
	}

	public String toString() {
		return this.text;
	}

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