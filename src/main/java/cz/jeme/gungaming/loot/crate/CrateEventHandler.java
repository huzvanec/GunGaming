package cz.jeme.gungaming.loot.crate;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CrateEventHandler {
    private CrateEventHandler() {
        throw new AssertionError();
    }

    public static void onPlayerInteract(final PlayerInteractEvent event) {
        final Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK) return;
        final Player player = event.getPlayer();
        if (player.isSneaking() || player.getGameMode() == GameMode.SPECTATOR) return;
        CrateGenerator.INSTANCE.click(event);
    }
}
