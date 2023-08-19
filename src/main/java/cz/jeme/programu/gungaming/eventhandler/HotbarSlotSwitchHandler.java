package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.item.gun.Gun;
import cz.jeme.programu.gungaming.manager.ReloadManager;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import cz.jeme.programu.gungaming.util.Sounds;
import cz.jeme.programu.gungaming.util.item.Guns;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HotbarSlotSwitchHandler {

    private final @NotNull ReloadManager reloadManager = ReloadManager.getInstance();
    private final @NotNull ZoomManager zoomManager = ZoomManager.getInstance();

    public void onHotbarSlotSwitch(@NotNull PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        zoomManager.zoomOut(player);
        reloadManager.abortReloads(player);
        ItemStack heldItem = player.getInventory().getItem(event.getNewSlot());
        if (Guns.isGun(heldItem)) {
            Gun gun = Guns.getGun(heldItem);
            player.playSound(Sounds.getGunSwitchSound(gun), player);
        }
    }
}
