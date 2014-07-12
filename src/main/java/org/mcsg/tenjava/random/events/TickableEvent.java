package org.mcsg.tenjava.random.events;



/**
 * 
 * @author drew
 *
 *	Interface for a tickable event. Tickable events are 
 *	called once per tick.
 *
 */
public interface TickableEvent extends RandomEvent{

	
	/**
	 * Tick the event
	 */
	public boolean tick();
	
}
