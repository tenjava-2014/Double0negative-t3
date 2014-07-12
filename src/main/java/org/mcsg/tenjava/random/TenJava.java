package org.mcsg.tenjava.random;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcsg.tenjava.random.util.Effects;

public class TenJava extends JavaPlugin {
	
	private static Plugin plugin;
	
	public void onEnable(){
		this.plugin = this;
		
		RandomEventManager.getInstance().setup();
		Bukkit.getPluginManager().registerEvents(new Effects(), this);
	}
	
	public void onDisable(){
		
	}
	
	public static Plugin getPlugin(){
		return plugin;
	}
	
	public static void fireEvent(Event event){
	    Bukkit.getServer().getPluginManager().callEvent(event);
	}
	
}
