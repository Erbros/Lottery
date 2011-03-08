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



public class Misc {
	
	protected final Logger log = null;
		
	protected Configuration buildConfiguration()
    {
        Configuration c = buildConfiguration();
        c.setProperty("cost", 5);
        c.setProperty("hours", 24);

        if (!buildConfiguration().save())
        {
        	//log.warning(getDescription().getName() + ": Unable to persist configuration files, changes will not be saved.");
        }
		return c;
    }
	
}
