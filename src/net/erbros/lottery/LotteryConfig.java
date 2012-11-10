package net.erbros.lottery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;


public class LotteryConfig
{

	final private Lottery plugin;
	private FileConfiguration config;
	private double cost;
	private double hours;
	private long nextexec;
	private boolean useiConomy;
	private int material;
	private double extraInPot;
	private boolean broadcastBuying;
	private int broadcastBuyingTime;
	private boolean welcomeMessage;
	private double netPayout;
	private boolean clearExtraInPot;
	private int maxTicketsEachUser;
	private int ticketsAvailable;
	private double jackpot;
	private String lastwinner;
	private double lastwinneramount;
	private boolean buyingExtendDeadline;
	private int buyingExtendRemaining;
	private double buyingExtendBase;
	private double buyingExtendMultiplier;
	private String taxTarget;

	private List<String> msgWelcome;

	public LotteryConfig(final Lottery plugin)
	{
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}

	public void loadConfig()
	{
		plugin.reloadConfig();
		config = plugin.getConfig();

		debugMsg("Loading Lottery configuration");

		hours = config.getDouble("config.hours", 24);

		useiConomy = config.getBoolean("config.useiConomy", true);
		material = config.getInt("config.material", 266);
		broadcastBuying = config.getBoolean("config.broadcastBuying", true);
		broadcastBuyingTime = config.getInt("config.broadcastBuyingTime", 120);
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
		buyingExtendDeadline = config.getBoolean("config.buyingExtend.enabled", true);
		buyingExtendRemaining = config.getInt("config.buyingExtend.secondsRemaining", 30);
		buyingExtendBase = config.getDouble("config.buyingExtend.extendBase", 15);
		buyingExtendMultiplier = config.getDouble("config.buyingExtend.extendMultiplier", 1.5);
		taxTarget = config.getString("config.taxTarget", "");

		// Load messages?
		loadCustomMessages();
		// Then lets save this stuff :)
		plugin.saveConfig();
	}

	public void set(final String path, final Object value)
	{
		config.set(path, value);
		plugin.saveConfig();
	}

	public void loadCustomMessages()
	{
		msgWelcome = formatCustomMessage("message.welcome", "&6[LOTTERY] &fDraw in: &c%drawLong%");
	}

	public List<String> formatCustomMessage(final String node, final String def)
	{
		final List<String> fList = new ArrayList<String>();
		// Lets find a msg.
		final String msg = config.getString(node, def);
		config.set(node, msg);

		// Lets put this in a arrayList in case we want more than one line.
		Collections.addAll(fList, msg.split("%newline%"));
		return fList;
	}

	// Enable some debugging?
	public void debugMsg(final String msg)
	{
		if (config.getBoolean("config.debug") && msg != null)
		{
			plugin.getLogger().log(Level.INFO, msg);
		}
	}

	public double getCost()
	{
		return cost;
	}

	public void setCost(final double cost)
	{
		this.cost = cost;
		set("config.cost", cost);
	}

	public double getHours()
	{
		return hours;
	}

	public void setHours(final double hours)
	{
		this.hours = hours;
		set("config.hours", hours);
	}

	public long getNextexec()
	{
		return nextexec;
	}

	public void setNextexec(final long nextexec)
	{
		this.nextexec = nextexec;
		set("config.nextexec", nextexec);
	}

	public boolean useiConomy()
	{
		return useiConomy;
	}

	public int getMaterial()
	{
		return material;
	}

	public double getExtraInPot()
	{
		return extraInPot;
	}

	public void setExtraInPot(final double extraInPot)
	{
		this.extraInPot = extraInPot;
		set("config.extraInPot", extraInPot);
	}

	public void addExtraInPot(final double extra)
	{
		extraInPot += extra;
		setExtraInPot(extraInPot);
	}

	public boolean useBroadcastBuying()
	{
		return broadcastBuying;
	}

	public int getBroadcastBuyingTime()
	{
		return broadcastBuyingTime;
	}

	public boolean useWelcomeMessage()
	{
		return welcomeMessage;
	}

	public double getNetPayout()
	{
		return netPayout;
	}

	public void setNetPayout(final double netPayout)
	{
		this.netPayout = netPayout;
		set("config.netPayout", netPayout);
	}

	public boolean clearExtraInPot()
	{
		return clearExtraInPot;
	}

	public int getMaxTicketsEachUser()
	{
		return maxTicketsEachUser;
	}

	public void setMaxTicketsEachUser(final int maxTicketsEachUser)
	{
		this.maxTicketsEachUser = maxTicketsEachUser;
		set("config.maxTicketsEachUser", maxTicketsEachUser);
	}

	public int getTicketsAvailable()
	{
		return ticketsAvailable;
	}

	public double getJackpot()
	{
		return jackpot;
	}

	public void setJackpot(final double jackpot)
	{
		this.jackpot = jackpot;
		set("config.jackpot", jackpot);
	}

	public List<String> getMsgWelcome()
	{
		return Collections.unmodifiableList(msgWelcome);
	}

	public String getLastwinner()
	{
		return lastwinner;
	}

	public void setLastwinner(final String lastwinner)
	{
		this.lastwinner = lastwinner;
		set("config.lastwinner", lastwinner);
	}

	public double getLastwinneramount()
	{
		return lastwinneramount;
	}

	public void setLastwinneramount(final double lastwinneramount)
	{
		this.lastwinneramount = lastwinneramount;
		set("config.lastwinneramount", lastwinneramount);
	}

	public boolean isBuyingExtendDeadline()
	{
		return buyingExtendDeadline;
	}

	public int getBuyingExtendRemaining()
	{
		return buyingExtendRemaining;
	}

	public double getBuyingExtendBase()
	{
		return buyingExtendBase;
	}

	public double getBuyingExtendMultiplier()
	{
		return buyingExtendMultiplier;
	}

	public String getTaxTarget()
	{
		return taxTarget;
	}
}
