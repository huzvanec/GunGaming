package cz.jeme.programu.gungaming;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import cz.jeme.programu.gungaming.items.ammo.Ammo;
import cz.jeme.programu.gungaming.items.ammo.NineMM;
import cz.jeme.programu.gungaming.items.ammo.Rocket;
import cz.jeme.programu.gungaming.items.ammo.SevenPointSixTwoMM;
import cz.jeme.programu.gungaming.items.guns.Gun;
import cz.jeme.programu.gungaming.items.guns.M9;
import cz.jeme.programu.gungaming.items.guns.OT38;
import cz.jeme.programu.gungaming.items.guns.RocketLauncher;
import cz.jeme.programu.gungaming.managers.CooldownManager;
import cz.jeme.programu.gungaming.managers.ReloadManager;
import cz.jeme.programu.gungaming.managers.ZoomManager;
import cz.jeme.programu.gungaming.runnables.ArrowVelocityTick;
import cz.jeme.programu.gungaming.utils.AmmoUtils;
import cz.jeme.programu.gungaming.utils.GunUtils;
import net.md_5.bungee.api.ChatColor;

public class GunGaming extends JavaPlugin {

	// Map of all valid arguments
	public static final Map<String, String> CORRECT_ARGS = new HashMap<String, String>();

	public static final Map<String, String> GIVE_GROUPS = new HashMap<String, String>();

	// Preifx
	public static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + ChatColor.BOLD + "ɢɢ"
			+ ChatColor.DARK_GRAY + "]: ";

	private CooldownManager cooldownManager = new CooldownManager();

	private ArrowVelocityTick arrowVelocityTick = new ArrowVelocityTick();

	private ZoomManager zoomManager = new ZoomManager();

	private ReloadManager reloadManager = new ReloadManager(cooldownManager);

	// Fill the maps
	{
		CORRECT_ARGS.put("RELOAD", "reload");
		CORRECT_ARGS.put("HELP", "help");
		CORRECT_ARGS.put("GIVE", "give");

		GIVE_GROUPS.put("GUNS", "gun");
		GIVE_GROUPS.put("AMMO", "ammo");
	}

	@Override
	public void onEnable() {
		registerItems();
		GunUtils.setUnmodifiableGuns();

		arrowVelocityTick.runTaskTimer(this, 0, 1);

		getCommand("gg").setTabCompleter(new CommandTabCompleter());

		EventListener eventListener = new EventListener(cooldownManager, zoomManager, reloadManager, arrowVelocityTick);

		PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		pluginManager.registerEvents(eventListener, this);
	}

	private void registerItems() {
		AmmoUtils.register(new NineMM());
		AmmoUtils.register(new SevenPointSixTwoMM());
		AmmoUtils.register(new Rocket());
		
		GunUtils.register(new M9());
		GunUtils.register(new OT38());
		GunUtils.register(new RocketLauncher());
	}

	@Override
	public void onDisable() {
		zoomManager.zoomOutAll();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("gg")) { // When command called
			if (args.length == 0) {
				help(sender);
				return true;
			}
			if (args[0].equals(CORRECT_ARGS.get("HELP"))) {
				help(sender);
				return true;
			}
			if (args[0].equals(CORRECT_ARGS.get("RELOAD"))) {
				sender.sendMessage(PREFIX + ChatColor.GOLD + "Reload TODO!");
				// TODO Reload
				return true;
			}
			if (args[0].equals(CORRECT_ARGS.get("GIVE"))) {
				give(sender, args);
				return true;
			}

		}
		sender.sendMessage(PREFIX + ChatColor.RED + "Command not recognized.");
		return true;
	}

	private void help(CommandSender sender) {
		sender.sendMessage(PREFIX + ChatColor.GOLD + "Help!");
		// TODO Help
	}

	private void give(CommandSender sender, String[] args) {
		if (args.length < 4) {
			sender.sendMessage(PREFIX + ChatColor.RED + "Not enough arguments!");
			return;
		}

		if (args.length > 5) {
			sender.sendMessage(PREFIX + ChatColor.RED + "Too many arguments!");
			return;
		}

		String playerName = args[1];
		Player player = Bukkit.getPlayer(playerName);
		if (player == null || !player.isOnline()) {
			// Player not found
			sender.sendMessage(PREFIX + ChatColor.RED + "This player is not online!");
			return;
		}

		String group = args[2];
		if (!GIVE_GROUPS.containsValue(group)) {
			sender.sendMessage(PREFIX + ChatColor.RED + "Unknown give group!");
			return;
		}

		String itemName = args[3].replace("_", " ");
		ItemStack item = null;

		if (group.equals(GIVE_GROUPS.get("GUNS"))) {
			if (!GunUtils.guns.containsKey(itemName)) {
				sender.sendMessage(PREFIX + ChatColor.RED + "Unknown gun name!");
				return;
			}

			Gun gun = GunUtils.guns.get(itemName);
			item = gun.item;
		} else if (group.equals(GIVE_GROUPS.get("AMMO"))) {
			if (!AmmoUtils.ammos.containsKey(itemName)) {
				sender.sendMessage(PREFIX + ChatColor.RED + "Unknown ammo name!");
				return;
			}
			Ammo ammo = AmmoUtils.ammos.get(itemName);
			item = ammo.item;
		} else {
			sender.sendMessage(PREFIX + ChatColor.RED + "Wrong item group!");
			return;
		}
		int count = 1;
		if (args.length == 5) {
			try {
				if (Integer.valueOf(args[4]) < 1) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Count can not be lower than 1!");
					return;
				}
			} catch (NullPointerException | NumberFormatException e) {
				sender.sendMessage(PREFIX + ChatColor.RED + "Count is not valid!");
				return;
			}
			count = Integer.valueOf(args[4]);
		}
		for (int i = 0; i < count; i++) {
			Map<Integer, ItemStack> add = player.getInventory().addItem(item);
			if (add.size() != 0) {
				sender.sendMessage(PREFIX + ChatColor.RED + "Count exceeded your inventory size!");
				return;
			}
		}
	}

	public static void serverLog(Level lvl, String msg) {
		if (msg == null) {
			msg = "null";
		}
		Bukkit.getServer().getLogger().log(lvl, ChatColor.stripColor(PREFIX) + msg);
	}
}