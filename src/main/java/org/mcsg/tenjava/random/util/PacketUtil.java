package org.mcsg.tenjava.random.util;

import net.minecraft.server.v1_7_R3.Packet;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtil {

	public static void sendPacketToPlayer(Player player, Packet packet){
		CraftPlayer cp = (CraftPlayer)player;
		cp.getHandle().playerConnection.sendPacket(packet);
	}
	
	public static void sendPacketToAll(Packet packet){
		for(Player player : Bukkit.getOnlinePlayers()){
			sendPacketToPlayer(player, packet);
		}
	}
	
	
}

