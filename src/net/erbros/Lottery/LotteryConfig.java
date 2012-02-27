package net.erbros.Lottery;

import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.configuration.file.FileConfiguration;

public class LotteryConfig {

    private Lottery plugin;
    private FileConfiguration config = null;

    public LotteryConfig(final Lottery plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    public void configReplacePath(String pathFrom, String pathReplace, boolean deleteFrom) {
        if (config.contains(pathFrom)) {
            // Let's check for booleans etc so we can save the objects correctly.
            if (config.isBoolean(pathReplace) && config.isBoolean(pathFrom)) {
                config.set(pathReplace, config.getBoolean(pathFrom));
                debugMsg("Got value from old path: " + pathFrom + ", to new: " + pathReplace);
            } else {
                debugMsg("isBoolean: " + pathReplace + "(" + config.isBoolean(pathReplace) + ") :: isBoolean: " + pathFrom + "(" + config.isBoolean(pathFrom) + ")");
            }
            // Int?
            if (config.isInt(pathReplace) && config.isInt(pathFrom)) {
                config.set(pathReplace, config.getInt(pathFrom));
                debugMsg("Got value from old path: " + pathFrom + ", to new: " + pathReplace);
            } else {
                debugMsg("isBoolean: " + pathReplace + "(" + config.isBoolean(pathReplace) + ") :: isBoolean: " + pathFrom + "(" + config.isBoolean(pathFrom) + ")");
            }
            if (config.isLong(pathReplace) && config.isLong(pathFrom)) {
                config.set(pathReplace, config.getLong(pathFrom));
                debugMsg("Got value from old path: " + pathFrom + ", to new: " + pathReplace);
            } else {
                debugMsg("isBoolean: " + pathReplace + "(" + config.isBoolean(pathReplace) + ") :: isBoolean: " + pathFrom + "(" + config.isBoolean(pathFrom) + ")");
            }
            if (config.isBoolean(pathReplace) && config.isBoolean(pathFrom)) {
                config.set(pathReplace, config.getBoolean(pathFrom));
                debugMsg("Got value from old path: " + pathFrom + ", to new: " + pathReplace);
            } else {
                debugMsg("isBoolean: " + pathReplace + "(" + config.isBoolean(pathReplace) + ") :: isBoolean: " + pathFrom + "(" + config.isBoolean(pathFrom) + ")");
            }
            if (config.isString(pathReplace) && config.isString(pathFrom)) {
                config.set(pathReplace, config.getString(pathFrom));
                debugMsg("Got value from old path: " + pathFrom + ", to new: " + pathReplace);
            } else {
                debugMsg("isBoolean: " + pathReplace + "(" + config.isBoolean(pathReplace) + ") :: isBoolean: " + pathFrom + "(" + config.isBoolean(pathFrom) + ")");
            }

            // Should we remove the old path?
            if (deleteFrom) {
                config.set(pathFrom, null);
            }
        }
    }

    public void loadConfig() {
        plugin.reloadConfig();

        debugMsg("Loading Lottery configuration");

        plugin.hours = config.getDouble("config.hours", 24);
        Lottery.useiConomy = config.getBoolean("config.useiConomy", true);
        Lottery.material = config.getInt("config.material", 266);
        plugin.broadcastBuying = config.getBoolean("config.broadcastBuying", true);
        plugin.welcomeMessage = config.getBoolean("config.welcomeMessage", true);
        plugin.extraInPot = config.getDouble("config.extraInPot", 0);
        plugin.clearExtraInPot = config.getBoolean("config.clearExtraInPot", true);
        plugin.netPayout = config.getDouble("config.netPayout", 100);
        plugin.maxTicketsEachUser = config.getInt("config.maxTicketsEachUser", 1);
        plugin.TicketsAvailable = config.getInt("config.numberOfTicketsAvailable", 0);
        plugin.jackpot = config.getDouble("config.jackpot", 0);
        Lottery.nextexec = config.getLong("config.nextexec");
        Lottery.cost = Etc.formatAmount(config.getDouble("config.cost", 5), Lottery.useiConomy);

        // Load messages?
        loadCustomMessages();
        // Then lets save this stuff :)
        plugin.saveConfig();
    }

    public void loadCustomMessages() {
        plugin.msgWelcome = formatCustomMessage("message.welcome", "&6[LOTTERY] &fDraw in: &c%drawLong%");
    }

    public ArrayList<String> formatCustomMessage(String node, String def) {
        final ArrayList<String> fList = new ArrayList<String>();
        // Lets find a msg.
        final String msg = config.getString(node, def);
        config.set(node, msg);

        // Lets put this in a arrayList in case we want more than one line.
        Collections.addAll(fList, msg.split("%newline%"));
        return fList;
    }

    // Enable some debugging?
    public void debugMsg(String msg) {
        if (config.getBoolean("config.debug") == true) {
            if (msg != null) {
                Lottery.log.info(msg);
            }
        }
    }
}
