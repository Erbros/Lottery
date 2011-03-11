package net.erbros.Lottery;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerChatEvent;



public class LotteryPlayerListener extends PlayerListener {

	public static Lottery plugin;
	
	public LotteryPlayerListener(Lottery instance) {
		plugin = instance;
	}
	public void onPlayerCommand(PlayerChatEvent event) {
		String[] split = event.getMessage().split(" ");
		Player player = event.getPlayer();
		plugin.log.info(split[0] +" and " + split[1]);
		if(split[0].equalsIgnoreCase("/lottery")) {
			// Checking if split[1] == null so I dont get any NullPointerExceptions.
			if(split[1] == null) {
				// Basic lottery info to the player.
			} else {
				if(split[1].equalsIgnoreCase("buy")) {
					// Give the player a lottery ticket, and take some money from him/her.
					if(plugin.addPlayer(player) == true) {
						// You got your ticket. Change coins to iconomy config later.
						player.sendMessage("You got your lottery ticket for " + plugin.cost + "coins");
					} else {
						// You can't buy more than one ticket.
						player.sendMessage("You allready had a ticket. Wait until next round to buy another.");
					}
				}
			}
		}
	}
	
}