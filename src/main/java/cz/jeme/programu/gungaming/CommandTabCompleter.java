package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.utils.AmmoUtils;
import cz.jeme.programu.gungaming.utils.GunUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {

    private final ArrayList<String> emptyArray = new ArrayList<>();

    public CommandTabCompleter() {
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<String>(GunGaming.CORRECT_ARGS.values());
        }
        if (!args[0].equals(GunGaming.CORRECT_ARGS.get("GIVE"))) {
            return emptyArray;
        }
        if (args.length == 2) {
            ArrayList<String> playerNames = new ArrayList<String>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        if (args.length == 3) {
            return new ArrayList<String>(GunGaming.GIVE_GROUPS.values());
        }
        if (args.length == 4) {
            if (args[2].equals(GunGaming.GIVE_GROUPS.get("GUNS"))) {
                return guns();
            }
            if (args[2].equals(GunGaming.GIVE_GROUPS.get("AMMO"))) {
                return ammo();
            }
        }
        return emptyArray;
    }

    private List<String> guns() {
        ArrayList<String> gunNames = new ArrayList<String>();
        for (String gunName : GunUtils.guns.keySet()) {
            gunNames.add(gunName.replace(" ", "_"));
        }
        return gunNames;
    }

    private List<String> ammo() {
        ArrayList<String> ammoNames = new ArrayList<String>();
        for (String ammoName : AmmoUtils.ammos.keySet()) {
            ammoNames.add(ammoName.replace(" ", "_"));
        }
        return ammoNames;
    }
}