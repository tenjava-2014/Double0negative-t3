package org.mcsg.tenjava.random.events;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.mcsg.tenjava.random.util.Effects;
import org.mcsg.tenjava.random.util.ParticalEffect;
import org.mcsg.tenjava.random.util.ParticalEffect.Partical;
import org.mcsg.tenjava.random.util.Settings;

public class AsteroidEvent implements TickableEvent{

	private static final Random rand = new Random();
	private ArrayList<FallingBlock> blocks = new ArrayList<>();
	Vector vel = new Vector(rand.nextDouble() * 4 - 2, rand.nextDouble() - 0.5, rand.nextDouble() * 4 - 2);

	public <T extends Event> boolean isRandom(T event){
		return rand.nextInt(Settings.options.AST_RANDOM) == 1 && Bukkit.getOnlinePlayers().length > 0;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public <T extends Event> void startEvent(T event) {
		int bcount = rand.nextInt(15);
		Location start = Bukkit.getOnlinePlayers()[rand.nextInt(Bukkit.getOnlinePlayers().length)].getLocation().add(0, 100, 0);
		for(int a = 0; a < bcount; a++){
			Location l = new Location(start.getWorld(), start.getX() + rand.nextInt(25) - 13, start.getY() + rand.nextInt(25) - 13, start.getZ() + rand.nextInt(25) - 13);

			FallingBlock block = l.getWorld().spawnFallingBlock(l, 173, (byte) 0);
			blocks.add(block);
			block.setVelocity(vel.add(new Vector(rand.nextDouble() * 0.4 - 0.3,rand.nextDouble() * 0.2 - 0.1, rand.nextDouble() * 0.4 - 0.3 )));
		}
	}

	@Override
	public RandomEvent getInstance() {
		return new AsteroidEvent();
	}

	int tick = 0;
	@Override
	public boolean tick() {
		tick++;
		for(FallingBlock block : blocks.toArray(new FallingBlock[0])){
			if(tick % 5 == 0)
			ParticalEffect.playEffect(Partical.FireworksSpark, block.getLocation());
			Location l = block.getLocation().clone();
			if(new Location(l.getWorld(), l.getX(), l.getY() - 3, l.getZ()).getBlock().getType() != Material.AIR){
				Effects.createExplosion(block.getLocation().subtract(0, 4, 0), 4);
				block.remove();
				blocks.remove(block);
				for(int a = 0; a < rand.nextInt(3); a++)
				l.getWorld().dropItemNaturally(l, new ItemStack(Material.DIAMOND_ORE));
			}
			if(tick > 300){
				blocks.clear();
				return true;
			}
		}
		return false;
	}

}
