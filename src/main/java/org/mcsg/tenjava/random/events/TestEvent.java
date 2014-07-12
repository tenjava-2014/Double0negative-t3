package org.mcsg.tenjava.random.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class TestEvent implements TickableEvent{

	public <T extends Event> boolean isRandom(T event){
		return true;
	}
	
	int tick = 0;
	
	@Override
	public <T extends Event> void startEvent(T event) {
		Bukkit.broadcastMessage("Test event starting");
	}

	@Override
	public boolean tick() {
		Bukkit.broadcastMessage("Test event ticking");
		tick++;
		return tick > 5;
	}

	@Override
	public RandomEvent getInstance() {
		return new TestEvent();
	}



}
