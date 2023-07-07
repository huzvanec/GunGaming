package cz.jeme.programu.gungaming.eventhandler.interaction;

import cz.jeme.programu.gungaming.manager.CooldownManager;
import cz.jeme.programu.gungaming.manager.ZoomManager;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractHandler {

    private final LeftClickHandler leftClickHandler;
    private final RightClickHandler rightClickHandler;

    public PlayerInteractHandler(CooldownManager cooldownManager, ZoomManager zoomManager) {
        leftClickHandler = new LeftClickHandler(zoomManager);
        rightClickHandler = new RightClickHandler(cooldownManager);
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_AIR) {
            rightClickHandler.air(event);
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            rightClickHandler.block(event);
        } else if (action == Action.LEFT_CLICK_AIR) {
            leftClickHandler.air(event);
        } else if (action == Action.LEFT_CLICK_BLOCK) {
            leftClickHandler.block(event);
        }
    }
}
