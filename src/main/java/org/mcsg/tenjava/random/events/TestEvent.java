package org.mcsg.tenjava.random.events;

import org.bukkit.Bukkit;

public class TestEvent implements TickableEvent{

	int tick = 0;
	
	@Override
	public void startEvent() {
		Bukkit.broadcastMessage("Test event starting");
		
	}

	@Override
	public boolean tick() {
		Bukkit.broadcastMessage("Test event ticking");
		tick++;
		return tick > 5;
	}

}
