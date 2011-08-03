package net.erbros.Lottery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Etc extends Lottery {

	public ArrayList<String> playersInFile(String file) {
		ArrayList<String> players = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					getDataFolder() + File.separator + file));
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

	public String formatMaterialName(int materialId) {
		String returnMaterialName = "";
		String rawMaterialName = Material.getMaterial(materialId).toString();
		rawMaterialName = rawMaterialName.toLowerCase();
		// Large first letter.
		String firstLetterCapital = rawMaterialName.substring(0, 1)
				.toUpperCase();
		rawMaterialName = firstLetterCapital
				+ rawMaterialName.substring(1, rawMaterialName.length());
		returnMaterialName = rawMaterialName.replace("_", " ");

		return returnMaterialName;
	}

	public boolean removeFromClaimList(Player player) {
		// Do the player have something to claim?
		ArrayList<String> otherPlayersClaims = new ArrayList<String>();
		ArrayList<String> claimArray = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					getDataFolder() + File.separator + "lotteryClaim.txt"));
			String str;
			while ((str = in.readLine()) != null) {
				String[] split = str.split(":");
				if (split[0].equals(player.getName())) {
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
		if (claimArray.size() == 0) {
			player.sendMessage(ChatColor.GOLD + "[LOTTERY] " + ChatColor.WHITE
					+ "You did not have anything unclaimed.");
			return false;
		}
		// Do a bit payout.
		for (int i = 0; i < claimArray.size(); i++) {
			String[] split = claimArray.get(i).split(":");
			int claimAmount = Integer.parseInt(split[1]);
			int claimMaterial = Integer.parseInt(split[2]);
			player.getInventory().addItem(
					new ItemStack(claimMaterial, claimAmount));
			player.sendMessage("You just claimed " + claimAmount + " "
					+ formatMaterialName(claimMaterial) + ".");
		}

		// Add the other players claims to the file again.
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					getDataFolder() + File.separator + "lotteryClaim.txt"));
			for (int i = 0; i < otherPlayersClaims.size(); i++) {
				out.write(otherPlayersClaims.get(i));
				out.newLine();
			}

			out.close();

		} catch (IOException e) {
		}
		return true;
	}

	public boolean addToClaimList(String playerName, int winningAmount,
			int winningMaterial) {
		// Then first add new winner, and after that the old winners.
		try {
			BufferedWriter out = new BufferedWriter(
					new FileWriter(getDataFolder() + File.separator
							+ "lotteryClaim.txt", true));
			out.write(playerName + ":" + winningAmount + ":" + winningMaterial);
			out.newLine();
			out.close();
		} catch (IOException e) {
		}
		return true;
	}

	public boolean addToWinnerList(String playerName, int winningAmount,
			int winningMaterial) {
		// This list should be 10 players long.
		ArrayList<String> winnerArray = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					getDataFolder() + File.separator + "lotteryWinners.txt"));
			String str;
			while ((str = in.readLine()) != null) {
				winnerArray.add(str);
			}
			in.close();
		} catch (IOException e) {
		}
		// Then first add new winner, and after that the old winners.
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					getDataFolder() + File.separator + "lotteryWinners.txt"));
			out.write(playerName + ":" + winningAmount + ":" + winningMaterial);
			out.newLine();
			// How long is the array? We just want the top 9. Removing index 9
			// since its starting at 0.
			if (winnerArray.size() > 0) {
				if (winnerArray.size() > 9) {
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

	void StartTimerSchedule(boolean drawAtOnce) {

		long extendtime = 0;
		// Cancel any existing timers.
		if (timerStarted == true) {
			timer.cancel();
			timer.purge();
			extendtime = extendTime();
		} else {
			// Get time until lottery drawing.
			extendtime = nextexec - System.currentTimeMillis();
		}
		// If the time is passed (perhaps the server was offline?), draw lottery
		// at once.

		if (extendtime <= 0) {
			extendtime = 3000;
		}

		// Is the drawAtOnce boolean set to true? In that case, do drawing in a
		// few secs.
		if (drawAtOnce) {
			extendtime = 1000;
			c = getConfiguration();
			c.setProperty("nextexec", System.currentTimeMillis() + 1000);
			nextexec = System.currentTimeMillis() + 1000;
			log.info("DRAW NOW");
		}

		// Start new timer.
		timer = new Timer();
		timer.schedule(new LotteryDraw(), extendtime);
		// Timer is now started, let it know.
		timerStarted = true;
	}

	public void makeConfig() {
		c = getConfiguration();

		if (c.getProperty("broadcastBuying") == null
				|| c.getProperty("cost") == null
				|| c.getProperty("hours") == null
				|| c.getProperty("material") == null
				|| c.getProperty("useiConomy") == null
				|| c.getProperty("welcomeMessage") == null) {

			if (c.getProperty("cost") == null) {
				c.setProperty("cost", "5");
			}
			if (c.getProperty("hours") == null) {
				c.setProperty("hours", "24");
			}
			if (c.getProperty("material") == null) {
				c.setProperty("material", "266");
			}
			if (c.getProperty("useiConomy") == null) {
				c.setProperty("useiConomy", "true");
			}

			if (c.getProperty("broadcastBuying") == null) {
				c.setProperty("broadcastBuying", "true");
			}
			if (c.getProperty("welcomeMesasge") == null) {
				c.setProperty("welcomeMessage", true);
			}

			if (!getConfiguration().save()) {
				log.warning("Unable to persist configuration files, changes will not be saved.");
			}
		}

	}

}