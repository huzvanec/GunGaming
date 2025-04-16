package cz.jeme.gungaming.game.lobby;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class LobbyEventHandler {
    private LobbyEventHandler() {
        throw new AssertionError();
    }

    public static void onPlayerInteract(final PlayerInteractEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }

    public static void onEntityDamage(final EntityDamageEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }

    public static void onPlayerJoin(final PlayerJoinEvent event) {
        if (!Lobby.enabled()) return;
        final Player player = event.getPlayer();
        Lobby.instance().playerSetup(player);
    }

    public static void onFoodLevelChange(final FoodLevelChangeEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }

    public static void onPlayerRecipeDiscover(final PlayerRecipeDiscoverEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }

    public static void onPlayerAdvancementCriterionGrant(final PlayerAdvancementCriterionGrantEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }

    public static void onPlayerPortal(final PlayerPortalEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }
}
