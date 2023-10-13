package cz.jeme.programu.gungaming.eventhandler.interaction;

import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerInteractHandler {
    private PlayerInteractHandler() {
        throw new AssertionError();
    }

    public static void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR -> RightClickHandler.air(event);
            case RIGHT_CLICK_BLOCK -> RightClickHandler.block(event);
            case LEFT_CLICK_AIR -> LeftClickHandler.air(event);
            case LEFT_CLICK_BLOCK -> LeftClickHandler.block(event);
        }
    }
}