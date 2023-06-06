package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.item.Ammos;
import cz.jeme.programu.gungaming.util.item.Guns;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GG extends Command {

    public static final Map<String, String> CORRECT_ARGS = new HashMap<>();
    public static final Map<String, String> GIVE_GROUPS = new HashMap<>();


    // Fill the maps
    static {
        CORRECT_ARGS.put("RELOAD", "reload");
        CORRECT_ARGS.put("HELP", "help");
        CORRECT_ARGS.put("GIVE", "give");

        GIVE_GROUPS.put("GUNS", "gun");
        GIVE_GROUPS.put("AMMO", "ammo");
    }

    public GG() {
        super("gg");
    }

    protected void register() {
        Bukkit.getCommandMap().register("gungaming", this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length == 0) {
            help(sender);
            return true;
        }
        if (args[0].equals(CORRECT_ARGS.get("HELP"))) {
            help(sender);
            return true;
        }
        if (args[0].equals(CORRECT_ARGS.get("RELOAD"))) {
            sender.sendMessage(Messages.prefix("<red>Reload TODO!</red>"));
            // TODO Reload
            return true;
        }
        if (args[0].equals(CORRECT_ARGS.get("GIVE"))) {
            give(sender, args);
            return true;
        }
        sender.sendMessage(Messages.prefix("<red>Unknown command!</red>"));
        return true;
    }

    private void help(CommandSender sender) {
        sender.sendMessage(Messages.prefix("<red>Help TODO!</red>"));
        // TODO Help
    }

    private void give(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(Messages.prefix("<red>Not enough arguments!</red>"));
            return;
        }

        if (args.length > 5) {
            sender.sendMessage(Messages.prefix("<red>Too many arguments!</red>"));
            return;
        }

        String playerName = args[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline()) {
            // Player not found
            sender.sendMessage(Messages.prefix("<red>This player is not online!</red>"));
            return;
        }

        String group = args[2];
        if (!GIVE_GROUPS.containsValue(group)) {
            sender.sendMessage(Messages.prefix("<red>Unknown give group!</red>"));
            return;
        }

        String itemName = args[3].replace("_", " ");
        ItemStack item;

        if (group.equals(GIVE_GROUPS.get("GUNS"))) {
            if (!Guns.guns.containsKey(itemName)) {
                sender.sendMessage(Messages.prefix("<red>Unknown gun name!</red>"));
                return;
            }

            CustomItem gun = Guns.guns.get(itemName);
            item = gun.item;
        } else if (group.equals(GIVE_GROUPS.get("AMMO"))) {
            if (!Ammos.ammos.containsKey(itemName)) {
                sender.sendMessage(Messages.prefix("<red>Unknown ammo name!</red>"));
                return;
            }
            CustomItem ammo = Ammos.ammos.get(itemName);
            item = ammo.item;
        } else {
            sender.sendMessage(Messages.prefix("<red>Wrong item group!</red>"));
            return;
        }
        int count = 1;
        if (args.length == 5) {
            try {
                if (Integer.parseInt(args[4]) < 1) {
                    sender.sendMessage(Messages.prefix("<red>Count can not be lower than 1!</red>"));
                    return;
                }
            } catch (NullPointerException | NumberFormatException e) {
                sender.sendMessage(Messages.prefix("<red>Count is not valid!</red>"));
                return;
            }
            count = Integer.parseInt(args[4]);
        }
        for (int i = 0; i < count; i++) {
            Map<Integer, ItemStack> add = player.getInventory().addItem(item);
            if (add.size() != 0) {
                sender.sendMessage(Messages.prefix("<gold>Count exceeded your inventory size!</gold>"));
                return;
            }
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return new ArrayList<>(CORRECT_ARGS.values());
        }
        if (!args[0].equals(CORRECT_ARGS.get("GIVE"))) {
            return new ArrayList<>();
        }
        if (args.length == 2) {
            ArrayList<String> playerNames = new ArrayList<String>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        if (args.length == 3) {
            return new ArrayList<>(GIVE_GROUPS.values());
        }
        if (args.length == 4) {
            if (args[2].equals(GIVE_GROUPS.get("GUNS"))) {
                return guns();
            }
            if (args[2].equals(GIVE_GROUPS.get("AMMO"))) {
                return ammo();
            }
        }
        return new ArrayList<>();
    }

    private List<String> guns() {
        ArrayList<String> gunNames = new ArrayList<>();
        for (String gunName : Guns.guns.keySet()) {
            gunNames.add(gunName.replace(" ", "_"));
        }
        return gunNames;
    }

    private List<String> ammo() {
        ArrayList<String> ammoNames = new ArrayList<>();
        for (String ammoName : Ammos.ammos.keySet()) {
            ammoNames.add(ammoName.replace(" ", "_"));
        }
        return ammoNames;
    }
}
