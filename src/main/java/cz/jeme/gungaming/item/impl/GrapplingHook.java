package cz.jeme.gungaming.item.impl;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.item.CustomItem;
import cz.jeme.gungaming.loot.Rarity;
import cz.jeme.gungaming.loot.SingleLoot;
import cz.jeme.gungaming.persistence.PersistentData;
import cz.jeme.gungaming.util.Components;
import cz.jeme.gungaming.util.Lores;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class GrapplingHook extends CustomItem implements SingleLoot {
    public static final PersistentData<Byte, Boolean> HOOKED_DATA = PersistentData.ofBoolean(GunGaming.key("hooked"));
    public static final PersistentData<Long, Long> GRAPPLING_TIME_DATA = PersistentData.ofLong(GunGaming.key("grappling_time"));

    private static final int FALL_RESISTANCE = 6000; // fall resistance after using the grappling hook, in millis

    protected GrapplingHook() {
        item.editMeta(Damageable.class, meta -> meta.setMaxDamage(10)); // 9 uses
    }

    @Override
    protected Component provideName() {
        return Component.text("Grappling Hook");
    }

    @Override
    protected String provideDescription() {
        return "I don't think this is used for fishing...";
    }

    @Override
    protected Material provideMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.EPIC;
    }

    @Override
    protected int provideMinAmount() {
        return 1;
    }

    @Override
    protected int provideMaxAmount() {
        return 1;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "grappling_hook";
    }

    @Override
    protected List<String> update(final ItemStack item) {
        final Damageable meta = (Damageable) item.getItemMeta();
        final int damage = meta.getDamage();
        final int maxDamage = meta.getMaxDamage();
        final String uses = maxDamage - damage + "/" + maxDamage;
        return List.of(
                Lores.loreStat("Uses", uses)
        );
    }

    public void onPlayerFish(final PlayerFishEvent event) {
        final EquipmentSlot hand = event.getHand();
        if (hand == null) return;
        final Player player = event.getPlayer();
        final ItemStack fishingRod = player.getInventory().getItem(hand);
        if (!CustomItem.is(fishingRod, GrapplingHook.class)) return;
        switch (event.getState()) {
            case FISHING -> onThrow(event);
            case REEL_IN -> onSubtract(event);
            default -> {
                player.sendActionBar(Components.of("<red>Remember: It's a Grappling Hook!"));
                event.setCancelled(true);
            }
        }
    }

    private void onThrow(final PlayerFishEvent event) {
        final FishHook hook = event.getHook();
        hook.setVelocity(hook.getVelocity().multiply(1.4));
        new BukkitRunnable() {
            private final World world = hook.getWorld();
            private boolean hooked = false;
            private static final float RANGE = 0.3f;
            private static final Vector VERTICAL_DISTURBANCE = new Vector(0f, 0.0302f, 0f);

            @Override
            public void run() {
                if (!hook.isValid()) {
                    cancel();
                    return;
                }
                if (!hooked) {
                    final Location location = hook.getLocation();
                    final List<Block> blocks = List.of(
                            world.getBlockAt(location.clone().add(RANGE, 0, 0)),
                            world.getBlockAt(location.clone().add(0, RANGE, 0)),
                            world.getBlockAt(location.clone().add(0, 0, RANGE)),
                            world.getBlockAt(location.clone().subtract(RANGE, 0, 0)),
                            world.getBlockAt(location.clone().subtract(0, RANGE, 0)),
                            world.getBlockAt(location.clone().subtract(0, 0, RANGE))
                    );
                    if (blocks.stream().map(Block::getType).anyMatch(Material::isCollidable) || hook.isOnGround()) {
                        hooked = true;
                        HOOKED_DATA.write(hook, true);
                    }
                } else {
                    hook.setVelocity(VERTICAL_DISTURBANCE);
                }
            }
        }.runTaskTimer(GunGaming.instance(), 0L, 1L);
    }

    private void onSubtract(final PlayerFishEvent event) {
        assert event.getHand() != null;
        final ItemStack fishingRod = event.getPlayer().getInventory().getItem(event.getHand());
        final FishHook hook = event.getHook();
        if (!HOOKED_DATA.check(hook)) return;
        final boolean hooked = HOOKED_DATA.require(hook);
        if (!hooked) return;
        final Player player = event.getPlayer();
        final Location hookLoc = event.getHook().getLocation();
        final Location playerLoc = player.getLocation();

        if (player.getGameMode() != GameMode.CREATIVE)
            fishingRod.editMeta(Damageable.class, meta -> {
                meta.setDamage(meta.getDamage() + 1);
                if (meta.getDamage() == meta.getMaxDamage()) {
                    fishingRod.setAmount(fishingRod.getAmount() - 1);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                }
            });

        if (fishingRod.getAmount() != 0) updateItem(fishingRod);

        final double xD = hookLoc.getX() - playerLoc.getX();
        final double yD = hookLoc.getY() - playerLoc.getY();
        final double zD = hookLoc.getZ() - playerLoc.getZ();

        player.setVelocity(new Vector(xD, yD, zD).multiply(.5));
        GRAPPLING_TIME_DATA.write(player, System.currentTimeMillis());
    }

    public static void onEntityDamage(final EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (!GRAPPLING_TIME_DATA.check(event.getEntity())) return;
        final long grapplingTime = GRAPPLING_TIME_DATA.require(event.getEntity());
        if (System.currentTimeMillis() - grapplingTime <= FALL_RESISTANCE)
            event.setCancelled(true);
    }
}
