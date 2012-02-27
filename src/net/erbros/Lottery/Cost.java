package net.erbros.Lottery;

import org.bukkit.configuration.Configuration;

public class Cost {

    private Lottery plugin;
    private Configuration config;

    public Cost(Lottery plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public int getCostInt() {
        int cost = (int) config.getDouble("config.cost", 5);
        // Let's just set this in the config so there is no confusion.
        config.set("config.cost", cost);

        return cost;
    }

    public double getCostDouble() {
        double cost = (int) config.getDouble("config.cost", 5);
        return cost;
    }
}
