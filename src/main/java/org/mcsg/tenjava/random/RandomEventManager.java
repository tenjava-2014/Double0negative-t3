package org.mcsg.tenjava.random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mcsg.tenjava.random.bukkit_events.ServerTickEvent;
import org.mcsg.tenjava.random.events.RandomEvent;
import org.mcsg.tenjava.random.events.TestEvent;
import org.mcsg.tenjava.random.events.TickableEvent;

public class RandomEventManager implements Listener{

	//singleton instance
	private static RandomEventManager instance;
	private RandomEventManager(){}
	
	public static RandomEventManager getInstance(){
		return instance;
	}
	
		
	private HashMap<Event, List<RandomEvent>> events = new HashMap<>();
	private List<TickableEvent> tickEvents = new ArrayList<>();
	private Random rand = new Random();
	
	private long tick = 0;
	private long nextRandomTick = 0;
	private int maxBetweenTicks = 500;
	
	public void setup(){
		nextRandomTick = rand.nextInt(maxBetweenTicks);
		
		addEvent(null, new TestEvent());
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(TenJava.getPlugin(), () -> {
			tick++;
			TenJava.fireEvent(new ServerTickEvent(tick));
		}, 0, 1);
	}
	
	public void addEvent(Event bukkitEvent, RandomEvent event){
		List<RandomEvent> list = events.getOrDefault(bukkitEvent, new ArrayList<RandomEvent>());
		list.add(event);
		events.put(bukkitEvent, list);
	}
	

	@EventHandler
	public void onTick(ServerTickEvent e){
		tickEvents.stream().forEach((tickable) -> {if(tickable.tick()) tickEvents.remove(tickable); });
		if(e.getTick() >= nextRandomTick){
			List<? extends RandomEvent> list = events.get(null);
			if(list != null && list.size() > 0){
				RandomEvent revent = list.get(rand.nextInt(list.size()));
				revent.startEvent();
				if(revent instanceof TickableEvent){
					tickEvents.add((TickableEvent) revent);
				}
			}
		}
	}
	
	
	
	
}
