package org.mcsg.tenjava.random.events;

import org.bukkit.event.Event;


/**
 * Interface for a random event.
 * @author drew
 *
 */
public interface RandomEvent {

	/**
	 * Start the event
	 * 
	 */
	public <T extends Event> void startEvent(T event);
	
	
	
	/**
	 * return a new instance of this event
	 * @return get and instance of this event
	 */
	public RandomEvent getInstance();
}
