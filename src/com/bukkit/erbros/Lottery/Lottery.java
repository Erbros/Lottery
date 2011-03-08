package com.bukkit.erbros.Lottery;
//All the imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.config.Configuration;


public class Lottery extends JavaPlugin{

	// Doing some logging. Thanks cyklo 
	protected final Logger log;
	protected Integer cost;
	protected Integer hours;
	
	public Lottery() {
		log = Logger.getLogger("Minecraft");
		cost = 5;
		hours = 24;
		
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
		
		Configuration c = getConfiguration();
		
		if(c.getProperty("cost") == null || c.getProperty("hours") == null) {
			
			c.setProperty("cost", 5);
			c.setProperty("hours", 24);
			
	        if (!getConfiguration().save())
	        {
	            getServer().getLogger().warning("Unable to persist configuration files, changes will not be saved.");
	        }
		} 
		
		String convert = c.getProperty("cost").toString();
		cost = Integer.parseInt(convert);
		convert = c.getProperty("hours").toString();
		hours = Integer.parseInt(convert);
				
	}
	
	protected void buildConfiguration()
    {
        Configuration c = getConfiguration();
        c.setProperty("cost", 5);
        c.setProperty("hours", 24);

        if (!getConfiguration().save())
        {
            getServer().getLogger().warning("Unable to persist configuration files, changes will not be saved.");
        }
    }
	
}