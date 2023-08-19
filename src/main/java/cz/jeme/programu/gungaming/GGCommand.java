package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.Crate;
import cz.jeme.programu.gungaming.loot.generation.CrateGenerator;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.item.Groups;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GGCommand extends Command {

    public static final @NotNull Map<String, String> CORRECT_ARGS = Map.of(
            "RELOAD", "reload",
            "HELP", "help",
            "GIVE", "give",
            "GENERATE", "generate"
    );

    public GGCommand() {
        super("gg", "Main command for gungaming", "false", Collections.emptyList());
        setPermission("gungaming.gg");
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
        if (args[0].equals(CORRECT_ARGS.get("GENERATE"))) {
            generate(sender);
            return true;
        }
        sender.sendMessage(Messages.prefix("<red>Unknown command!</red>"));
        return true;
    }

    private void generate(@NotNull CommandSender sender) {
        Player player = (Player) sender;
        Location loc1 = new Location(player.getWorld(), -250, 0, -250);
        Location loc2 = new Location(player.getWorld(), 250, 0, 250);

        CrateGenerator.generateCrates(Map.of(
                Crate.WOODEN_CRATE, 0.1f,
                Crate.GOLDEN_CRATE, 0.02f
        ), loc1, loc2, player);
    }

    private void help(@NotNull CommandSender sender) {
        sender.sendMessage(Messages.prefix("<red>Help TODO!</red>"));
        // TODO Help
    }

    private void give(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 4) {
            sender.sendMessage(Messages.prefix("<red>Not enough arguments!</red>"));
            return;
        }

        if (args.length > 5) {
            sender.sendMessage(Messages.prefix("<red>Too many arguments!</red>"));
            return;
        }

        List<Player> players = new ArrayList<>();
        if (args[1].equals("@everyone")) {
            players = new ArrayList<>(Bukkit.getOnlinePlayers());
        } else {
            String playerName = args[1];
            Player player = Bukkit.getPlayer(playerName);
            if (player == null || !player.isOnline()) {
                // Player not found
                sender.sendMessage(Messages.prefix("<red>This player is not online!</red>"));
                return;
            }
            players.add(player);
        }

        String groupName = args[2].toLowerCase();
        if (!Groups.groups.containsKey(groupName)) {
            sender.sendMessage(Messages.prefix("<red>Unknown group name \"" + groupName + "\"!</red>"));
            return;
        }

        String itemName = args[3].toLowerCase();
        ItemStack item;

        Map<String, ? extends CustomItem> group = Groups.groups.get(groupName);

        if (!matchesLowercaseUnderscores(group.keySet(), itemName)) {
            sender.sendMessage(Messages.prefix("<red>Unknown " + groupName + " name!</red>"));
            return;
        }

        CustomItem customItem = getLowercaseUnderscores(group, itemName);
        assert customItem != null;
        item = customItem.item;

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
        for (int j = 0; j < count; j++) {
            for (Player player : players) {
                Map<Integer, ItemStack> exceeded = player.getInventory().addItem(item);
                if (!exceeded.isEmpty()) {
                    sender.sendMessage(Messages.prefix("<gold>Count exceeded the inventory size!</gold>"));
                    return;
                }
            }
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return containsFilter(new ArrayList<>(CORRECT_ARGS.values()), args[0]);
        }
        if (!args[0].equals(CORRECT_ARGS.get("GIVE"))) {
            return new ArrayList<>();
        }
        if (args.length == 2) {
            ArrayList<String> playerNames = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            playerNames.add("@everyone");
            return containsFilter(playerNames, args[1]);
        }
        if (args.length == 3) {
            return containsFilter(new ArrayList<>(Groups.groups.keySet()), args[2]);
        }
        if (args.length == 4) {
            Map<String, ? extends CustomItem> group = Groups.groups.get(args[2]);
            if (group == null) return new ArrayList<>();
            List<String> itemNames = new ArrayList<>();
            for (CustomItem customItem : group.values()) {
                itemNames.add(customItem.name.replace(' ', '_').toLowerCase());
            }
            return containsFilter(itemNames, args[3]);
        }
        return new ArrayList<>();
    }

    private static @NotNull List<String> containsFilter(@NotNull Collection<String> collection, @NotNull String pattern) {
        return collection.stream()
                .filter(item -> item.contains(pattern))
                .toList();
    }

    private static boolean matchesLowercaseUnderscores(@NotNull Collection<? extends String> collection, @NotNull String match) {
        for (String entry : collection) {
            if (entry.replace(' ', '_').toLowerCase().equals(match)) return true;
        }
        return false;
    }

    private static @Nullable <T> T getLowercaseUnderscores(Map<String, T> map, String match) {
        for (String entry : map.keySet()) {
            if (entry.replace(' ', '_').toLowerCase().equals(match)) return map.get(entry);
        }
        return null;
    }
}
