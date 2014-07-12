package org.mcsg.tenjava.random.events;


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
	public void startEvent();
	
	
	
	/**
	 * return a new instance of this event
	 * @return get and instance of this event
	 */
	public RandomEvent getInstance();
}
