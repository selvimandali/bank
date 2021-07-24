package com.bank.commonapi.events;

public enum EventType {
	
	APPLY_LOAN("apply loan"), UPDATE_USER("update user");
	
	private String description;
	
	private EventType(String description) {
		this.description=description;
	}
	
	public EventType getEventType() {
		for(EventType event: EventType.values()) {
			if(event.name()==description) {
				return event;
			}
		}
		return null;
	}

}
