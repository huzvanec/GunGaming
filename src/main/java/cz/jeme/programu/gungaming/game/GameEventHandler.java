package cz.jeme.programu.gungaming.game;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.game.runnable.countdown.Respawn;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.item.tracker.TeammateTracker;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.*;


public final class GameEventHandler {
    private GameEventHandler() {
        throw new AssertionError();
    }

    public static void onPlayerMove(final @NotNull PlayerMoveEvent event) {
        if (!Game.running()) return;
        if (!event.hasChangedPosition()) return;
        final Location to = event.getTo();
        if (to.y() < to.getWorld().getMinHeight()) {
            event.setCancelled(true);
            return;
        }
        if (!Game.FROZEN_DATA.read(event.getPlayer()).orElse(false)) return;
        event.setCancelled(true);
    }

    public static void onEntityDamage(final @NotNull EntityDamageEvent event) {
        if (!Game.INVULNERABLE_DATA.read(event.getEntity()).orElse(false)) return;
        event.setCancelled(true);
    }

    public static void onEntityToggleGlide(final @NotNull EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof final Player player)) return;
        if (!Game.GLIDING_DATA.read(player).orElse(false)) return;
        event.setCancelled(true);
        final Material groundMaterial = player.getWorld().getBlockAt(player.getLocation().subtract(0, .4, 0)).getType();
        if (!groundMaterial.isEmpty()) {
            Game.GLIDING_DATA.write(player, false);
            Bukkit.getScheduler().runTaskLater(
                    GunGaming.plugin(),
                    () -> Game.INVULNERABLE_DATA.write(player, false),
                    20 * 3
            );
        }
    }

    public static void onPrepareItemCraft(final @NotNull PrepareItemCraftEvent event) {
        if (!Game.running()) return;
        event.getInventory().setResult(ItemStack.empty());
    }

    private static final @NotNull Random RANDOM = new Random();

    public static void onPlayerDeath(final @NotNull PlayerDeathEvent event) {
        if (!Game.running()) return;
        event.setCancelled(true);
        final Player player = event.getPlayer();
        final Location location = player.getLocation();
        final World world = player.getWorld();
        final PlayerInventory inventory = player.getInventory();
        final double chance = GameConfig.DEATH_DROP_PERCENTAGE.get() / 100D;
        for (final ItemStack item : inventory) {
            if (item == null) continue;
            if (CustomItem.is(item, TeammateTracker.class)) continue;
            final int amount = item.getAmount();
            int dropAmount = amount;
            for (int i = 0; i < amount; i++) {
                if (RANDOM.nextDouble() > chance) {
                    dropAmount--;
                    if (dropAmount == 0) break;
                }
            }
            item.setAmount(dropAmount);
            world.dropItemNaturally(location, item);
            item.setAmount(amount - dropAmount);
        }
        final Component deathMessage = event.deathMessage();
        new Respawn(Objects.requireNonNull(Game.instance()), player);
        if (deathMessage != null)
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(
                    Components.of("<#FF0000>☠ ").append(deathMessage))
            );
        final Player killer = player.getKiller();
        if (killer != null && !killer.getUniqueId().equals(player.getUniqueId())) {
            GameTeam.ofPlayer(killer).addScore(killer, 1);
        }
    }

    public static void onFoodLevelChange(final @NotNull FoodLevelChangeEvent event) {
        if (!Game.running()) return;
        event.setCancelled(true);
    }

    private static final @NotNull Map<UUID, Integer> HEALING = new HashMap<>();
    private static final int HEALING_TICKS = 4; // how many ticks of real healing are required for the player to heal in game

    public static void onEntityRegainHealth(final @NotNull EntityRegainHealthEvent event) {
        if (event.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED) return;
        if (!Game.running()) return;
        if (!(event.getEntity() instanceof final Player player)) return;
        final UUID uuid = player.getUniqueId();
        if (!HEALING.containsKey(uuid)) {
            HEALING.put(uuid, 1);
            return;
        }
        if (HEALING.get(uuid) == HEALING_TICKS) {
            HEALING.put(uuid, 0);
            return;
        }
        event.setCancelled(true);
        HEALING.put(uuid, HEALING.get(uuid) + 1);
    }

    public static void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        if (!Game.running()) return;
        final Player player = event.getPlayer();
        final Game game = Game.instance();
        player.setGameMode(GameMode.SPECTATOR);
        final double x = player.getX();
        final double z = player.getZ();
        if (x >= game.xMax() || x <= game.xMin() || z >= game.zMax() || z <= game.zMin())
            player.teleport(game.spawn());
        Game.GLIDING_DATA.write(player, false);
        Game.FROZEN_DATA.write(player, false);
        Game.INVULNERABLE_DATA.write(player, true);
        game.removePlayer(player);
        player.showTitle(Title.title(
                Components.of("<red>" + Components.latinString("You are a spectator")),
                Component.empty(),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(5), Duration.ofSeconds(2))
        ));
        event.joinMessage(Components.of("<#00FFFF>\uD83D\uDC41 ").append(
                player.teamDisplayName().append(
                        Components.of("<#00FFFF> started spectating")
                )
        ));
    }

    public static void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        if (!Game.running()) return;
        final Player player = event.getPlayer();
        final boolean gamePlayer = Game.instance().removePlayer(player);
        final Location location = player.getLocation();
        final World world = player.getWorld();
        final PlayerInventory inventory = player.getInventory();
        if (gamePlayer) {
            for (final ItemStack item : inventory) {
                if (item == null) continue;
                if (CustomItem.is(item, TeammateTracker.class)) continue;
                world.dropItemNaturally(location, item);
            }
            inventory.clear();
            event.quitMessage(Components.of("<#FF0000>❌ ").append(
                            player.teamDisplayName().append(
                                    Components.of("<#FF0000> left the game and got eliminated")
                            )
                    )
            );
        } else {
            event.quitMessage(Components.of("<#00FFFF>\uD83D\uDC41 ").append(
                    player.teamDisplayName().append(
                            Components.of("<#00FFFF> quit spectating")
                    )
            ));
        }
    }

    public static void onPlayerAdvancementCriterionGrant(final @NotNull PlayerAdvancementCriterionGrantEvent event) {
        if (!Game.running()) return;
        event.setCancelled(true);
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void onEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent event) {
        if (!Game.running()) return;
        if (!(event.getEntity() instanceof final Player hurt)) return;
        if (!(event.getDamageSource().getCausingEntity() instanceof final Player damager)) return;
        if (hurt.getUniqueId().equals(damager.getUniqueId())) return;
        if (GameTeam.ofPlayer(damager).players().contains(hurt)) { // it's his teammate
            event.setCancelled(true);
            return;
        }
        if (Game.instance().gracePeriod()) {
            event.setCancelled(true);
            damager.sendActionBar(Components.of("<red>You can't damage other players during grace period!"));
        }
    }

    public static void onPlayerRecipeDiscover(final @NotNull PlayerRecipeDiscoverEvent event) {
        if (!Game.running()) return;
        event.setCancelled(true);
    }

    public static void onPlayerPortal(final @NotNull PlayerPortalEvent event) {
        if (!Game.running()) return;
        event.setCancelled(true);
    }
}
