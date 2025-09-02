package cz.jeme.gungaming.item.tracker.impl;

import cz.jeme.gungaming.config.GameConfig;
import cz.jeme.gungaming.game.Game;
import cz.jeme.gungaming.game.GameTeam;
import cz.jeme.gungaming.item.tracker.PlayerTracker;
import cz.jeme.gungaming.loot.Rarity;
import cz.jeme.gungaming.loot.SingleLoot;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TeammateTracker extends PlayerTracker implements SingleLoot {
    @Override
    protected Component provideName() {
        return Component.text("Teammate Tracker");
    }

    @Override
    protected String provideDescription() {
        return "Tracks the nearest teammate";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.UNOBTAINABLE;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "teammate_tracker";
    }

    @Override
    protected boolean validate(final Player player, final Player trackPlayer) {
        return Game.running() &&
               GameConfig.TEAM_PLAYERS.get() > 1 &&
               GameTeam.ofPlayer(player).players().contains(trackPlayer);
    }
}
