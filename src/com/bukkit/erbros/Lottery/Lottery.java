package com.bukkit.erbros.Lottery;
//All the imports
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;


public class Lottery extends JavaPlugin{

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		System.out.println("Lottery disabled successfully.");
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		
		// Gets version number and writes out starting line to console.
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println( pdfFile.getName() + "version " + pdfFile.getVersion() + "is enabled" );
		
		// Start Registration. Thanks TheYeti.
		getDataFolder().mkdirs();
		
		// Does the lotteryUsers.txt exist?
		if (!(new File(getDataFolder(), "lotteryUsers.txt").exists())) {
            try {
				new File(getDataFolder(), "lotteryUsers.txt").createNewFile();
			} catch (IOException e) {
				
			}
        }
		
		
		
		
	}

}
