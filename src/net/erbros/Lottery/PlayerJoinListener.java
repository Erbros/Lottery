package net.erbros.Lottery;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerJoinListener extends PlayerListener {
    private Lottery plugin;
    
    public PlayerJoinListener(Lottery plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Send the player some info about time until lottery draw?
        
        for (String msg : plugin.msgWelcome) {
            event.getPlayer().sendMessage(plugin.formatCustomMessageLive(msg, event.getPlayer()));
            plugin.debugMsg("Welcome msg sent: " + msg);
        }
    }

}
