package net.erbros.Lottery;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private Lottery plugin;
    private Etc etc;

    public PlayerJoinListener(Lottery plugin) {
        this.plugin = plugin;
        this.etc = plugin.etc;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Send the player some info about time until lottery draw?

        for (String msg : plugin.msgWelcome) {
            event.getPlayer().sendMessage(etc.formatCustomMessageLive(msg, event.getPlayer()));
            etc.debugMsg("Welcome msg sent: " + msg);
        }
    }
}
