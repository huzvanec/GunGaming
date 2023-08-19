package cz.jeme.programu.gungaming.eventhandler;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.Namespace;
import cz.jeme.programu.gungaming.item.misc.GraplingHook;
import cz.jeme.programu.gungaming.item.misc.Misc;
import cz.jeme.programu.gungaming.util.item.Miscs;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GraplingHandler {
    public void onThrow(@NotNull PlayerFishEvent event) {
        FishHook hook = event.getHook();
        hook.setVelocity(hook.getVelocity().multiply(1.4));
        new BukkitRunnable() {
            private final World world = hook.getWorld();
            private boolean hooked = false;
            private static final float RANGE = 0.3f;

            @Override
            public void run() {
                if (!hook.isValid()) {
                    cancel();
                    return;
                }
                if (!hooked) {
                    List<Boolean> air = List.of(
                            world.getBlockAt(hook.getLocation().add(0, 0, RANGE)).getType().isAir(),
                            world.getBlockAt(hook.getLocation().add(RANGE, 0, 0)).getType().isAir(),
                            world.getBlockAt(hook.getLocation().subtract(0, 0, RANGE)).getType().isAir(),
                            world.getBlockAt(hook.getLocation().subtract(RANGE, 0, 0)).getType().isAir()
                    );
                    if (air.contains(false) || hook.isOnGround()) {
                        hooked = true;
                        Namespace.HOOKED.set(hook, true);
                    }
                } else {
                    hook.setVelocity(new Vector(0f, 0.03f, 0f));
                }
            }
        }.runTaskTimer(GunGaming.getPlugin(), 0L, 1L);
    }

    public void onSubtract(@NotNull PlayerFishEvent event) {
        ItemStack fishingRod = event.getPlayer().getInventory().getItemInMainHand();
        if (!Miscs.isMisc(fishingRod)) return;
        Misc misc = Miscs.getMisc(fishingRod);
        if (!(misc instanceof GraplingHook)) return;
        FishHook hook = event.getHook();
        if (!Namespace.HOOKED.has(hook)) return;
        Boolean hooked = Namespace.HOOKED.get(hook);
        assert hooked != null : "Hooked is null!";
        if (!hooked) return;
        Player player = event.getPlayer();
        Location hookLoc = event.getHook().getLocation();
        Location playerLoc = player.getLocation();

        if (!(fishingRod.getItemMeta() instanceof Damageable meta))
            throw new IllegalArgumentException("Fishing rod is not damageable!");

        meta.setDamage(meta.getDamage() + 8);
        if (meta.getDamage() == fishingRod.getType().getMaxDurability()) {
            fishingRod.setAmount(fishingRod.getAmount() - 1);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
        }
        fishingRod.setItemMeta(meta);

        double xD = hookLoc.getX() - playerLoc.getX();
        double yD = hookLoc.getY() - playerLoc.getY();
        double zD = hookLoc.getZ() - playerLoc.getZ();

        player.setVelocity(new Vector(xD, yD, zD).multiply(0.5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 120, 999, false, false, false));
    }
}
