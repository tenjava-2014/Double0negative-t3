package org.mcsg.tenjava.random.bukkit_events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerTickEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
	
    private long tick;
    
    public ServerTickEvent(long tick) {
		this.tick = tick;
	}
    
	public long getTick(){
		return tick;
	}
}
