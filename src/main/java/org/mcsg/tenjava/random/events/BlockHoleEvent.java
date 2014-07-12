package org.mcsg.tenjava.random.events;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.mcsg.tenjava.random.util.Effects;
import org.mcsg.tenjava.random.util.ParticalEffect;
import org.mcsg.tenjava.random.util.ParticalEffect.Partical;
import org.mcsg.tenjava.random.util.Settings;

public class BlockHoleEvent implements TickableEvent{

	
	
	private Location loc;
	private Random rand = new Random ();
	private int stop = rand.nextInt(200) + 100;
	
	@Override
	public <T extends Event> boolean isRandom(T event) {
		return rand.nextInt(Settings.options.BLOCK_HOLE_RANDOM) == 100 && Bukkit.getOnlinePlayers().length > 0;
	}

	@Override
	public <T extends Event> void startEvent(T event) {
		loc = Bukkit.getOnlinePlayers()[rand.nextInt(Bukkit.getOnlinePlayers().length)].getLocation().add(rand.nextInt(20) - 10, -3, rand.nextInt(20) - 10);
	}

	@Override
	public RandomEvent getInstance() {
		return new BlockHoleEvent();
	}

	int tick = 0;
	@Override
	public boolean tick() {
		tick++;
		if(tick % 5 == 0){
			Effects.createImplosion(loc, 2);
			ParticalEffect.playEffect(Partical.Portal, loc);
			loc = loc.add(rand.nextDouble() * 10 - 5,rand.nextDouble() * 4 - 2, rand.nextDouble() * 10 - 5 );
		}
		if(tick > stop){
			return true;
		} else 
			return false;
	}

	
	
	
	
	
	
}
