package cz.jeme.programu.gungaming.runnable;

import cz.jeme.programu.gungaming.manager.ReloadManager;
import cz.jeme.programu.gungaming.util.Inventories;
import cz.jeme.programu.gungaming.util.item.Ammos;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Reload extends BukkitRunnable {

    private final Material material;
    public final ItemStack item;
    private final Player player;
    private final int ammoCount;
    private final ReloadManager reloadManager;
    private final ItemStack ammoItem;
    private final boolean isCreative;

    public Reload(ItemStack item, Player player, ReloadManager reloadManager, int ammoCount, ItemStack ammoItem,
                  boolean isCreative) {
        this.item = item;
        this.material = item.getType();
        this.reloadManager = reloadManager;
        this.player = player;
        this.ammoCount = ammoCount;
        this.ammoItem = ammoItem;
        this.isCreative = isCreative;
    }

    @Override
    public void run() {
        if (!isCreative) {
            Inventories.removeItems(player.getInventory(), ammoItem, ammoCount);
        }
        Ammos.add(item, ammoCount);
        reloadManager.removeReload(player, material);
    }
}
