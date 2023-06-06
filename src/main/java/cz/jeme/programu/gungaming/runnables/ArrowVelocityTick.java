package cz.jeme.programu.gungaming.runnables;

import cz.jeme.programu.gungaming.ArrowVelocityListener;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class ArrowVelocityTick extends BukkitRunnable implements ArrowVelocityListener {

    public Map<Arrow, Vector> arrows = new HashMap<>();

    @Override
    public void run() {
        for (Arrow arrow : arrows.keySet()) {
            arrow.setVelocity(arrows.get(arrow));
            if (arrow.getTicksLived() >= 600) { // After 30 seconds
                removeArrow(arrow);
                arrow.remove();
            }
        }
    }

    @Override
    public void removeArrow(Arrow arrow) {
        arrows.remove(arrow);
    }

    @Override
    public void addArrow(Arrow arrow) {
        arrows.put(arrow, arrow.getVelocity());
    }

}
