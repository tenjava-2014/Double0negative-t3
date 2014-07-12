package org.mcsg.tenjava.random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.mcsg.tenjava.random.bukkit_events.ServerTickEvent;
import org.mcsg.tenjava.random.events.RandomEvent;
import org.mcsg.tenjava.random.events.TestEvent;
import org.mcsg.tenjava.random.events.TickableEvent;

public class RandomEventManager implements Listener{

	//singleton instance
	private static RandomEventManager instance = new RandomEventManager();
	private RandomEventManager(){}
	
	public static RandomEventManager getInstance(){
		return instance;
	}
	
		
	private HashMap<Class<? extends Event>, List<RandomEvent>> events = new HashMap<>();
	private List<TickableEvent> tickEvents = new ArrayList<>();
	private Random rand = new Random();
	
	private long tick = 0;
	private long nextRandomTick = 0;
	private int maxBetweenTicks = 100;
	
	public void setup(){
		nextRandomTick = rand.nextInt(maxBetweenTicks);
		
		addEvent(null, new TestEvent());
		addEvent(BlockBreakEvent.class, new TestEvent());
		
		
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(TenJava.getPlugin(), () -> {
			tick++;
			TenJava.fireEvent(new ServerTickEvent(tick));
		}, 0, 1);
		Bukkit.getPluginManager().registerEvents(this, TenJava.getPlugin());
	}
	
	public void addEvent(Class<? extends Event> bukkitEvent, RandomEvent event){
		List<RandomEvent> list = events.getOrDefault(bukkitEvent, new ArrayList<RandomEvent>());
		list.add(event);
		events.put(bukkitEvent, list);
	}
	

	@EventHandler
	public void onTick(ServerTickEvent e){
		new ArrayList<>(tickEvents).stream().forEach((tickable) -> {if(tickable.tick()) tickEvents.remove(tickable); });
		if(e.getTick() >= nextRandomTick){
			nextRandomTick += rand.nextInt(maxBetweenTicks);
			List<? extends RandomEvent> list = events.get(null);
			if(list != null && list.size() > 0){
				RandomEvent revent = list.get(rand.nextInt(list.size())).getInstance();
				revent.startEvent(null);
				if(revent instanceof TickableEvent){
					tickEvents.add((TickableEvent) revent);
				}
			}
		}
	}
	
	@EventHandler
	public <T extends Event>void onEvent(T e){
		List<? extends RandomEvent> list = events.get(e.getClass());
		if(list != null && list.size() > 0){
			RandomEvent revent = list.get(rand.nextInt(list.size())).getInstance();
			revent.startEvent(e);
			if(revent instanceof TickableEvent){
				tickEvents.add((TickableEvent) revent);
			}
		}
	}
	
	
	
	
}
