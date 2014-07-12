package org.mcsg.tenjava.random.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.mcsg.tenjava.random.TenJava;
import org.mcsg.tenjava.random.util.Region;
import org.mcsg.tenjava.random.util.Settings;

public class MobDeathMatch implements TickableEvent, Listener{

	private static List<Player> inDeath = new ArrayList<Player>();
	
	private Region rg;
	private Region outer;
	private Player player;
	private Location center;
	private Monster mob;
	private Random rand = new Random();
	private List<Monster> mobs  = new ArrayList<Monster>();
	private List<Location> fire = new ArrayList<Location>();
	public <T extends Event> boolean isRandom(T event){
		return !inDeath.contains(((EntityDamageByEntityEvent) event).getDamager()) && rand.nextInt(Settings.options.MOB_DEATH_MATCH_RANDOM) == 1;
	}

	@Override
	public <T extends Event> void startEvent(T event) {
		EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
		if(ev.getEntityType() != EntityType.PLAYER && ev.getDamager().getType() == EntityType.PLAYER && ev.getEntity() instanceof Monster){
			mob = (Monster) ev.getEntity();
			player = (Player) ev.getDamager();
			inDeath.add(player);
			center = player.getLocation();
			Location pl = center.clone();
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
			player.sendMessage(ChatColor.GOLD+"Suddenly, a Death Match! Don't DIE");
			Bukkit.getPluginManager().registerEvents(this, TenJava.getPlugin());
		}
	}

	public void clearBorder(){
		fire.stream().forEach((loc) -> loc.getBlock().setType(Material.AIR));
		fire.clear();
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
		player.setFireTicks(0);
		
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
	public boolean spawned = false;
	public boolean end = false;
	Region trg; 
	@Override
	public boolean tick() {
		if(end || player == null) return true;
		tick ++;
		if(tick < 30 && tick % 2 == 0){
			int no = tick / 2;
			clearBorder();
			trg = new Region(center.clone().add(no, 0, no), center.clone().subtract(no,0,no));
			drawBorder(trg);
		}
		if(!spawned && tick >= 30){
			int a = new Random().nextInt(20);
			for(int b = 0; b < a; b++){
				Monster en = (Monster) center.getWorld().spawnEntity(rg.getRandomSurface(), mob.getType());
				en.setTarget(player);
				mobs.add(en);
			}
			spawned = true;
		}
		if(tick > 150 && mobs.size() == 0){
			Item item = center.getWorld().dropItemNaturally(rg.getRandomLocation(), new ItemStack(Material.values()[rand.nextInt(Material.values().length)], rand.nextInt(5)));
			player.sendMessage(ChatColor.AQUA + "You win! A "+item.getItemStack().getType()+" has dropped as a prize! ");

			endGame();
			return true;
		}
		return false;
	}

	public void endGame(){
		inDeath.remove(player);
		clearBorder();
		player = null;
		rg = null;
		outer = null;
		end = true;
	}
	
	@EventHandler
	public void target(EntityTargetLivingEntityEvent ev){
		if(mobs.contains(ev.getEntity()) && ev.getTarget() != player){
			ev.setTarget(player);
		}
	}

	@EventHandler
	public void playerMove(PlayerMoveEvent ev){
		if(player != null && ev.getPlayer() == player && !rg.isInside(ev.getTo())){
			ev.setTo(ev.getFrom());
		}
	}

	@EventHandler
	public void fireSpread(BlockFadeEvent ev){
		if(outer != null &&  outer.isInside(ev.getBlock().getLocation())){
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void fireSpread(BlockBurnEvent ev){
		if(outer != null && outer.isInside(ev.getBlock().getLocation())){

			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void fireSpread(BlockSpreadEvent ev){
		if(outer != null && outer.isInside(ev.getBlock().getLocation())){
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void killEnt(EntityDeathEvent ev){
		mobs.remove(ev.getEntity());
	}

	@EventHandler
	public void killPlayer(PlayerDeathEvent ev){
		if(ev.getEntity() == player){
			player.sendMessage(ChatColor.RED+"You lose :(");
			endGame();
		}
	}
}
