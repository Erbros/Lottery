package com.bukkit.erbros.Lottery;

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
		if(split[0].equalsIgnoreCase("/lottery")) {
			// Checking if split[1] == null so I dont get any NullPointerExceptions.
			if(split[1] == null) {
				// Basic lottery info to the player.
			} else {
				if(split[1].equalsIgnoreCase("buy")) {
					// Give the player a lottery ticket, and take some money from him/her.
				}
			}
		}
	}
}
