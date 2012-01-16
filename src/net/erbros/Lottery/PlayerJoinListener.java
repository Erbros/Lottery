package net.erbros.Lottery;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerJoinListener extends PlayerListener {
    private Lottery plugin;
    private Etc etc;
    
    public PlayerJoinListener(Lottery plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Send the player some info about time until lottery draw?
        
        for (String msg : plugin.msgWelcome) {
            event.getPlayer().sendMessage(etc.formatCustomMessageLive(msg, event.getPlayer()));
            etc.debugMsg("Welcome msg sent: " + msg);
        }
    }

}
