package net.erbros.Lottery;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import net.erbros.Lottery.Lottery;
import net.erbros.Lottery.Etc;

public class PlayerJoinListener extends PlayerListener {
	public PlayerJoinListener() { }
		
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Send the player some info about time until lottery draw?
		event.getPlayer().sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Draw in: " + ChatColor.RED + Etc.timeUntil(Lottery.nextexec));
	}
	
}
	
	