package org.mcsg.tenjava.random.util;

import org.bukkit.Location;

import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

public class ParticalEffect {

	public enum Partical {
		Smoke("smoke"),
		;
		
		
		public String name;
		private Partical(String name){
			this.name = name;
		}
		public String getName(){
			return this.name;
		}
	}
	
	
	public void playEffect(Partical part , Location loc ){
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(part.getName(),(float) loc.getX(), (float)loc.getY(), (float)loc.getZ(), 0, 0, 0, 1, 10);
		PacketUtil.sendPacketToAll(packet);
	}
	
	
	
	
}
