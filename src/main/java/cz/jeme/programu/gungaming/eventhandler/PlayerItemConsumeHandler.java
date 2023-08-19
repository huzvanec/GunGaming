package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.item.consumable.Consumable;
import cz.jeme.programu.gungaming.util.Sounds;
import cz.jeme.programu.gungaming.util.item.Consumables;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerItemConsumeHandler {
    private final @NotNull Map<UUID, BukkitRunnable> soundBoard = new HashMap<>();

    public void onFinishConsume(@NotNull PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getHand());

        if (!Consumables.isConsumable(item)) return;
        soundBoard.get(player.getUniqueId()).cancel();

        Consumable consumable = Consumables.getConsumable(item);
        event.setCancelled(true);
        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
        consumable.consume(event);

        Bukkit.getScheduler().runTaskLater(
                GunGaming.getPlugin(),
                () -> player.playSound(Sounds.getConsumableBurpSound(consumable), player),
                1L
        );
    }

    public void onStartConsume(@NotNull PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        assert item != null : "Item is null!";
        Consumable consumable = Consumables.getConsumable(item);
        Player player = event.getPlayer();
        BukkitRunnable eatRunnable = new BukkitRunnable() {
            private int counter = 0;
            @Override
            public void run() {
                if (counter == 7) {
                    cancel();
                    return;
                }
                player.playSound(Sounds.getConsumableEatSound(consumable), player);
                counter++;
            }
        };
        soundBoard.put(player.getUniqueId(), eatRunnable);
        eatRunnable.runTaskTimer(GunGaming.getPlugin(), 9L, 4L);
    }

    public void onStopConsume(@NotNull PlayerStopUsingItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!Consumables.isConsumable(item)) return;
        soundBoard.get(player.getUniqueId()).cancel();
    }
}
