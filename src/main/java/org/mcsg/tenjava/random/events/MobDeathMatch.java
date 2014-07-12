package org.mcsg.tenjava.random.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.mcsg.tenjava.random.TenJava;
import org.mcsg.tenjava.random.util.Region;

public class MobDeathMatch implements TickableEvent, Listener{

	private Region rg;
	private Region outer;
	private Player player;
	private List<Monster> mobs  = new ArrayList<Monster>();
	private List<Location> fire = new ArrayList<Location>();
	public boolean isRandom(){
		return true;
	}

	@Override
	public <T extends Event> void startEvent(T event) {
		EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
		if(ev.getEntityType() != EntityType.PLAYER && ev.getDamager().getType() == EntityType.PLAYER && ev.getEntity() instanceof Monster){
			Monster ent = (Monster) ev.getEntity();
			player = (Player) ev.getDamager();

			Location pl= player.getLocation();
			Location l1 = pl.clone().add(15, 255, 15);
			Location l2 = pl.clone().add(-15, -pl.getY(),-15);

			rg = new Region(l1, l2);
			outer = new Region(rg.getMax().add(10, 0, 10), rg.getMin().add(-10, 0, -10));
			System.out.println(rg);
			System.out.println(outer);
			for(Entity entity : pl.getWorld().getEntities()){
				if(rg.isInside(entity.getLocation()) && !(entity instanceof Player)){
					entity.remove();
				}
			}

			int a = new Random().nextInt(15);
			for(int b = 0; b < a; b++){
				Monster en = (Monster) pl.getWorld().spawnEntity(rg.getRandomSurface(), ent.getType());
				en.setTarget(player);
				mobs.add(en);
			}

			player.sendMessage(ChatColor.GOLD+"Suddenly, a Death Match! Don't DIE");
			drawBorder(rg);
			Bukkit.getPluginManager().registerEvents(this, TenJava.getPlugin());
		}
	}

	public void drawBorder(Region rg){
		for(int x = rg.getMin().getBlockX() ; x < rg.getMax().getBlockX(); x++){
			{ 
				int z = rg.getMax().getBlockZ();
				int y = getY(rg.getMax().getWorld(), x, z);
				Location l = new Location(rg.getMax().getWorld(), x, y, z);
				l.getBlock().setType(Material.FIRE);
				fire.add(l);
			}
			{ 
				int z = rg.getMin().getBlockZ();
				int y = getY(rg.getMax().getWorld(), x, z);
				Location l = new Location(rg.getMax().getWorld(), x, y, z);
				l.getBlock().setType(Material.FIRE);
				fire.add(l);

			}
		}
		for(int z = rg.getMin().getBlockZ() ; z < rg.getMax().getBlockZ(); z++){
			{ 
				int x = rg.getMax().getBlockX();
				int y = getY(rg.getMax().getWorld(), x, z);
				Location l = new Location(rg.getMax().getWorld(), x, y, z);
				l.getBlock().setType(Material.FIRE);
				fire.add(l);

			}
			{ 
				int x = rg.getMin().getBlockX();
				int y = getY(rg.getMax().getWorld(), x, z);
				Location l = new Location(rg.getMax().getWorld(), x, y, z);
				l.getBlock().setType(Material.FIRE);
				fire.add(l);

			}
		}
	}


	public int getY(World w, int x, int z){
		int y = 255;
		Material type = new Location(w, x, y, z).getBlock().getType();
		while(type == Material.AIR || type == Material.LONG_GRASS || type == Material.RED_ROSE){
			y--;
			type = new Location(w, x, y, z).getBlock().getType();
		}
		return y + 1;
	}

	@Override
	public RandomEvent getInstance() {
		return new MobDeathMatch();
	}

	int tick = 0;
	@Override
	public boolean tick() {
		tick ++;
		if(mobs.size() == 0){
			
			
			return true;
		}
		return false;
	}

	@EventHandler
	public void target(EntityTargetLivingEntityEvent ev){
		if(mobs.contains(ev.getEntity()) && ev.getTarget() != player){
			ev.setTarget(player);
		}
	}

	@EventHandler
	public void playerMove(PlayerMoveEvent ev){
		if(ev.getPlayer() == player && !rg.isInside(ev.getTo())){
			ev.setTo(ev.getFrom());
		}
	}

	@EventHandler
	public void fireSpread(BlockFadeEvent ev){
		if(outer.isInside(ev.getBlock().getLocation())){
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void fireSpread(BlockBurnEvent ev){
		if(outer.isInside(ev.getBlock().getLocation())){

			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void fireSpread(BlockSpreadEvent ev){
		if(outer.isInside(ev.getBlock().getLocation())){
			ev.setCancelled(true);
		}
	}


	@EventHandler
	public void fireSpread(BlockIgniteEvent ev){
		ev.setCancelled(true);
	}
}
