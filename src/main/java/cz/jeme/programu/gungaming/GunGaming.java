package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.item.ammo.NineMM;
import cz.jeme.programu.gungaming.item.ammo.Rocket;
import cz.jeme.programu.gungaming.item.ammo.SevenPointSixTwoMM;
import cz.jeme.programu.gungaming.item.attachment.magazine.BigMagazine;
import cz.jeme.programu.gungaming.item.attachment.magazine.HugeMagazine;
import cz.jeme.programu.gungaming.item.attachment.magazine.SmallMagazine;
import cz.jeme.programu.gungaming.item.attachment.scope.HighScope;
import cz.jeme.programu.gungaming.item.attachment.scope.LowScope;
import cz.jeme.programu.gungaming.item.attachment.scope.MediumScope;
import cz.jeme.programu.gungaming.item.attachment.stock.MetalStock;
import cz.jeme.programu.gungaming.item.attachment.stock.PlasticStock;
import cz.jeme.programu.gungaming.item.attachment.stock.WoodenStock;
import cz.jeme.programu.gungaming.item.gun.AK47;
import cz.jeme.programu.gungaming.item.gun.M9;
import cz.jeme.programu.gungaming.item.gun.OT38;
import cz.jeme.programu.gungaming.item.gun.RocketLauncher;
import cz.jeme.programu.gungaming.item.misc.Concrete;
import cz.jeme.programu.gungaming.loot.Loot;
import cz.jeme.programu.gungaming.manager.CooldownManager;
import cz.jeme.programu.gungaming.manager.ReloadManager;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.item.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class GunGaming extends JavaPlugin {

    private final CooldownManager cooldownManager = new CooldownManager();

    private final ZoomManager zoomManager = new ZoomManager();

    private final ReloadManager reloadManager = new ReloadManager(cooldownManager);

    @Override
    public void onEnable() {
        registerItems();
        Loot.registerLoot();

        new GGCommand(); // register the /gg command

        EventListener eventListener = new EventListener(cooldownManager, zoomManager, reloadManager, getDataFolder());

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(eventListener, this);

        saveDefaultConfig();
    }

    /**
     * Register all the custom items for GunGaming
     */
    private void registerItems() {
        Ammos.register(new NineMM());
        Ammos.register(new SevenPointSixTwoMM());
        Ammos.register(new Rocket());

        Guns.register(new M9());
        Guns.register(new OT38());
        Guns.register(new RocketLauncher());
        Guns.register(new AK47());

        Miscs.register(new Concrete());

        Attachments.register(new LowScope());
        Attachments.register(new MediumScope());
        Attachments.register(new HighScope());

        Attachments.register(new WoodenStock());
        Attachments.register(new PlasticStock());
        Attachments.register(new MetalStock());

        Attachments.register(new SmallMagazine());
        Attachments.register(new BigMagazine());
        Attachments.register(new HugeMagazine());

        Guns.registered();
        Ammos.registered();
        Miscs.registered();
        Attachments.registered();

        Groups.register("gun", Guns.guns);
        Groups.register("ammo", Ammos.ammos);
        Groups.register("misc", Miscs.miscs);
        Groups.register("attachment", Attachments.attachments);

        Groups.setUnmodifiable();
    }

    @Override
    public void onDisable() {
        zoomManager.zoomOutAll();
    }

    /**
     * Log a message with the plugin prefix to the console.
     *
     * @param lvl Message severitiy identifier
     * @param msg The message to log
     */
    public static void serverLog(Level lvl, String msg) {
        if (msg == null) throw new NullPointerException("Message is null!");
        Bukkit.getLogger().log(lvl, Messages.strip(Messages.PREFIX) + msg);
    }

    /**
     * Provides a static and fast access to the plugin object.
     *
     * @return GunGaming plugin object
     */
    public static GunGaming getPlugin() {
        return JavaPlugin.getPlugin(GunGaming.class);
    }

    /**
     * Provides a fast way to create namespaced keys assigned to this plugin.
     *
     * @param key The namespaced key string key
     * @return The namespaced key created
     */
    public static NamespacedKey namespacedKey(String key) {
        return new NamespacedKey(getPlugin(), key);
    }
}