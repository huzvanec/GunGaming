package cz.jeme.programu.gungaming.eventhandler.interaction;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerInteractHandler {
    private PlayerInteractHandler() {
        throw new AssertionError();
    }
    public static void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_AIR) {
            RightClickHandler.air(event);
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            RightClickHandler.block(event);
        } else if (action == Action.LEFT_CLICK_AIR) {
            LeftClickHandler.air(event);
        } else if (action == Action.LEFT_CLICK_BLOCK) {
            LeftClickHandler.block(event);
        }
    }
}
