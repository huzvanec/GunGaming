package cz.jeme.programu.gungaming;

import org.bukkit.entity.Arrow;

public interface ArrowVelocityListener {
    void removeArrow(Arrow arrow);

    void addArrow(Arrow arrow);
}
