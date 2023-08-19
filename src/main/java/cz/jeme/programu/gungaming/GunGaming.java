package cz.jeme.programu.gungaming;

import cz.jeme.programu.gungaming.item.ammo.NineMm;
import cz.jeme.programu.gungaming.item.ammo.Rocket;
import cz.jeme.programu.gungaming.item.ammo.SevenSixTwoMm;
import cz.jeme.programu.gungaming.item.ammo.TwelveGauge;
import cz.jeme.programu.gungaming.item.attachment.magazine.BigMagazine;
import cz.jeme.programu.gungaming.item.attachment.magazine.HugeMagazine;
import cz.jeme.programu.gungaming.item.attachment.magazine.SmallMagazine;
import cz.jeme.programu.gungaming.item.attachment.scope.HighScope;
import cz.jeme.programu.gungaming.item.attachment.scope.LowScope;
import cz.jeme.programu.gungaming.item.attachment.scope.MediumScope;
import cz.jeme.programu.gungaming.item.attachment.stock.MetalStock;
import cz.jeme.programu.gungaming.item.attachment.stock.PlasticStock;
import cz.jeme.programu.gungaming.item.attachment.stock.WoodenStock;
import cz.jeme.programu.gungaming.item.consumable.Bandage;
import cz.jeme.programu.gungaming.item.consumable.Medkit;
import cz.jeme.programu.gungaming.item.consumable.Pills;
import cz.jeme.programu.gungaming.item.consumable.Soda;
import cz.jeme.programu.gungaming.item.gun.*;
import cz.jeme.programu.gungaming.item.misc.Concrete;
import cz.jeme.programu.gungaming.item.misc.GraplingHook;
import cz.jeme.programu.gungaming.item.throwable.MolotovCocktail;
import cz.jeme.programu.gungaming.item.throwable.grenade.FragGrenade;
import cz.jeme.programu.gungaming.item.throwable.grenade.MIRVGrenade;
import cz.jeme.programu.gungaming.item.throwable.grenade.SmallGrenade;
import cz.jeme.programu.gungaming.item.throwable.grenade.SmokeGrenade;
import cz.jeme.programu.gungaming.loot.Loot;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.Messages;
import cz.jeme.programu.gungaming.util.item.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class GunGaming extends JavaPlugin {

    @Override
    public void onEnable() {
        registerItems();
        Loot.registerLoot();
        new GGCommand(); // register the /gg command

        EventListener eventListener = new EventListener(getDataFolder());

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(eventListener, this);

        saveDefaultConfig();
    }

    /**
     * Register all the custom items for GunGaming
     */
    private void registerItems() {
        Ammos.register(new NineMm());
        Ammos.register(new SevenSixTwoMm());
        Ammos.register(new Rocket());
        Ammos.register(new TwelveGauge());

        Guns.register(new M9());
        Guns.register(new NagantM1895());
        Guns.register(new RocketLauncher());
        Guns.register(new AK47());
        Guns.register(new M134Minigun());
        Guns.register(new RemingtonM870());
        Guns.register(new DragunovSVU());
        Guns.register(new SV98());

        Miscs.register(new Concrete());
        Miscs.register(new GraplingHook());

        Attachments.register(new LowScope());
        Attachments.register(new MediumScope());
        Attachments.register(new HighScope());

        Attachments.register(new WoodenStock());
        Attachments.register(new PlasticStock());
        Attachments.register(new MetalStock());

        Attachments.register(new SmallMagazine());
        Attachments.register(new BigMagazine());
        Attachments.register(new HugeMagazine());

        Throwables.register(new FragGrenade());
        Throwables.register(new SmokeGrenade());
        Throwables.register(new MIRVGrenade());
        Throwables.register(new SmallGrenade());
        Throwables.register(new MolotovCocktail());

        Consumables.register(new Bandage());
        Consumables.register(new Medkit());
        Consumables.register(new Soda());
        Consumables.register(new Pills());

        Guns.registered();
        Ammos.registered();
        Miscs.registered();
        Attachments.registered();
        Throwables.registered();
        Consumables.registered();

        Groups.register("gun", Guns.guns);
        Groups.register("ammo", Ammos.ammos);
        Groups.register("misc", Miscs.miscs);
        Groups.register("attachment", Attachments.attachments);
        Groups.register("throwable", Throwables.throwables);
        Groups.register("consumable", Consumables.consumables);

        Groups.registered();
    }

    @Override
    public void onDisable() {
        ZoomManager.getInstance().zoomOutAll();
    }

    /**
     * Log a message with the plugin prefix to the console.
     *
     * @param level Message severitiy identifier
     * @param message The message to log
     */
    public static void serverLog(@NotNull Level level, @NotNull String message) {
        Bukkit.getLogger().log(level, Messages.strip(Messages.PREFIX) + message);
    }

    /**
     * Provides a static and fast access to the plugin object.
     *
     * @return GunGaming plugin object
     */
    public static @NotNull GunGaming getPlugin() {
        return JavaPlugin.getPlugin(GunGaming.class);
    }

    /**
     * Provides a fast way to create namespaced keys assigned to this plugin.
     *
     * @param key The namespaced key string key
     * @return The namespaced key created
     */
    public static @NotNull NamespacedKey generateNamespacedKey(@NotNull String key) {
        return new NamespacedKey(getPlugin(), key);
    }
}