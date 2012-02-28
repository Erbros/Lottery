package net.erbros.Lottery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;

public class LotteryConfig {

    private Lottery plugin;
    private FileConfiguration config = null;
    
    private double cost;
    private double hours;
    private long nextexec;  
    private boolean useiConomy;
    private int material;
    private double extraInPot;
    private boolean broadcastBuying;
    private boolean welcomeMessage;
    private double netPayout;
    private boolean clearExtraInPot;
    private int maxTicketsEachUser;
    private int ticketsAvailable;
    private double jackpot;
    private String lastwinner;
    private double lastwinneramount;
    private List<String> msgWelcome;
    

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

        hours = config.getDouble("config.hours", 24);
        useiConomy = config.getBoolean("config.useiConomy", true);
        material = config.getInt("config.material", 266);
        broadcastBuying = config.getBoolean("config.broadcastBuying", true);
        welcomeMessage = config.getBoolean("config.welcomeMessage", true);
        extraInPot = config.getDouble("config.extraInPot", 0);
        clearExtraInPot = config.getBoolean("config.clearExtraInPot", true);
        netPayout = config.getDouble("config.netPayout", 100);
        maxTicketsEachUser = config.getInt("config.maxTicketsEachUser", 1);
        ticketsAvailable = config.getInt("config.numberOfTicketsAvailable", 0);
        jackpot = config.getDouble("config.jackpot", 0);
        nextexec = config.getLong("config.nextexec");
        cost = Etc.formatAmount(config.getDouble("config.cost", 5), useiConomy);
        lastwinner = config.getString("config.lastwinner", "");
        lastwinneramount = config.getDouble("config.lastwinneramount", 0);

        // Load messages?
        loadCustomMessages();
        // Then lets save this stuff :)
        plugin.saveConfig();
    }

    public void loadCustomMessages() {
        msgWelcome = formatCustomMessage("message.welcome", "&6[LOTTERY] &fDraw in: &c%drawLong%");
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
        config.set("config.cost", cost);
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
        config.set("config.hours", hours);
    }

    public long getNextexec() {
        return nextexec;
    }

    public void setNextexec(long nextexec) {
        this.nextexec = nextexec;
        config.set("config.nextexec", nextexec);
    }

    public boolean useiConomy() {
        return useiConomy;
    }

    public int getMaterial() {
        return material;
    }

    public double getExtraInPot() {
        return extraInPot;
    }

    public void setExtraInPot(double extraInPot) {
        this.extraInPot = extraInPot;
        config.set("config.extraInPot", extraInPot);
    }
    
    public void addExtraInPot(double extra) {
        extraInPot += extra;
        config.set("config.extraInPot", extraInPot);
    }

    public boolean useBroadcastBuying() {
        return broadcastBuying;
    }

    public boolean useWelcomeMessage() {
        return welcomeMessage;
    }

    public double getNetPayout() {
        return netPayout;
    }

    public void setNetPayout(double netPayout) {
        this.netPayout = netPayout;
        config.set("config.netPayout", netPayout);
    }

    public boolean clearExtraInPot() {
        return clearExtraInPot;
    }

    public int getMaxTicketsEachUser() {
        return maxTicketsEachUser;
    }
    
    public void setMaxTicketsEachUser(int maxTicketsEachUser) {
        this.maxTicketsEachUser = maxTicketsEachUser;
        config.set("config.maxTicketsEachUser", maxTicketsEachUser);
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public double getJackpot() {
        return jackpot;
    }

    public void setJackpot(double jackpot) {
        this.jackpot = jackpot;        
        config.set("config.jackpot", jackpot);        
    }

    public List<String> getMsgWelcome() {
        return msgWelcome;
    }

    public String getLastwinner() {
        return lastwinner;
    }

    public void setLastwinner(String lastwinner) {
        this.lastwinner = lastwinner;
        config.set("config.lastwinner", lastwinner);     
    }

    public double getLastwinneramount() {
        return lastwinneramount;
    }

    public void setLastwinneramount(double lastwinneramount) {
        this.lastwinneramount = lastwinneramount;
        config.set("config.lastwinneramount", lastwinneramount);     
    }
}
