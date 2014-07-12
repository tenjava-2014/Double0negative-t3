package org.mcsg.tenjava.random.events;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

public class AstroidEvent implements TickableEvent{

	private static final Random rand = new Random();
	private ArrayList<FallingBlock> blocks = new ArrayList<>();
	Vector vel = new Vector(rand.nextDouble() * 2, -1, rand.nextDouble() * 2);

	public boolean isRandom(){
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public <T extends Event> void startEvent(T event) {
		System.out.println("loaded");
		int bcount = rand.nextInt(50);
		Location start = Bukkit.getOnlinePlayers()[rand.nextInt(Bukkit.getOnlinePlayers().length)].getLocation().add(rand.nextInt(1) - 0, 100, rand.nextInt(1) - 0);
		for(int a = 0; a < bcount; a++){
			Location l = new Location(start.getWorld(), start.getX() + rand.nextInt(25) - 13, start.getY() + rand.nextInt(25) - 13, start.getZ() + rand.nextInt(25) - 13);
			System.out.println("Spawning at "+ l);

			FallingBlock block = l.getWorld().spawnFallingBlock(l, 1, (byte) 0);
			blocks.add(block);
			block.setVelocity(vel);
		}
	}

	@Override
	public RandomEvent getInstance() {
		return new AstroidEvent();
	}

	@Override
	public boolean tick() {
		for(FallingBlock block : blocks){
			block.setVelocity(vel);
		}
		return false;
	}

}
