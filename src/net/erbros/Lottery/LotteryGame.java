package net.erbros.Lottery;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import net.erbros.Lottery.register.payment.Method;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LotteryGame {

    private Lottery plugin;
    private LotteryConfig lConfig;
    private FileConfiguration config = null;

    public LotteryGame(final Lottery plugin) {
        this.plugin = plugin;
        lConfig = plugin.getLotteryConfig();
        config = plugin.getConfig();
    }

    public boolean addPlayer(Player player, Integer maxAmountOfTickets, Integer numberOfTickets) {

        if (playerInList(player) + numberOfTickets > maxAmountOfTickets) {
            return false;
        }

        // Do the ticket cost money or item?
        if (Lottery.useiConomy == false) {
            // Do the user have the item
            if (player.getInventory().contains(Lottery.material,
                    (int) Lottery.cost * numberOfTickets)) {
                // Remove items.
                player.getInventory().removeItem(
                        new ItemStack(Lottery.material,
                        (int) Lottery.cost * numberOfTickets));
            } else {
                return false;
            }
        } else {
            // Do the player have money?
            // First checking if the player got an account, if not let's create
            // it.
            plugin.Method.hasAccount(player.getName());
            Method.MethodAccount account = plugin.Method.getAccount(player.getName());

            // And lets withdraw some money
            if (account.hasOver(Lottery.cost * numberOfTickets)) {
                // Removing coins from players account.
                account.subtract(Lottery.cost * numberOfTickets);
            } else {
                return false;
            }

        }
        // If the user paid, continue. Else we would already have sent return
        // false
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(
                    plugin.getDataFolder() + File.separator + "lotteryPlayers.txt",
                    true));
            for (Integer i = 0; i < numberOfTickets; i++) {
                out.write(player.getName());
                out.newLine();
            }
            out.close();

        } catch (IOException e) {
        }

        return true;
    }

    public Integer playerInList(Player player) {
        int numberOfTickets = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    plugin.getDataFolder() + File.separator + "lotteryPlayers.txt"));
            String str;
            while ((str = in.readLine()) != null) {

                if (str.equalsIgnoreCase(player.getName())) {
                    numberOfTickets = numberOfTickets + 1;
                }
            }
            in.close();
        } catch (IOException e) {
        }

        return numberOfTickets;
    }

    public ArrayList<String> playersInFile(String file) {
        ArrayList<String> players = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    plugin.getDataFolder() + File.separator + file));
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

    public double winningAmount() {
        double amount = 0;
        ArrayList<String> players = playersInFile("lotteryPlayers.txt");
        amount = players.size() * Etc.formatAmount(Lottery.cost, Lottery.useiConomy);
        // Set the net payout as configured in the config.
        if (plugin.netPayout > 0) {
            amount = amount * plugin.netPayout / 100;
        }
        // Add extra money added by admins and mods?
        amount += plugin.extraInPot;
        // Any money in jackpot?

        // Do we have a jackpot economy account?
        if (plugin.jackpotAccount != "" && Lottery.useiConomy == true) {
            if (plugin.Method.hasAccount(plugin.jackpotAccount)) {
                Method.MethodAccount jackAccount = plugin.Method.getAccount(plugin.jackpotAccount);
                amount += jackAccount.balance();
            }
        } else {
            amount += config.getDouble("config.jackpot");
        }

        // format it once again.
        amount = Etc.formatAmount(amount, Lottery.useiConomy);

        return amount;
    }

    public int ticketsSold() {
        int sold = 0;
        ArrayList<String> players = playersInFile("lotteryPlayers.txt");
        sold = players.size();
        return sold;
    }

    public boolean removeFromClaimList(Player player) {
        // Do the player have something to claim?
        ArrayList<String> otherPlayersClaims = new ArrayList<String>();
        ArrayList<String> claimArray = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    plugin.getDataFolder() + File.separator + "lotteryClaim.txt"));
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
        if (claimArray.isEmpty()) {
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
                    + Etc.formatMaterialName(claimMaterial) + ".");
        }

        // Add the other players claims to the file again.
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(
                    plugin.getDataFolder() + File.separator + "lotteryClaim.txt"));
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
                    new FileWriter(plugin.getDataFolder() + File.separator
                    + "lotteryClaim.txt", true));
            out.write(playerName + ":" + winningAmount + ":" + winningMaterial);
            out.newLine();
            out.close();
        } catch (IOException e) {
        }
        return true;
    }

    public boolean addToWinnerList(String playerName, Double winningAmount,
            int winningMaterial) {
        // This list should be 10 players long.
        ArrayList<String> winnerArray = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(
                    plugin.getDataFolder() + File.separator + "lotteryWinners.txt"));
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
                    plugin.getDataFolder() + File.separator + "lotteryWinners.txt"));
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

    public String timeUntil(long time, boolean mini) {

        double timeLeft = Double.parseDouble(Long.toString(((time - System.currentTimeMillis()) / 1000)));
        // If negative number, just tell them its DRAW TIME!
        if (timeLeft < 0) {
            // Lets make it draw at once.. ;)
            plugin.startTimerSchedule(true);
            // And return some string to let the user know we are doing our best ;)
            if (mini) {
                return "Soon";
            }
            return "Draw will occur soon!";

        }

        // How many days left?
        String stringTimeLeft = "";

        if (timeLeft >= 60 * 60 * 24) {
            int days = (int) Math.floor(timeLeft / (60 * 60 * 24));
            timeLeft -= 60 * 60 * 24 * days;
            if (!mini) {
                stringTimeLeft += Integer.toString(days) + " " + Etc.pluralWording("day", days) + ", ";
            } else {
                stringTimeLeft += Integer.toString(days) + "d ";
            }
        }
        if (timeLeft >= 60 * 60) {
            int hours = (int) Math.floor(timeLeft / (60 * 60));
            timeLeft -= 60 * 60 * hours;
            if (!mini) {
                stringTimeLeft += Integer.toString(hours) + " " + Etc.pluralWording("hour", hours) + ", ";
            } else {
                stringTimeLeft += Integer.toString(hours) + "h ";
            }
        }
        if (timeLeft >= 60) {
            int minutes = (int) Math.floor(timeLeft / (60));
            timeLeft -= 60 * minutes;
            if (!mini) {
                stringTimeLeft += Integer.toString(minutes) + " " + Etc.pluralWording("minute", minutes) + ", ";
            } else {
                stringTimeLeft += Integer.toString(minutes) + "m ";
            }
        } else {
            // Lets remove the last comma, since it will look bad with 2 days, 3
            // hours, and 14 seconds.
            if (stringTimeLeft.equalsIgnoreCase("") == false && !mini) {
                stringTimeLeft = stringTimeLeft.substring(0,
                        stringTimeLeft.length() - 1);
            }
        }
        int secs = (int) timeLeft;
        if (stringTimeLeft.equalsIgnoreCase("") == false && !mini) {
            stringTimeLeft += "and ";
        }
        if (!mini) {
            stringTimeLeft += Integer.toString(secs) + " " + Etc.pluralWording("second", secs);
        } else {
            stringTimeLeft += secs + "s";
        }

        return stringTimeLeft;
    }

    public boolean getWinner() {
        ArrayList<String> players = playersInFile("lotteryPlayers.txt");

        if (players.isEmpty() == true) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "[LOTTERY] "
                    + ChatColor.WHITE
                    + "No tickets sold this round. Thats a shame.");
            return false;
        } else {
            // Find rand. Do minus 1 since its a zero based array.
            int rand = 0;

            // is max number of tickets 0? If not, include empty tickets not sold.
            if (plugin.TicketsAvailable > 0 && ticketsSold() < plugin.TicketsAvailable) {
                rand = new Random().nextInt(plugin.TicketsAvailable);
                // If it wasn't a player winning, then do some stuff. If it was a player, just continue below.
                if (rand > players.size() - 1) {
                    // No winner this time, pot goes on to jackpot!
                    Double jackpot = winningAmount();


                    // Do we have a jackpot economy account?
                    if (plugin.jackpotAccount != "" && Lottery.useiConomy == true) {
                        plugin.Method.hasAccount(plugin.jackpotAccount);
                        Method.MethodAccount jackAccount = plugin.Method.getAccount(plugin.jackpotAccount);
                        jackAccount.set(jackpot);
                    } else {
                        config.set("config.jackpot", jackpot);
                    }

                    addToWinnerList("Jackpot", jackpot, Lottery.useiConomy ? 0 : Lottery.material);
                    config.set("config.lastwinner", "Jackpot");
                    config.set("config.lastwinneramount", jackpot);
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[LOTTERY] "
                            + ChatColor.WHITE
                            + "No winner, we have a rollover! "
                            + ChatColor.GREEN
                            + ((Lottery.useiConomy) ? plugin.Method.format(jackpot) : +jackpot + " " + Etc.formatMaterialName(Lottery.material))
                            + ChatColor.WHITE
                            + " went to jackpot!");
                    clearAfterGettingWinner();
                    return true;
                }
            } else {
                // Else just continue
                rand = new Random().nextInt(players.size());
            }


            lConfig.debugMsg("Rand: " + Integer.toString(rand));
            double amount = winningAmount();
            if (Lottery.useiConomy == true) {
                plugin.Method.hasAccount(players.get(rand));
                Method.MethodAccount account = plugin.Method.getAccount(players.get(rand));

                // Just make sure the account exists, or make it with default
                // value.
                // Add money to account.
                account.add(amount);
                // Announce the winner:
                Bukkit.broadcastMessage(ChatColor.GOLD + "[LOTTERY] "
                        + ChatColor.WHITE + "Congratulations to "
                        + players.get(rand) + " for winning " + ChatColor.RED
                        + plugin.Method.format(amount) + ".");
                addToWinnerList(players.get(rand), amount, 0);
            } else {
                // let's throw it to an int.
                int matAmount = (int) Etc.formatAmount(amount, Lottery.useiConomy);
                amount = (double) matAmount;
                Bukkit.broadcastMessage(ChatColor.GOLD + "[LOTTERY] "
                        + ChatColor.WHITE + "Congratulations to "
                        + players.get(rand) + " for winning " + ChatColor.RED
                        + matAmount + " " + Etc.formatMaterialName(Lottery.material) + ".");
                Bukkit.broadcastMessage(ChatColor.GOLD + "[LOTTERY] "
                        + ChatColor.WHITE + "Use " + ChatColor.RED
                        + "/lottery claim" + ChatColor.WHITE
                        + " to claim the winnings.");
                addToWinnerList(players.get(rand), amount, Lottery.material);

                addToClaimList(players.get(rand), matAmount, Lottery.material);
            }
            Bukkit.broadcastMessage(ChatColor.GOLD
                    + "[LOTTERY] "
                    + ChatColor.WHITE
                    + "There was in total "
                    + Etc.realPlayersFromList(players).size()
                    + " "
                    + Etc.pluralWording("player", Etc.realPlayersFromList(players).size()) + " buying " + players.size() + " "
                    + Etc.pluralWording("ticket", players.size()));

            // Add last winner to config.
            config.set("config.lastwinner", players.get(rand));
            config.set("config.lastwinneramount", amount);


            // Do we have a jackpot economy account?
            if (plugin.jackpotAccount != "" && Lottery.useiConomy == true) {
                plugin.Method.hasAccount(plugin.jackpotAccount);
                Method.MethodAccount jackAccount = plugin.Method.getAccount(plugin.jackpotAccount);
                jackAccount.set(0);
            } else {
                config.set("config.jackpot", 0);
            }

            clearAfterGettingWinner();
        }
        return true;
    }

    public void clearAfterGettingWinner() {

        // extra money in pot added by admins and mods?
        // Should this be removed?
        if (plugin.clearExtraInPot == true) {
            config.set("extraInPot", 0);
            plugin.extraInPot = (double) 0;
        }
        // Clear file.
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(plugin.getDataFolder() + File.separator
                    + "lotteryPlayers.txt", false));
            out.write("");
            out.close();

        } catch (IOException e) {
        }
    }

    public String formatCustomMessageLive(String msg, Player player) {
        //Lets give timeLeft back if user provie %draw%
        msg = msg.replaceAll("%draw%", timeUntil(Lottery.nextexec, true));
        //Lets give timeLeft with full words back if user provie %drawLong%
        msg = msg.replaceAll("%drawLong%", timeUntil(Lottery.nextexec, false));
        // If %player% = Player name
        msg = msg.replaceAll("%player%", player.getDisplayName());
        // %cost% = cost
        if (Lottery.useiConomy) {
            msg = msg.replaceAll("%cost%", String.valueOf(Etc.formatAmount(Lottery.cost, Lottery.useiConomy)));
        } else {
            msg = msg.replaceAll("%cost%", String.valueOf((int) Etc.formatAmount(Lottery.cost, Lottery.useiConomy)));
        }

        // %pot%
        msg = msg.replaceAll("%pot%", Double.toString(winningAmount()));
        // Lets get some colors on this, shall we?
        msg = msg.replaceAll("(&([a-f0-9]))", "\u00A7$2");
        return msg;
    }
}
