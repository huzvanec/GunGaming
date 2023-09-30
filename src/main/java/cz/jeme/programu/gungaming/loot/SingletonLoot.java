package cz.jeme.programu.gungaming.loot;

import cz.jeme.programu.gungaming.loot.crate.Crate;

/**
 * Interface for tagging items that can appear in a crate only once.
 * This will not affect the entire item group (use {@link Crate#getLimits()} to affect the group).
 */
public interface SingletonLoot {
}