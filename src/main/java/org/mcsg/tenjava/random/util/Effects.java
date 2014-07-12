package org.mcsg.tenjava.random.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.mcsg.tenjava.random.RandomEventManager;

public class Effects implements Listener{


	private static HashMap<Location, Float>explosions = new HashMap<Location, Float>();
	private static HashMap<Location, List<FallingBlock>>implosions = new HashMap<Location, List<FallingBlock>>();

	public static void createExplosion(Location l, float s){
		explosions.put(l, s);
		l.getWorld().createExplosion(l, s);
	}
	
	public static void createImplosion(Location l, float s){
		implosions.put(l, null);
		createExplosion(l, s);
	}

	/* horrible hackery */
	@SuppressWarnings("deprecation")
	public static boolean blockNextTo(Location l){
		return l.add(new Vector(0,1,0)).getBlock().getTypeId() != 0 ||
				l.add(new Vector(0,-2,0)).getBlock().getTypeId() != 0 ||
				l.add(new Vector(1, 1, 0)).getBlock().getTypeId() != 0 ||
				l.add(new Vector(-2, 0, 0)).getBlock().getTypeId() != 0 ||
				l.add(new Vector(1, 0 , 1)).getBlock().getTypeId() !=0 ||
				l.add(new Vector(0, 0, -2)).getBlock().getTypeId() != 0;
	
	}

	
	public static void createRocket(Location l, final Vector vel){
		final Item ball = l.getWorld().dropItem(l,new ItemStack(Material.MAGMA_CREAM));

		ball.setVelocity(vel);
		
		Trigger t = new Trigger() {
			
			Long time = System.currentTimeMillis() + 250;

			@Override
			public void execute() {
				Effects.createExplosion(ball.getLocation(),5);
				ball.remove();
				
			}
			
			@Override
			public boolean check() {
				if((System.currentTimeMillis() > time && Effects.blockNextTo(ball.getLocation())) ||
						System.currentTimeMillis() > time + 10000){
					return true;
				}
				ball.setVelocity(vel);
				return false;
			}
		};
		
		
		RandomEventManager.getInstance().registerTrigger(t, 1);
	}


	@SuppressWarnings("deprecation")
	@EventHandler
	public void explosionHandler(EntityExplodeEvent e){
		Location l = getExplosion(e.getLocation());
		Location i = getImplosion(e.getLocation());

		if(l != null){
			List<Block> list = e.blockList();
			World w = e.getLocation().getWorld();
			ArrayList<FallingBlock> blocks = new ArrayList<FallingBlock>();
			for(Block b:list){
				if(b.getTypeId() != 0){
					blocks.add(w.spawnFallingBlock(b.getLocation(), b.getType(), b.getData()));
					b.setTypeId(0);
					b.getState().update();
				}
			}
			if(i != null){
				implosions.put(i, blocks);
			}
			list.clear();
			float size = explosions.get(l);
			explosions.remove(l);
			w.createExplosion(l, (float) (size+3));
		}else if(i != null){
			for(FallingBlock b:implosions.get(i)){
				b.setVelocity(b.getVelocity().multiply(-1));
			}
			implosions.remove(i);
			e.setCancelled(true);
		}
		else{
			e.setCancelled(true);
		}
		e.setYield(0);
	}


	public Location getExplosion(Location l1){
		for(Location l:explosions.keySet()){
			if(l.getBlockX() == l1.getBlockX() && l.getBlockZ() == l1.getBlockZ())
				return l;
		}
		return null;
	}

	public Location getImplosion(Location l1){
		for(Location l:implosions.keySet()){
			if(l.getBlockX() == l1.getBlockX() && l.getBlockZ() == l1.getBlockZ())
				return l;
		}
		return null;
	}


	
}

