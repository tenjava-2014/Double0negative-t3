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
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.mcsg.tenjava.random.bukkit_events.ServerTickEvent;
import org.mcsg.tenjava.random.events.RandomEvent;
import org.mcsg.tenjava.random.events.TestEvent;
import org.mcsg.tenjava.random.events.TickableEvent;
import org.mcsg.tenjava.random.util.MultiMapLongTrigger;
import org.mcsg.tenjava.random.util.Trigger;

public class RandomEventManager implements Listener{

	//singleton instance
	private static RandomEventManager instance = new RandomEventManager();
	private RandomEventManager(){}
	
	public static RandomEventManager getInstance(){
		return instance;
	}
	
	private final MultiMapLongTrigger triggers = new MultiMapLongTrigger();
	private HashMap<Class<? extends Event>, List<RandomEvent>> events = new HashMap<>();
	private List<TickableEvent> tickEvents = new ArrayList<>();
	private Random rand = new Random();
	
	private long tick = 0;
	private long nextRandomTick = 0;
	private int maxBetweenTicks = 100;
	
	public void setup(){
		nextRandomTick = rand.nextInt(maxBetweenTicks);
		
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
		
		for (final Long l : triggers.keySet()) {
			if (e.getTick() == 0 || e.getTick() % l == 0) {
				List<Trigger> trigs = triggers.get(l);
				new ArrayList<Trigger>(trigs).stream().forEach((t) -> {
					if(t.check()){ t.execute(); trigs.remove(t); }
				});
			}
		}
	}
	
	public void registerTrigger(Trigger t, long frequency) {
		triggers.put(frequency, t);
	}
	
	@EventHandler
	public <T extends BlockEvent> void onEvent(T e){
		onEvent(e);
	}
	
	@EventHandler
	public <T extends PlayerEvent> void onEvent(T e){
		onEvent(e);
	}
	
	
	@EventHandler
	public <T extends EntityEvent> void onEvent(T e){
		onEvent(e);
	}
	
	public <T extends Event> void onEvent(T event){
		List<? extends RandomEvent> list = events.get(event.getClass());
		if(list != null && list.size() > 0){
			RandomEvent revent = list.get(rand.nextInt(list.size())).getInstance();
			revent.startEvent(event);
			if(revent instanceof TickableEvent){
				tickEvents.add((TickableEvent) revent);
			}
		}
	}
	
	
}
