package org.mcsg.tenjava.random.util;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;


public class Region {

	private Location max;
	private Location min;
	
	public Region(Location loc1, Location loc2){
		sort(loc1, loc2);
	}
	
	public void sort(Location l1, Location l2){
		if(!l1.getWorld().equals(l2.getWorld()))
			throw new IllegalArgumentException("Worlds must be the same!");
		
		max = new Location(l1.getWorld(), 
				Math.max(l1.getBlockX(), l2.getBlockX()),
				Math.max(l1.getBlockY(), l2.getBlockY()),
				Math.max(l1.getBlockZ(), l2.getBlockZ()));
				
		min = new Location(l1.getWorld(), 
				Math.min(l1.getBlockX(), l2.getBlockX()),
				Math.min(l1.getBlockY(), l2.getBlockY()),
				Math.min(l1.getBlockZ(), l2.getBlockZ()));
		
	}
	
	public boolean isInside(Location l){
		int x = l.getBlockX();
		int y = l.getBlockY();
		int z = l.getBlockZ();
		
        return x >= min.getBlockX() && x < max.getBlockX() + 1 && y >= min.getBlockY() && y < max.getBlockY() + 1 && z >= min.getBlockZ() && z < max.getBlockZ() + 1; 
	}
	
	public Location getMax(){
		return max.clone();
	}
	
	public Location getMin(){
		return min.clone();
	}
	
	public Location getRandomLocation(){
		Random r = new Random();

		
		int x =  r.nextInt(getMax().getBlockX() - getMin().getBlockX()) + getMin().getBlockX();
		int y =  r.nextInt(getMax().getBlockY() - getMin().getBlockY()) + getMin().getBlockY();
		int z =  r.nextInt(getMax().getBlockZ() - getMin().getBlockZ()) + getMin().getBlockZ();

		return new Location(getMax().getWorld(), x, y, z);
		
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
	
	public Location getRandomSurface(){
		Location l = getRandomLocation();
		int x = l.getBlockX();
		int z = l.getBlockZ();
		int y = getY(l.getWorld(), x, z);
		
		return new Location(l.getWorld(), x, y, z);
	}
	
	public String toString(){
		return min.toString() + " - "+max.toString();
	}
	
}