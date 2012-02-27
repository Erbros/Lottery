package net.erbros.Lottery;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import org.bukkit.Material;

public class Etc {

    public static double formatAmount(double amount, boolean usingiConomy) {
        // Okay, if this is a material it's really simple. Just floor it.
        DecimalFormat formatter = null;
        if (!usingiConomy) {
            formatter = new DecimalFormat("0");
        } else {
            formatter = new DecimalFormat("0.00");
        }
        amount = Double.parseDouble(formatter.format(amount));


        return amount;
    }

    public static String formatMaterialName(int materialId) {
        String returnMaterialName = "";
        String rawMaterialName = Material.getMaterial(materialId).toString();
        rawMaterialName = rawMaterialName.toLowerCase();
        // Large first letter.
        String firstLetterCapital = rawMaterialName.substring(0, 1).toUpperCase();
        rawMaterialName = firstLetterCapital
                + rawMaterialName.substring(1, rawMaterialName.length());
        returnMaterialName = rawMaterialName.replace("_", " ");

        return returnMaterialName;
    }

    public static String pluralWording(String word, Integer number) {
        // Start
        if (word.equalsIgnoreCase("ticket")) {
            if (number == 1) {
                return "ticket";
            } else {
                return "tickets";
            }
        }
        // Next
        if (word.equalsIgnoreCase("player")) {
            if (number == 1) {
                return "player";
            } else {
                return "players";
            }
        }
        // Next
        if (word.equalsIgnoreCase("day")) {
            if (number == 1) {
                return "day";
            } else {
                return "days";
            }
        }
        // Next
        if (word.equalsIgnoreCase("hour")) {
            if (number == 1) {
                return "hour";
            } else {
                return "hours";
            }
        }
        // Next
        if (word.equalsIgnoreCase("minute")) {
            if (number == 1) {
                return "minute";
            } else {
                return "minutes";
            }
        }
        // Next
        if (word.equalsIgnoreCase("second")) {
            if (number == 1) {
                return "second";
            } else {
                return "seconds";
            }
        }
        // Next
        return "i don't know that word";
    }

    public static Hashtable<String, Integer> realPlayersFromList(ArrayList<String> ticketList) {
        Hashtable<String, Integer> playerList = new Hashtable<String, Integer>();
        int value = 0;
        for (String check : ticketList) {
            if (playerList.containsKey(check)) {
                value = Integer.parseInt(playerList.get(check).toString()) + 1;
            } else {
                value = 1;
            }
            playerList.put(check, value);
        }
        return playerList;
    }

    public static int parseInt(final String arg) {
        int newInt = 0;
        try {
            newInt = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
        }
        return newInt > 0 ? newInt : 0;
    }

    public static int parseDouble(final String arg) {
        int newInt = 0;
        try {
            newInt = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
        }
        return newInt > 0 ? newInt : 0;
    }
}
