package net.erbros.Lottery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LotteryCommand extends Lottery {
	
	public boolean doLotteryCommand(CommandSender sender, Command command, String label, String[] args) {
		c = getConfiguration();
		// If its just /lottery, and no args.
		if(args.length == 0) {
			// Check if we got any money/items in the pot.
			int amount = etc.winningAmount();
			// Send some messages:
			sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Draw in: " + ChatColor.RED + Etc.timeUntil(nextexec));
			if(useiConomy == false) {
				sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Buy a ticket for " + ChatColor.RED +  cost + " " + etc.formatMaterialName(material) + ChatColor.WHITE + " with " + ChatColor.RED + "/lottery buy");
				sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "There is currently " + ChatColor.GREEN +  amount + " " + etc.formatMaterialName(material) + ChatColor.WHITE + " in the pot.");
			} else {
				sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Buy a ticket for " + ChatColor.RED + iConomy.getBank().format(cost) + ChatColor.WHITE + " with " + ChatColor.RED + "/lottery buy");
				sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "There is currently " + ChatColor.GREEN +  iConomy.getBank().format(amount) + ChatColor.WHITE + " in the pot.");
			}
			sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.RED + "/lottery help" + ChatColor.WHITE + " for other commands");
			// Does lastwinner exist and != null? Show.
			// Show different things if we are using iConomy over material.
			if(useiConomy == true) {
				if(c.getProperty("lastwinner") != null) {
					sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Last winner: " + c.getProperty("lastwinner") + " (" + iConomy.getBank().format(Integer.parseInt(c.getProperty("lastwinneramount").toString())) + ")");
				} 
				
			} else {
				if(c.getProperty("lastwinner") != null) {
					sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Last winner: " + c.getProperty("lastwinner") + " (" + c.getProperty("lastwinneramount").toString() + " " + etc.formatMaterialName(material) + ")");
				} 
			}
			
			// if not iConomy, make players check for claims.
			if(useiConomy == false) {
				sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Check if you have won with " + ChatColor.RED + "/lottery claim");
			} 
			
		} else {
			if(args[0].equalsIgnoreCase("buy")) {
				Player player = (Player) sender;
				
				if(etc.addPlayer(player) == true) {
					// You got your ticket. 
					if(useiConomy == false) {
						sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "You got your lottery ticket for " + ChatColor.RED +  cost + " " + etc.formatMaterialName(material));
					} else {
						sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "You got your lottery ticket for " + ChatColor.RED + iConomy.getBank().format(cost));
					}
					if(broadcastBuying == true) {
						Server.broadcastMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + player.getDisplayName() + " just bought a ticket.");
					}
					
				} else {
					// You can't buy more than one ticket.
					sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Either you can't afford a ticket, or you got one already.");
				}
			} else if(args[0].equalsIgnoreCase("claim")) {
				etc.removeFromClaimList((Player) sender);
			} else if(args[0].equalsIgnoreCase("draw")) {
				// Later add permissions. As of now, is the player op?
				if(sender.isOp()) {
					// Start a timer that ends in 3 secs.
					sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Lottery will be drawn at once.");
					etc.StartTimerSchedule(true);
				}
				
			} else if(args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Help commands");
				sender.sendMessage(ChatColor.RED + "/lottery" + ChatColor.WHITE + " : Basic lottery info.");
				sender.sendMessage(ChatColor.RED + "/lottery buy" + ChatColor.WHITE + " : Buy a ticket.");
				sender.sendMessage(ChatColor.RED + "/lottery claim" + ChatColor.WHITE + " : Claim outstandig wins.");
				sender.sendMessage(ChatColor.RED + "/lottery winners" + ChatColor.WHITE + " : Check last winners.");
			} else if(args[0].equalsIgnoreCase("winners")) {
				// Get the winners.
				ArrayList<String> winnerArray = new ArrayList<String>();
				try {
				    BufferedReader in = new BufferedReader(new FileReader(getDataFolder() + File.separator + "lotteryWinners.txt"));
				    String str;
				    while ((str = in.readLine()) != null) {
				    	winnerArray.add(str);
				    }
				    in.close();
				} catch (IOException e) {
				}
				String[] split;
				String winListPrice;
				for (int i = 0; i < winnerArray.size(); i++) {
					split = winnerArray.get(i).split(":");
					if(split[2].equalsIgnoreCase("0")) {
						winListPrice = iConomy.getBank().format(Double.parseDouble(split[1]));
					} else {
						winListPrice = split[1] + " " + etc.formatMaterialName(Integer.parseInt(split[2])).toString();
					}
					sender.sendMessage((i + 1) + ". " + split[0] + " " + winListPrice);
				}
			} else {
				sender.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Hey, I don't recognize that command!");
			}
		}
		
		return true;
	}
}