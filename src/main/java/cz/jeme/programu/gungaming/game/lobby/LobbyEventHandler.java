package cz.jeme.programu.gungaming.game.lobby;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.jetbrains.annotations.NotNull;

public final class LobbyEventHandler {
    private LobbyEventHandler() {
        throw new AssertionError();
    }

    public static void onPlayerInteract(final @NotNull PlayerInteractEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }

    public static void onEntityDamage(final @NotNull EntityDamageEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }

    public static void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        if (!Lobby.enabled()) return;
        Lobby.instance().playerSetup(event.getPlayer());
    }

    public static void onFoodLevelChange(final @NotNull FoodLevelChangeEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }

    public static void onPlayerRecipeDiscover(final @NotNull PlayerRecipeDiscoverEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }

    public static void onPlayerAdvancementCriterionGrant(final @NotNull PlayerAdvancementCriterionGrantEvent event) {
        if (!Lobby.enabled()) return;
        event.setCancelled(true);
    }
}
