package com.bukkit.erbros.Lottery;

import org.bukkit.event.player.PlayerListener;


public class LotteryPlayerListener extends PlayerListener {

	public static Lottery plugin;
	
	public LotteryPlayerListener(Lottery instance) {
		plugin = instance;
	}
	
}
