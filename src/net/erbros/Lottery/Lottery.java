package net.erbros.Lottery;
//All the imports
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.coelho.iConomy.iConomy;
import com.nijiko.coelho.iConomy.system.Account;

import net.erbros.Lottery.PluginListener;
import net.erbros.Lottery.Etc;
import net.erbros.Lottery.LotteryCommand;




public class Lottery extends JavaPlugin{

	protected static Integer cost;
	protected Integer hours;
	protected static Long nextexec;
	protected Boolean timerStarted;
	protected Boolean useiConomy;
	protected Integer material;
	protected Boolean broadcastBuying;
	protected Boolean welcomeMessage;
	protected Configuration c;
	// Starting timer we are going to use for scheduling.
	Timer timer;
	// The iConomy variables.
	private static PluginListener PluginListener = null;
	private static PlayerJoinListener PlayerListener = null;
	protected static iConomy iConomy = null;
	protected static org.bukkit.Server Server = null;
	
	Etc etc = new Etc();
	LotteryCommand lotteryCommand = new LotteryCommand();

	
	// Doing some logging. Thanks cyklo 
	protected final Logger log;
	
	public Lottery() {
		log = Logger.getLogger("Minecraft");
		cost = 5;
		hours = 24;
		timerStarted = false;
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " has been unloaded." );
		//log.info(getDescription().getName() + ": has been disabled.");
	}

	@Override
	public void onEnable() {
		
		// Gets version number and writes out starting line to console.
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled" );
		
		// Start Registration. Thanks TheYeti.
		getDataFolder().mkdirs();
		
		// Does config exist? If not, make it.
		
		etc.makeConfig();
		
		cost = Integer.parseInt(c.getProperty("cost").toString());
		hours = Integer.parseInt(c.getProperty("hours").toString());
		useiConomy = Boolean.parseBoolean(c.getProperty("useiConomy").toString());
		material = Integer.parseInt(c.getProperty("material").toString());
		broadcastBuying = Boolean.parseBoolean(c.getProperty("broadcastBuying").toString());
		welcomeMessage = Boolean.parseBoolean(c.getProperty("welcomeMessage").toString());
		
		
		Server = getServer();
		// Do we need iConomy?
		if(useiConomy == true) {
			// Check if we got iConomy support. If not, no need in starting plugin.
			PluginListener = new PluginListener();
			// Event Registration
			getServer().getPluginManager().registerEvent(Event.Type.PLUGIN_ENABLE, PluginListener, Priority.Monitor, this);
		}
		if(welcomeMessage == true) {
			PlayerListener = new PlayerJoinListener();
			getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, PlayerListener, Priority.Monitor, this);
		}
		
		// Listen for some player interaction perhaps? Thanks to cyklo :)
		
		getCommand("lottery").setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				lotteryCommand.doLotteryCommand(sender, command, label, args);
				return true;
			}
        });

		
		
		// Is the date we are going to draw the lottery set? If not, we should do it.
		if(c.getProperty("nextexec") == null) {
			
			// Set first time to be config hours later? Millisecs, * 1000.
			nextexec = System.currentTimeMillis() + extendTime();
			c.setProperty("nextexec", nextexec);
			
	        if (!getConfiguration().save())
	        {
	            getServer().getLogger().warning("Unable to persist configuration files, changes will not be saved.");
	        }
		} else {
			nextexec = Long.parseLong(c.getProperty("nextexec").toString());
		}
		
		// Start the timer for the first time.
		etc.StartTimerSchedule(false);
		
		// This could, and should, probably be fixed nicer, but for now it'll have to do.
		// Adding timer that waits the time between nextexec and time now.
		
	}


	public long extendTime() {
		Configuration c = getConfiguration();
		hours = Integer.parseInt(c.getProperty("hours").toString());
		Long extendTime = Long.parseLong(hours.toString()) * 60 * 60 * 1000;
		return extendTime;
	}

	class LotteryDraw extends TimerTask {
		public void run() {
			// Cancel timer.
			// Get new config.
			c = getConfiguration();
			nextexec = Long.parseLong(c.getProperty("nextexec").toString());
			
			if(nextexec > 0 && System.currentTimeMillis() > nextexec) {
				// Get the winner, if any. And remove file so we are ready for new round.
				etc.getWinner();
				nextexec = System.currentTimeMillis() + extendTime();
	
				c.setProperty("nextexec",nextexec);
				if (!getConfiguration().save())
		        {
		            getServer().getLogger().warning("Unable to persist configuration files, changes will not be saved.");
		        }
			}
			// Call a new timer.
			etc.StartTimerSchedule(false);
		}
	}

	
	
    public static org.bukkit.Server getBukkitServer() {
        return Server;
    }

    public static iConomy getiConomy() {
        return iConomy;
    }
    
    public static boolean setiConomy(iConomy plugin) {
        if (iConomy == null) {
            iConomy = plugin;
        } else {
            return false;
        }
        return true;
    }
    
  
    
}