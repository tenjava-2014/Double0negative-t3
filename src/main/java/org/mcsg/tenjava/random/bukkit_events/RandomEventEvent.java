package org.mcsg.tenjava.random.bukkit_events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.mcsg.tenjava.random.events.RandomEvent;

public class RandomEventEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	
	private RandomEvent event;
	private Event bukkitEvent;
	
	
	public Event getBukkitEvent (){
		return bukkitEvent;
	}
	
	public RandomEvent getEvent(){
		return event;
	}
	
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
	

}
