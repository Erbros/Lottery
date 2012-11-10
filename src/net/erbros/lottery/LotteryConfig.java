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

	private List<String> msgprefix;

	private List<String> msgWelcome;
	private List<String> msgErrorPlugin;
	private List<String> msgErrorAccess;
	private List<String> msgErrorCommand;
	private List<String> msgErrorConsole;
	private List<String> msgErrorConsole2;
	private List<String> msgErrorNoAvailable;
	private List<String> msgErrorAtMax;
	private List<String> msgErrorNotAfford;
	private List<String> msgErrorNumber;
	private List<String> msgErrorClaim;

	private List<String> msgTicketCommand;
	private List<String> msgPotAmount;
	private List<String> msgYourTickets;
	private List<String> msgTimeRemaining;
	private List<String> msgCommandHelp;
	private List<String> msgLastWinner;
	private List<String> msgCheckClaim;
	private List<String> msgBoughtTicket;
	private List<String> msgBoughtTickets;
	private List<String> msgBoughtAnnounceDraw;
	private List<String> msgBoughtAnnounce;
	private List<String> msgDrawIn;
	private List<String> msgDrawNow;
	private List<String> msgDrawSoon;
	private List<String> msgDrawSoonLong;
	private List<String> msgPlayerClaim;

	private List<String> msgNoWinnerTickets;
	private List<String> msgNoWinnerRollover;
	private List<String> msgWinnerCongrat;
	private List<String> msgWinnerCongratClaim;
	private List<String> msgWinnerSummary;

	private List<String> msgAddToPot;
	private List<String> msgConfigCost;
	private List<String> msgConfigHours;
	private List<String> msgConfigMax;
	private List<String> msgConfigReload;

	private List<String> msgHelp1;
	private List<String> msgHelp2;
	private List<String> msgHelp3;
	private List<String> msgHelp4;
	private List<String> msgHelp5;
	private List<String> msgHelp6;
	private List<String> msgHelp7;
	private List<String> msgHelp8;

	private List<String> msgHelpPot;

	private List<String> msgHelpConfig1;
	private List<String> msgHelpConfig2;
	private List<String> msgHelpConfig3;
	private List<String> msgHelpConfig4;
	private List<String> msgHelpConfig5;

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
		msgprefix = formatCustomMessage("message.prefix", "&6[LOTTERY]&r");

		msgWelcome = formatCustomMessage("message.Welcome", "%prefix% &fDraw in: &c%drawLong%");
		msgErrorPlugin = formatCustomMessage("message.ErrorPlugin", "%prefix% Sorry, we haven\'t found a money plugin yet..");
		msgErrorAccess = formatCustomMessage("message.ErrorAccess", "%prefix% You don\'t have access to that command.");
		msgErrorCommand = formatCustomMessage("message.ErrorCommand", "%prefix% Hey, I don\'t recognize that command!");
		msgErrorConsole = formatCustomMessage("message.ErrorConsole", "%prefix% You\'re the console, I can\'t sell you tickets.");
		msgErrorConsole2 = formatCustomMessage("message.ErrorConsole2", "%prefix% You\'re the console, you don\'t have an inventory.");
		msgErrorNoAvailable = formatCustomMessage("message.ErrorNoAvailable", "%prefix% There are no more tickets available");
		msgErrorAtMax = formatCustomMessage("message.ErrorAtMax", "%prefix% You already have the maximum of %0% %1% already.");
		msgErrorNotAfford = formatCustomMessage("message.ErrorNotAfford", "%prefix% You can\'t afford a ticket");
		msgErrorNumber = formatCustomMessage("message.ErrorNumber", "%prefix% Provide a number greater than zero (decimals accepted)");
		msgErrorClaim = formatCustomMessage("message.ErrorClaim", "%prefix% You did not have anything unclaimed.");

		msgTicketCommand = formatCustomMessage("message.TicketCommand", "%prefix% Buy a ticket for &c%cost% &rwith &c/lottery buy");
		msgPotAmount = formatCustomMessage("message.PotAmount", "%prefix% There is currently &a%pot% &rin the pot.");
		msgYourTickets = formatCustomMessage("message.YourTickets", "%prefix% You have &c%0% &r%1%");
		msgTimeRemaining = formatCustomMessage("message.TimeRemaining", "%prefix% There is &c%0% &r%1% left.");
		msgCommandHelp = formatCustomMessage("message.CommandHelp", "%prefix% &c/lottery help&r for other commands");
		msgLastWinner = formatCustomMessage("message.LastWinner", "%prefix% Last winner: %0% %1%");
		msgCheckClaim = formatCustomMessage("message.CheckClaim", "%prefix% Check if you have won with &c/lottery claim");
		msgBoughtTicket = formatCustomMessage("message.BoughtTicket", "%prefix% You got &c%0% &r%1% for %c%2%");
		msgBoughtTickets = formatCustomMessage("message.BoughtTickets", "%prefix% You now have &c%0% &r%1%");
		msgBoughtAnnounceDraw = formatCustomMessage("message.BoughtAnnounceDraw", "%prefix% &r%0% &rjust bought %1% %2%! Draw in %3%");
		msgBoughtAnnounce = formatCustomMessage("message.BoughtAnnounce", "%prefix% &r%0% &rjust bought %1% %2%!");
		msgDrawIn = formatCustomMessage("message.DrawIn", "%prefix% Draw in: &c%0%");
		msgDrawNow = formatCustomMessage("message.DrawNow", "%prefix% Lottery will be drawn at once.");
		msgDrawSoon = formatCustomMessage("message.DrawSoon", "Soon");
		msgDrawSoonLong = formatCustomMessage("message.DrawSoonLong", "Draw will occur soon!");
		msgPlayerClaim = formatCustomMessage("message.PlayerClaim", "%prefix You just claimed %0%");

		msgNoWinnerTickets = formatCustomMessage("message.NoWinnerTickets", "%prefix% No tickets sold this round. Thats a shame.");
		msgNoWinnerRollover = formatCustomMessage("message.NoWinnerRollover", "%prefix% No winner, we have a rollover! &a%0% &rwent to jackpot!");
		msgWinnerCongrat = formatCustomMessage("message.WinnerCongrat", "%prefix% Congratulations go to %0% &rfor winning &c%1%.");
		msgWinnerCongratClaim = formatCustomMessage("message.WinnerCongratClaim", "%prefix% Use &c/lottery claim &rto claim the winnings.");
		msgWinnerSummary = formatCustomMessage("message.WinnerSummary", "%prefix% There was a total of %0% %1% buying %2% %3%");

		msgAddToPot = formatCustomMessage("message.AddToPot", "%prefix% Added &a%0% &rto pot. Extra total is &a %1%");
		msgConfigCost = formatCustomMessage("message.ConfigCost", "%prefix% Cost changed to &c%0%");
		msgConfigHours = formatCustomMessage("message.ConfigHours", "%prefix% Hours changed to &c%0%");
		msgConfigMax = formatCustomMessage("message.ConfigMax", "%prefix% Max amount of tickets changed to &c%0%");
		msgConfigReload = formatCustomMessage("message.ConfigReload", "%prefix% Config reloaded");

		msgHelp1 = formatCustomMessage("message.Help1", "%prefix% Help commands");
		msgHelp2 = formatCustomMessage("message.Help2", "%prefix% &c/lottery&r : Basic lottery info.");
		msgHelp3 = formatCustomMessage("message.Help3", "%prefix% &c/lottery buy <n>&r : Buy ticket(s).");
		msgHelp4 = formatCustomMessage("message.Help4", "%prefix% &c/lottery claim&r : Claim outstandig wins.");
		msgHelp5 = formatCustomMessage("message.Help5", "%prefix% &c/lottery winners&r : Check last winners.");
		msgHelp6 = formatCustomMessage("message.Help6", "%prefix% &1/lottery draw&r : Draw lottery.");
		msgHelp7 = formatCustomMessage("message.Help7", "%prefix% &1/lottery addtopot&r : Add number to pot.");
		msgHelp8 = formatCustomMessage("message.Help8", "%prefix% &1/lottery config&r : Edit the config.");

		msgHelpPot = formatCustomMessage("message.HelpPot", "%prefix% /lottery addtopot <number>");

		msgHelpConfig1 = formatCustomMessage("message.HelpConfig1", "%prefix% Edit config commands");
		msgHelpConfig2 = formatCustomMessage("message.HelpConfig2", "%prefix% &c/lottery config cost <i>");
		msgHelpConfig3 = formatCustomMessage("message.HelpConfig3", "%prefix% &c/lottery config hours <i>");
		msgHelpConfig4 = formatCustomMessage("message.HelpConfig4", "%prefix% &c/lottery config maxTicketsEachUser <i>");
		msgHelpConfig5 = formatCustomMessage("message.HelpConfig5", "%prefix% &c/lottery config reload");
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
