package net.erbros.Lottery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import net.erbros.Lottery.register.payment.Methods;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommandExecutor implements CommandExecutor {
    private Lottery plugin;
    public Etc etc;
    
    public MainCommandExecutor(Lottery plugin) {
        this.plugin = plugin;
    }
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            // Lets check if we have found a plugin for money.
            if (!Methods.hasMethod() && Lottery.useiConomy == true) {
                etc.debugMsg("No money plugin found yet.");
                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                    + ChatColor.WHITE + "Sorry, we haven't found a money plugin yet..");

                return true;
            }


            // Can the player access the plugin?
            if (!sender.hasPermission("lottery.buy")) {

                    return true;
            }



            // If its just /lottery, and no args.
            if (args.length == 0) {


                // Is this a console? If so, just tell that lottery is running and time until next draw.
                if(!(sender instanceof Player)) {
                    sender.sendMessage("Hi Console - The Lottery plugin is running");

                    // Send some messages:
                    sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                    + ChatColor.WHITE + "Draw in: " + ChatColor.RED
                                    + etc.timeUntil(Lottery.nextexec, false));
                    return true;
                }
                Player player = (Player) sender;

                // Check if we got any money/items in the pot.
                double amount = etc.winningAmount();
                // Send some messages:
                player.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                + ChatColor.WHITE + "Draw in: " + ChatColor.RED
                                + etc.timeUntil(Lottery.nextexec, false));
                if (Lottery.useiConomy == false) {
                        player.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                        + ChatColor.WHITE + "Buy a ticket for "
                                        + ChatColor.RED + Lottery.cost + " "
                                        + etc.formatMaterialName(Lottery.material)
                                        + ChatColor.WHITE + " with " + ChatColor.RED
                                        + "/lottery buy");
                        player.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                        + ChatColor.WHITE + "There is currently "
                                        + ChatColor.GREEN + amount + " "
                                        + etc.formatMaterialName(Lottery.material)
                                        + ChatColor.WHITE + " in the pot.");
                } else {
                        player.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                        + ChatColor.WHITE + "Buy a ticket for "
                                        + ChatColor.RED + plugin.Method.format(Lottery.cost)
                                        + ChatColor.WHITE + " with " + ChatColor.RED
                                        + "/lottery buy");
                        player.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                        + ChatColor.WHITE + "There is currently "
                                        + ChatColor.GREEN + plugin.Method.format(amount)
                                        + ChatColor.WHITE + " in the pot.");
                }
                if (plugin.maxTicketsEachUser > 1) {
                        player.sendMessage(ChatColor.GOLD
                                        + "[LOTTERY] "
                                        + ChatColor.WHITE
                                        + "You got "
                                        + ChatColor.RED
                                        + etc.playerInList((Player) sender)
                                        + " "
                                        + ChatColor.WHITE
                                        + Etc.pluralWording("ticket",
                                                    etc.playerInList((Player) sender)));
                }
                // Number of tickets available?
                if(plugin.numberOfTicketsAvailable > 0) {
                    player.sendMessage(ChatColor.GOLD
                            + "[LOTTERY]"
                            + ChatColor.WHITE
                            + " There is "
                            + ChatColor.RED
                            + (plugin.numberOfTicketsAvailable - etc.ticketsSold())
                            + ChatColor.WHITE
                            + " "
                            + Etc.pluralWording("ticket",plugin.numberOfTicketsAvailable - etc.ticketsSold())
                            + " left.");
                }
                player.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                + ChatColor.RED + "/lottery help" + ChatColor.WHITE
                                + " for other commands");
                // Does lastwinner exist and != null? Show.
                // Show different things if we are using iConomy over
                // material.
                if (Lottery.useiConomy == true) {
                        if (plugin.config.getString("config.lastwinner") != null) {
                                player.sendMessage(ChatColor.GOLD
                                                + "[LOTTERY] "
                                                + ChatColor.WHITE
                                                + "Last winner: "
                                                + plugin.config.getString("config.lastwinner")
                                                + " ("
                                                + plugin.Method.format(plugin.config.getInt("config.lastwinneramount"))
                                                + ")");
                        }

                } else {
                        if (plugin.config.getString("config.lastwinner") != null) {
                                player.sendMessage(ChatColor.GOLD
                                                + "[LOTTERY] "
                                                + ChatColor.WHITE
                                                + "Last winner: "
                                                + plugin.config.getString("config.lastwinner")
                                                + " ("
                                                + plugin.config.getInt("config.lastwinneramount")
                                                + " "
                                                + etc.formatMaterialName(Lottery.material) + ")");
                        }
                }

                // if not iConomy, make players check for claims.
                if (Lottery.useiConomy == false) {
                        player.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                        + ChatColor.WHITE
                                        + "Check if you have won with " + ChatColor.RED
                                        + "/lottery claim");
                }

            } else {
                    if (args[0].equalsIgnoreCase("buy")) {


                        // Is this a console? If so, just tell that lottery is running and time until next draw.
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.GOLD
                                            + "[LOTTERY] " + ChatColor.WHITE
                                            + "You're the console, I can't sell you tickets.");
                            return true;
                        }
                        Player player = (Player) sender;


                        // How many tickets do the player want to buy?
                        int buyTickets = 1;
                        // Let's check if the user tries to be funny
                        if (args.length > 1) {
                                try {
                                        @SuppressWarnings("unused")
                                        int x = Integer.parseInt(args[1]);
                                } catch (NumberFormatException nFE) {
                                        player.sendMessage(ChatColor.GOLD
                                                        + "[LOTTERY] " + ChatColor.WHITE
                                                        + "Use a number! /lottery buy <number>");
                                        // Just setting args[1] to 1;
                                        args[1] = "1";
                                }
                        }

                        if (args.length < 2) {
                                buyTickets = 1;
                        } else if (Integer.parseInt(args[1].toString()) + etc.playerInList(player) <= plugin.maxTicketsEachUser) {
                                buyTickets = Integer.parseInt(args[1].toString());
                        } else if (Integer.parseInt(args[1].toString()) + etc.playerInList(player) > plugin.maxTicketsEachUser) {
                                buyTickets = plugin.maxTicketsEachUser - etc.playerInList(player);
                        } else {
                                buyTickets = 1;
                        }

                        if (buyTickets < 1) {
                                buyTickets = 1;
                        }
                        
                        // Have the admin entered a max number of tickets in the lottery?
                        if(plugin.numberOfTicketsAvailable > 0) {
                            // If so, can this user buy the selected amount?
                            if(etc.ticketsSold() + buyTickets > plugin.numberOfTicketsAvailable) {
                                if(etc.ticketsSold() >= plugin.numberOfTicketsAvailable) {
                                    player.sendMessage(ChatColor.GOLD
                                            + "[LOTTERY] " + ChatColor.WHITE
                                            + "There are no more tickets available");
                                    return true;
                                } else {
                                    buyTickets = plugin.numberOfTicketsAvailable - etc.ticketsSold();
                                }
                            }
                        }

                        if (etc.addPlayer(player, plugin.maxTicketsEachUser, buyTickets) == true) {
                            // You got your ticket.
                            if (Lottery.useiConomy == false) {
                                    player.sendMessage(ChatColor.GOLD
                                                    + "[LOTTERY] " + ChatColor.WHITE
                                                    + "You got " + buyTickets + " "
                                                    + Etc.pluralWording("ticket", buyTickets)
                                                    + " for " + ChatColor.RED
                                                    + Lottery.cost * buyTickets + " "
                                                    + etc.formatMaterialName(Lottery.material));
                            } else {
                                    player.sendMessage(ChatColor.GOLD
                                                    + "[LOTTERY] "
                                                    + ChatColor.WHITE
                                                    + "You got "
                                                    + buyTickets
                                                    + " "
                                                    + Etc.pluralWording("ticket", buyTickets)
                                                    + " for "
                                                    + ChatColor.RED
                                                    + plugin.Method.format(Lottery.cost
                                                                    * buyTickets));
                            }
                            // Can a user buy more than one ticket? How many
                            // tickets have he bought now?
                            if (plugin.maxTicketsEachUser > 1) {
                                    player.sendMessage(ChatColor.GOLD
                                                    + "[LOTTERY] "
                                                    + ChatColor.WHITE
                                                    + "You now have "
                                                    + ChatColor.RED
                                                    + etc.playerInList(player)
                                                    + " "
                                                    + ChatColor.WHITE
                                                    + Etc.pluralWording("ticket",
                                                                    etc.playerInList(player)));
                            }
                            if (plugin.broadcastBuying == true) {
                                    Bukkit.broadcastMessage(ChatColor.GOLD
                                                    + "[LOTTERY] " + ChatColor.WHITE
                                                    + player.getDisplayName()
                                                    + " just bought " + buyTickets + " "
                                                    + Etc.pluralWording("ticket", buyTickets));
                            }

                        } else {
                            // Something went wrong.
                            player.sendMessage(ChatColor.GOLD
                                            + "[LOTTERY] "
                                            + ChatColor.WHITE
                                            + "Either you can't afford a ticket, or you got " + plugin.maxTicketsEachUser + " " + Etc.pluralWording("ticket", plugin.maxTicketsEachUser) + " already.");
                        }
                    } else if (args[0].equalsIgnoreCase("claim")) {

                        // Is this a console? If so, just tell that lottery is running and time until next draw.
                        if(!(sender instanceof Player)) {
                            sender.sendMessage(ChatColor.GOLD
                                            + "[LOTTERY] " + ChatColor.WHITE
                                            + "You're the console, you don't have an inventory.");
                            return true;
                        }

                        etc.removeFromClaimList((Player) sender);
                    } else if (args[0].equalsIgnoreCase("draw")) {

                        if (sender.hasPermission("lottery.admin.draw")) {
                                // Start a timer that ends in 3 secs.
                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                + ChatColor.WHITE
                                                + "Lottery will be drawn at once.");
                                plugin.StartTimerSchedule(true);
                        } else {
                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                + ChatColor.WHITE
                                                + "You don't have access to that command.");
                        }

                    } else if (args[0].equalsIgnoreCase("help")) {
                        sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                        + ChatColor.WHITE + "Help commands");
                        sender.sendMessage(ChatColor.RED + "/lottery"
                                        + ChatColor.WHITE + " : Basic lottery info.");
                        sender.sendMessage(ChatColor.RED + "/lottery buy <n>"
                                        + ChatColor.WHITE + " : Buy ticket(s).");
                        sender.sendMessage(ChatColor.RED + "/lottery claim"
                                        + ChatColor.WHITE + " : Claim outstandig wins.");
                        sender.sendMessage(ChatColor.RED + "/lottery winners"
                                        + ChatColor.WHITE + " : Check last winners.");
                        // Are we dealing with admins?
                        if (sender.hasPermission("lottery.admin.draw"))
                                sender.sendMessage(ChatColor.BLUE + "/lottery draw"
                                                + ChatColor.WHITE + " : Draw lottery.");
                        if (sender.hasPermission("lottery.admin.addtopot"))
                                sender.sendMessage(ChatColor.BLUE
                                                + "/lottery addtopot" + ChatColor.WHITE
                                                + " : Add number to pot.");
                        if (sender.hasPermission("lottery.admin.editconfig"))
                                sender.sendMessage(ChatColor.BLUE + "/lottery config"
                                                + ChatColor.WHITE + " : Edit the config.");

                    } else if (args[0].equalsIgnoreCase("winners")) {
                        // Get the winners.
                        ArrayList<String> winnerArray = new ArrayList<String>();
                        try {
                                BufferedReader in = new BufferedReader(
                                                new FileReader(plugin.getDataFolder()
                                                                + File.separator
                                                                + "lotteryWinners.txt"));
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
                                if (split[2].equalsIgnoreCase("0")) {
                                        winListPrice = plugin.Method.format(Double
                                                        .parseDouble(split[1]));
                                } else {
                                        winListPrice = split[1]
                                                        + " "
                                                        + etc.formatMaterialName(
                                                                        Integer.parseInt(split[2]))
                                                                        .toString();
                                }
                                sender.sendMessage((i + 1) + ". " + split[0] + " "
                                                + winListPrice);
                        }
                    } else if (args[0].equalsIgnoreCase("addtopot")) {
                        // Do we trust this person?
                        if (sender.hasPermission("lottery.admin.addtopot")) {
                                if (args[1] == null) {
                                        sender.sendMessage(ChatColor.GOLD
                                                        + "[LOTTERY] " + ChatColor.WHITE
                                                        + "/lottery addtopot <number>");
                                        return true;
                                }
                                int addToPot = 0;
                                // Is it a number?
                                try {
                                        addToPot = Integer.parseInt(args[1]);
                                } catch (NumberFormatException nFE) {
                                        sender.sendMessage(ChatColor.GOLD
                                                        + "[LOTTERY] " + ChatColor.WHITE
                                                        + "Not a number.");
                                        return true;
                                }
                                plugin.extraInPot += addToPot;
                                plugin.config.set("extraInPot", plugin.extraInPot);
                                plugin.saveConfig();

                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                + ChatColor.WHITE + "Added "
                                                + ChatColor.GREEN + addToPot
                                                + ChatColor.WHITE
                                                + " to pot. Extra total is "
                                                + ChatColor.GREEN + plugin.extraInPot);
                        } else {
                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                + ChatColor.WHITE
                                                + "You don't have access to that command.");
                        }
                    } else if (args[0].equalsIgnoreCase("config")) {
                        // Do we trust this person?
                        if (sender.hasPermission("lottery.admin.editconfig")) {
                                // Did the admin provide any additional args or should we show options?
                                if (args.length == 1) {
                                        sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                        + ChatColor.WHITE + "Edit config commands");
                                        sender.sendMessage(ChatColor.RED + "/lottery config cost <i>");
                                        sender.sendMessage(ChatColor.RED + "/lottery config hours <i>");
                                        sender.sendMessage(ChatColor.RED + "/lottery config maxTicketsEachUser <i>");
                                        sender.sendMessage(ChatColor.RED + "/lottery config reload");
                                } else if(args.length >= 2) {
                                        if(args[1].equalsIgnoreCase("cost")) {
                                                if(args.length == 2) {
                                                        sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                                        + ChatColor.WHITE + "Please provide a number");
                                                        return true;
                                                } else {
                                                        int newCoin = 0;
                                                        try {
                                                                newCoin = Integer.parseInt(args[2].toString());
                                                        } catch (NumberFormatException e) {
                                                                //e.printStackTrace();
                                                        }
                                                        if(newCoin <= 0) {
                                                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                                                + ChatColor.WHITE + "Provide a integer (number) greater than zero");
                                                                return true;
                                                        } else {
                                                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                                                + ChatColor.WHITE + "Cost changed to "
                                                                                + ChatColor.RED + newCoin);
                                                                plugin.config.set("config.cost", newCoin);
                                                                // Save the configuration
                                                                plugin.saveConfig();
                                                                // Reload the configuration
                                                                etc.loadConfig();
                                                        }

                                                }
                                        } else if(args[1].equalsIgnoreCase("hours")) {
                                                if(args.length == 2) {
                                                        sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                                        + ChatColor.WHITE + "Please provide a number");
                                                        return true;
                                                } else {
                                                        int newHours = 0;
                                                        try {
                                                                newHours = Integer.parseInt(args[2].toString());
                                                        } catch (NumberFormatException e) {
                                                                //e.printStackTrace();
                                                        }
                                                        if(newHours <= 0) {
                                                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                                                + ChatColor.WHITE + "Provide a integer (number) greater than zero");
                                                                return true;
                                                        } else {
                                                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                                                + ChatColor.WHITE + "Hours changed to "
                                                                                + ChatColor.RED + newHours);
                                                                plugin.config.set("config.hours", newHours);
                                                                // Save the configuration
                                                                plugin.saveConfig();
                                                                // Reload the configuration
                                                                etc.loadConfig();
                                                        }

                                                }
                                        } else if(args[1].equalsIgnoreCase("maxTicketsEachUser") || args[1].equalsIgnoreCase("max")) {
                                                if(args.length == 2) {
                                                        sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                                        + ChatColor.WHITE + "Please provide a number");
                                                        return true;
                                                } else {
                                                        int newMaxTicketsEachUser = 0;
                                                        try {
                                                                newMaxTicketsEachUser = Integer.parseInt(args[2].toString());
                                                        } catch (NumberFormatException e) {
                                                                //e.printStackTrace();
                                                        }
                                                        if(newMaxTicketsEachUser <= 0) {
                                                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                                                + ChatColor.WHITE + "Provide a integer (number) greater to or equal to zero");
                                                                return true;
                                                        } else {
                                                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                                                + ChatColor.WHITE + "Max amount of tickets changed to "
                                                                                + ChatColor.RED + newMaxTicketsEachUser);
                                                                plugin.config.set("config.maxTicketsEachUser", newMaxTicketsEachUser);
                                                                // Save the configuration
                                                                plugin.saveConfig();
                                                                // Reload the configuration
                                                                etc.loadConfig();
                                                        }

                                                }
                                        } else if(args[1].equalsIgnoreCase("config")) {
                                                // Lets just reload the config.
                                                etc.loadConfig();
                                                sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                                                + ChatColor.WHITE + "Config reloaded");
                                        }
                                }
                                // Let's save the configuration, just in case something was changed.
                        }
                } else {

                        sender.sendMessage(ChatColor.GOLD + "[LOTTERY] "
                                        + ChatColor.WHITE
                                        + "Hey, I don't recognize that command!");
                }
            }

            return true;
        }
}
