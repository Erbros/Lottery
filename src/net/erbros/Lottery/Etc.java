package net.erbros.Lottery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.nijikokun.register.payment.Method.MethodAccount;

public class Etc extends Lottery {
	
	
	public ArrayList<String> playersInFile(String file) {
    	ArrayList<String> players = new ArrayList<String>();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(getDataFolder() + File.separator + file));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	// add players to array.
		    	players.add(str.toString());
		    }
		    in.close();
		} catch (IOException e) {
		}
		return players;
    }

    public int winningAmount() {
    	int amount = 0;
    	ArrayList<String> players = playersInFile("lotteryPlayers.txt");
    	amount = players.size() * cost;
    	return amount;
    }
    
    public boolean getWinner() {
		ArrayList<String> players = playersInFile("lotteryPlayers.txt");
		
		if(players.isEmpty() == true) {
			server.broadcastMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "No tickets sold this round. Thats a shame.");
			return false;
		} else {
			// Find rand. Do minus 1 since its a zero based array.
			int rand = 0;
			if(players.size() == 1) {
				rand = 0;
			} else {
				rand = new Random().nextInt(players.size());
			}
			
			log.info("Rand: " + Integer.toString(rand));
			int amount = winningAmount();
			if(useiConomy == true) {
				MethodAccount account = Method.getAccount(players.get(rand));
				account.add(amount);
				// Announce the winner:
				server.broadcastMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Congratulations to " + players.get(rand) + " for winning " + ChatColor.RED + Method.format(amount) + ".");
				addToWinnerList(players.get(rand), amount, 0);
			} else {
				server.broadcastMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Congratulations to " + players.get(rand) + " for winning " + ChatColor.RED + amount + " " + formatMaterialName(material) + ".");
				server.broadcastMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "Use " + ChatColor.RED + "/lottery claim" + ChatColor.WHITE + " to claim the winnings.");
				addToWinnerList(players.get(rand), amount, material);
				addToClaimList(players.get(rand), amount, material.intValue());
			}
			server.broadcastMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "There was in total " + players.size() + " players with a lottery ticket.");
			
			// Add last winner to config.
			c = getConfiguration();
			c.setProperty("lastwinner", players.get(rand));
			c.setProperty("lastwinneramount", amount);
			
			// Clear file.
			try {
			    BufferedWriter out = new BufferedWriter(new FileWriter(getDataFolder() + File.separator + "lotteryPlayers.txt",false));
			    out.write("");
			    out.close();
			    
			} catch (IOException e) {
			}
		}
		return true;
	}
    

	public String formatMaterialName(int materialId) {
		String returnMaterialName = "";
		String rawMaterialName = Material.getMaterial(materialId).toString();
		rawMaterialName = rawMaterialName.toLowerCase();
		// Large first letter.
		String firstLetterCapital = rawMaterialName.substring(0,1).toUpperCase();
		rawMaterialName = firstLetterCapital + rawMaterialName.substring(1,rawMaterialName.length());
		returnMaterialName = rawMaterialName.replace("_", " ");
		
		return returnMaterialName;
	}
	

	public boolean removeFromClaimList(Player player) {
		// Do the player have something to claim?
		ArrayList<String> otherPlayersClaims = new ArrayList<String>();
		ArrayList<String> claimArray = new ArrayList<String>();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(getDataFolder() + File.separator + "lotteryClaim.txt"));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	String[] split = str.split(":");
		        if(split[0].equals(player.getName())) {
		        	// Adding this to player claim.
		        	claimArray.add(str);
		        } else {
		        	otherPlayersClaims.add(str);
		        }
		    }
		    in.close();
		} catch (IOException e) {
		}
		
		// Did the user have any claims?
		if(claimArray.size() == 0) {
			player.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE + "You did not have anything unclaimed.");
			return false;
		}
		// Do a bit payout.
		for(int i = 0; i < claimArray.size(); i++) {
			String[] split = claimArray.get(i).split(":");
			int claimAmount = Integer.parseInt(split[1]);
			int claimMaterial = Integer.parseInt(split[2]);
			player.getInventory().addItem( new ItemStack(claimMaterial, claimAmount));
			player.sendMessage("You just claimed " + claimAmount + " " + formatMaterialName(claimMaterial) + ".");
		}
		
		
	    // Add the other players claims to the file again.
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(getDataFolder() + File.separator + "lotteryClaim.txt"));
		    for(int i = 0; i < otherPlayersClaims.size(); i++) {
		    	out.write(otherPlayersClaims.get(i));
		    	out.newLine();
		    }
		    
		    out.close();
		    
		    
		} catch (IOException e) {
		}
		return true;
	}

	public boolean addToClaimList(String playerName, int winningAmount, int winningMaterial) {
		// Then first add new winner, and after that the old winners.
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(getDataFolder() + File.separator + "lotteryClaim.txt",true));
		    out.write(playerName + ":" + winningAmount + ":" + winningMaterial);
		    out.newLine();
			out.close();
		} catch (IOException e) {
		}
		return true;
	}

	public boolean addToWinnerList(String playerName, int winningAmount, int winningMaterial) {
		// This list should be 10 players long. 
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
		// Then first add new winner, and after that the old winners.
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(getDataFolder() + File.separator + "lotteryWinners.txt"));
		    out.write(playerName + ":" + winningAmount + ":" + winningMaterial);
		    out.newLine();
		    //How long is the array? We just want the top 9. Removing index 9 since its starting at 0.
		    if(winnerArray.size() > 0) {
			    if(winnerArray.size() > 9) {
					winnerArray.remove(9);
			    }
			    // Go trough list and output lines.
				for (int i = 0; i < winnerArray.size(); i++) {
					out.write(winnerArray.get(i));
					out.newLine();
				}
		    }
			out.close();
		    
		    
		} catch (IOException e) {
		}
		return true;
	}

	public static String timeUntil(long time) {

		double timeLeft = Double.parseDouble(Long.toString(((time - System.currentTimeMillis()) / 1000)));
		// How many days left?
		String stringTimeLeft = "";
		if(timeLeft >= 60 * 60 * 24) {
			int days = (int) Math.floor(timeLeft / (60 * 60 * 24));
			timeLeft -= 60 * 60 * 24 * days;
			if(days == 1) {
				stringTimeLeft += Integer.toString(days) + " day, ";
			} else {
				stringTimeLeft += Integer.toString(days) + " days, ";
			}
		}
		if(timeLeft >= 60 * 60) {
			int hours = (int) Math.floor(timeLeft / (60 * 60));
			timeLeft -= 60 * 60 * hours;
			if(hours == 1) {
				stringTimeLeft += Integer.toString(hours) + " hour, ";
			} else {
				stringTimeLeft += Integer.toString(hours) + " hours, ";
			}
		}
		if(timeLeft >= 60) {
			int minutes = (int) Math.floor(timeLeft / (60));
			timeLeft -= 60 * minutes;
			if(minutes == 1) {
				stringTimeLeft += Integer.toString(minutes) + " minute ";
			} else {
				stringTimeLeft += Integer.toString(minutes) + " minutes ";
			}
		} else {
			// Lets remove the last comma, since it will look bad with 2 days, 3 hours, and 14 seconds.
			if(stringTimeLeft.equalsIgnoreCase("") == false) {
				stringTimeLeft = stringTimeLeft.substring(0, stringTimeLeft.length()-1);
			}
		}
		int secs = (int) timeLeft;
		if(stringTimeLeft.equalsIgnoreCase("") == false) {
			stringTimeLeft += "and ";
		}
		if(secs == 1) {
			stringTimeLeft += secs + " second.";
		} else {
			stringTimeLeft += secs + " seconds.";
		}
		
		return stringTimeLeft;
	}
	
	

	void StartTimerSchedule(boolean drawAtOnce) {
		
		
		long extendtime = 0;
		//Cancel any existing timers.
		if(timerStarted == true) {
			timer.cancel();
			timer.purge();
			extendtime = extendTime();
		} else {
			// Get time until lottery drawing.
			extendtime = nextexec - System.currentTimeMillis();
		}
		// If the time is passed (perhaps the server was offline?), draw lottery at once.
		
		if(extendtime <= 0) {
			extendtime = 3000;
		}
		
		// Is the drawAtOnce boolean set to true? In that case, do drawing in a few secs.
		if(drawAtOnce) {
			extendtime = 1000;
			c = getConfiguration();
			c.setProperty("nextexec", System.currentTimeMillis()+1000);
			nextexec = System.currentTimeMillis()+1000;
			log.info("DRAW NOW");
		}
		
		
		// Start new timer.
		timer = new Timer();
		timer.schedule(new LotteryDraw(), extendtime);
		// Timer is now started, let it know.
		timerStarted = true;
	}
	
	
	public boolean addPlayer(Player player) {

		// Is the player already listed, and thus already have a ticket?
		try {
		    BufferedReader in = new BufferedReader(new FileReader(getDataFolder() + File.separator + "lotteryPlayers.txt"));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	
		        if(str.equalsIgnoreCase(player.getName())) {
		        	// Player have bought earlier. Will send false signal to tell.
		        	in.close();
		        	return false;
		        }
		    }
		    in.close();
		} catch (IOException e) {
		}
		
		// Do the ticket cost money or item?
	    if(useiConomy == false) {
	    	// Do the user have the item?
	    	if(player.getInventory().contains(material, cost)) {
	    		// Remove items.
	    		player.getInventory().removeItem( new ItemStack(material, cost));
	    	} else {
	    		return false;
	    	}
	    } else {
	    	// Do the player have money?
	    	MethodAccount account = Method.getAccount(player.getName());
	    	if(account.hasOver(cost-1)) {
	    		// Removing coins from players account.
	    		account.subtract(cost);
	    	} else {
	    		return false;
	    	}
	    	
	    }
	    // If the user paid, continue. Else we would already have sent 
		try {
		    BufferedWriter out = new BufferedWriter(new FileWriter(getDataFolder() + File.separator + "lotteryPlayers.txt",true));
		    out.write(player.getName());
		    out.newLine();
		    out.close();
		    
		    
		} catch (IOException e) {
		}

		return true;
	}
	
	public void makeConfig() {
		c = getConfiguration();
	
		if(c.getProperty("broadcastBuying") == null || c.getProperty("cost") == null || c.getProperty("hours") == null || c.getProperty("material") == null  || c.getProperty("useiConomy") == null || c.getProperty("welcomeMessage") == null) {
			
			if(c.getProperty("cost") == null) {
				c.setProperty("cost", "5");
			}
			if(c.getProperty("hours") == null) {
				c.setProperty("hours", "24");
			}
			if(c.getProperty("material") == null) {
				c.setProperty("material", "266");
			}
			if(c.getProperty("useiConomy") == null) {
				c.setProperty("useiConomy", "true");
			}
			
			if(c.getProperty("broadcastBuying") == null) {
				c.setProperty("broadcastBuying", "true");
			}
			if(c.getProperty("welcomeMesasge") == null) {
				c.setProperty("welcomeMessage", true);
			}
			
		    if (!getConfiguration().save())
		    {
		        log.warning("Unable to persist configuration files, changes will not be saved.");
		    }
		}
		
	}
	
}