package cz.jeme.gungaming;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import cz.jeme.gungaming.game.GameEventHandler;
import cz.jeme.gungaming.game.lobby.LobbyEventHandler;
import cz.jeme.gungaming.item.ItemEventHandler;
import cz.jeme.gungaming.item.attachment.AttachmentEventHandler;
import cz.jeme.gungaming.item.block.BlockEventHandler;
import cz.jeme.gungaming.item.consumable.ConsumableEventHandler;
import cz.jeme.gungaming.item.gun.GunEventHandler;
import cz.jeme.gungaming.item.impl.GrapplingHook;
import cz.jeme.gungaming.item.melee.MeleeEventHandler;
import cz.jeme.gungaming.item.throwable.ThrowableEventHandler;
import cz.jeme.gungaming.item.tracker.TrackerEventHandler;
import cz.jeme.gungaming.loot.crate.CrateEventHandler;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.command.UnknownCommandEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum EventDistributor implements Listener {
    INSTANCE;

    @EventHandler
    private static void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
        ResourcePackEventHandler.onPlayerInteract(event);
        if (event.useInteractedBlock() == Event.Result.DENY &&
            event.useItemInHand() == Event.Result.DENY) return;
        LobbyEventHandler.onPlayerInteract(event);
        if (event.useInteractedBlock() == Event.Result.DENY &&
            event.useItemInHand() == Event.Result.DENY) return;
        ItemEventHandler.onPlayerInteract(event);
        CrateEventHandler.onPlayerInteract(event);
    }

    @EventHandler
    private static void onPlayerJoin(final PlayerJoinEvent event) {
        GameEventHandler.onPlayerJoin(event);
        LobbyEventHandler.onPlayerJoin(event);
        ResourcePackEventHandler.onPlayerJoin(event); // important: has to go last
    }

    @EventHandler
    private static void onPlayerResourcePackStatus(final PlayerResourcePackStatusEvent event) {
        ResourcePackEventHandler.onPlayerResourcePackStatus(event);
    }

    @EventHandler
    private static void onPlayerMove(final PlayerMoveEvent event) {
        ResourcePackEventHandler.onPlayerMove(event);
        if (event.isCancelled()) return;
        GameEventHandler.onPlayerMove(event);
    }

    @EventHandler
    private static void onPlayerSwapHandItems(final PlayerSwapHandItemsEvent event) {
        GunEventHandler.onPlayerSwapHandItems(event);
    }

    @EventHandler
    private static void onPlayerDropItem(final PlayerDropItemEvent event) {
        TrackerEventHandler.onPlayerDropItem(event);
        if (event.isCancelled()) return;
        GunEventHandler.onPlayerDropItem(event);
        AttachmentEventHandler.onPlayerDropItem(event);
    }

    @EventHandler
    private static void onPlayerItemHeld(final PlayerItemHeldEvent event) {
        ItemEventHandler.onPlayerItemHeld(event);
        GunEventHandler.onPlayerItemHeld(event);
        AttachmentEventHandler.onPlayerItemHeld(event);
    }

    @EventHandler
    private static void onInventoryOpenEvent(final InventoryOpenEvent event) {
        GunEventHandler.onInventoryOpenEvent(event);
    }

    @EventHandler
    private static void onInventoryClick(final InventoryClickEvent event) {
        TrackerEventHandler.onInventoryClick(event);
        if (event.isCancelled()) return;
        AttachmentEventHandler.onInventoryClick(event);
        if (event.isCancelled()) return;
        GunEventHandler.onInventoryClick(event);
    }

    @EventHandler
    private static void onProjectileHit(final ProjectileHitEvent event) {
        GunEventHandler.onProjectileHit(event);
        ThrowableEventHandler.onProjectileHit(event);
    }

    @EventHandler
    private static void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        GunEventHandler.onEntityDamageByEntity(event);
        ThrowableEventHandler.onEntityDamageByEntity(event);
        MeleeEventHandler.onEntityDamageByEntity(event);
        GameEventHandler.onEntityDamageByEntity(event); // important! must go last
    }

    @EventHandler
    private static void onEntityDamage(final EntityDamageEvent event) {
        GlobalEventHandler.onEntityDamage(event); // monitor-ish
        LobbyEventHandler.onEntityDamage(event);
        if (event.isCancelled()) return;
        GameEventHandler.onEntityDamage(event);
        if (event.isCancelled()) return;
        ResourcePackEventHandler.onEntityDamage(event);
        if (event.isCancelled()) return;
        GrapplingHook.onEntityDamage(event);
    }

    @EventHandler
    private static void onPrepareItemCraft(final PrepareItemCraftEvent event) {
        ItemEventHandler.onPrepareItemCraft(event);
        GameEventHandler.onPrepareItemCraft(event);
    }

    @EventHandler
    private static void onPlayerItemConsume(final PlayerItemConsumeEvent event) {
        MeleeEventHandler.onPlayerItemConsume(event);
        if (event.isCancelled()) return;
        ConsumableEventHandler.onPlayerItemConsume(event);
    }

    @EventHandler
    private static void onInventoryClose(final InventoryCloseEvent event) {
        AttachmentEventHandler.onInventoryClose(event);
        GunEventHandler.onInventoryClose(event);
    }

    @EventHandler
    private static void onPlayerQuit(final PlayerQuitEvent event) {
        AttachmentEventHandler.onPlayerQuit(event);
        GameEventHandler.onPlayerQuit(event);
    }

    @EventHandler
    private static void onPlayerGameModeChange(final PlayerGameModeChangeEvent event) {
        AttachmentEventHandler.onPlayerGameModeChange(event);
    }

    @EventHandler
    private static void onPlayerDeath(final PlayerDeathEvent event) {
        AttachmentEventHandler.onPlayerDeath(event);
        GunEventHandler.onPlayerDeath(event);
        GameEventHandler.onPlayerDeath(event);
    }

    @EventHandler
    private static void onPlayerItemDamage(final PlayerItemDamageEvent event) {
        GunEventHandler.onPlayerItemDamage(event);
    }

    @EventHandler
    private static void onEntityShootBow(final EntityShootBowEvent event) {
        GunEventHandler.onEntityShootBow(event);
    }

    private static final GrapplingHook GRAPPLING_HOOK = CustomElement.of(GrapplingHook.class);

    @EventHandler
    private static void onPlayerFish(final PlayerFishEvent event) {
        GRAPPLING_HOOK.onPlayerFish(event);
    }

    @EventHandler
    private static void onEntityToggleGlide(final EntityToggleGlideEvent event) {
        GameEventHandler.onEntityToggleGlide(event);
    }

    @EventHandler
    private static void onFoodLevelChange(final FoodLevelChangeEvent event) {
        GameEventHandler.onFoodLevelChange(event);
        if (event.isCancelled()) return;
        LobbyEventHandler.onFoodLevelChange(event);
    }

    @EventHandler
    private static void onEntityRegainHealth(final EntityRegainHealthEvent event) {
        GameEventHandler.onEntityRegainHealth(event);
    }

    @EventHandler
    private static void onPlayerAdvancementCriterionGrant(final PlayerAdvancementCriterionGrantEvent event) {
        GameEventHandler.onPlayerAdvancementCriterionGrant(event);
        if (event.isCancelled()) return;
        LobbyEventHandler.onPlayerAdvancementCriterionGrant(event);
    }

    @EventHandler
    private static void onItemSpawn(final ItemSpawnEvent event) {
        BlockEventHandler.onItemSpawn(event);
    }

    @EventHandler
    private static void onPlayerRecipeDiscover(final PlayerRecipeDiscoverEvent event) {
        GameEventHandler.onPlayerRecipeDiscover(event);
        if (event.isCancelled()) return;
        LobbyEventHandler.onPlayerRecipeDiscover(event);
    }

    @EventHandler
    private static void onBlockPreDispense(final BlockPreDispenseEvent event) {
        ThrowableEventHandler.onBlockPreDispense(event);
    }

    @EventHandler
    private static void onPlayerPortal(final PlayerPortalEvent event) {
        GameEventHandler.onPlayerPortal(event);
        if (event.isCancelled()) return;
        LobbyEventHandler.onPlayerPortal(event);
    }

    @EventHandler
    private static void onAsyncChat(final AsyncChatEvent event) {
        GameEventHandler.onAsyncChat(event);
    }

    @EventHandler
    private static void onBlockPlace(final BlockPlaceEvent event) {
        BlockEventHandler.onBlockPlace(event);
    }

    @EventHandler
    private static void onUnknownCommand(final UnknownCommandEvent event) {
        GlobalEventHandler.onUnknownCommand(event);
    }
}