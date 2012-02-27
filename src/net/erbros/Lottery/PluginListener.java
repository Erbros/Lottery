package net.erbros.Lottery;

import net.erbros.Lottery.register.payment.Methods;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginListener implements Listener {

    private Lottery plugin;
    private Methods Methods = null;

    public PluginListener(Lottery plugin) {
        this.plugin = plugin;
        this.Methods = new Methods();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        if (this.Methods != null && net.erbros.Lottery.register.payment.Methods.hasMethod()) {
            Boolean check = net.erbros.Lottery.register.payment.Methods.checkDisabled(event.getPlugin());

            if (check) {
                this.plugin.Method = null;
                System.out.println("[Lottery] Payment method was disabled. No longer accepting payments.");
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent event) {
        if (!net.erbros.Lottery.register.payment.Methods.hasMethod()) {
            if (net.erbros.Lottery.register.payment.Methods.setMethod(Bukkit.getPluginManager())) {
                // You might want to make this a public variable inside your
                // MAIN class public Method Method = null;
                // then reference it through this.plugin.Method so that way you
                // can use it in the rest of your plugin ;)
                this.plugin.Method = net.erbros.Lottery.register.payment.Methods.getMethod();
                System.out.println("[Lottery] Payment method found ("
                        + this.plugin.Method.getName() + " version: "
                        + this.plugin.Method.getVersion() + ")");
            }
        }
    }
}