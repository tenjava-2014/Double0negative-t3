package org.mcsg.tenjava.random;

import java.io.File;
import java.io.IOException;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcsg.tenjava.random.util.Effects;
import org.mcsg.tenjava.random.util.FileUtils;
import org.mcsg.tenjava.random.util.Settings;
import org.mcsg.tenjava.random.util.Settings.Options;

public class TenJava extends JavaPlugin {

	private static Plugin plugin;
	public static File settings;
	public void onEnable(){
		this.plugin = this;

		RandomEventManager.getInstance().setup();
		Bukkit.getPluginManager().registerEvents(new Effects(), this);

		File dir = this.getDataFolder();
		if(!dir.exists()){
			dir.mkdir();
		} 

		settings =  new File(dir, "settings.json");
		try {
			String json = FileUtils.readFile(settings);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			if(json == null || json.length() == 0){
				Settings.options = new Options();
			} else {
				Settings.options = gson.fromJson(json, Options.class);
			}
			json = gson.toJson(Settings.options);
			FileUtils.writeFile(settings, json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
