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
public class EnemyTracker extends PlayerTracker implements SingleLoot {
    @Override
    protected Component provideName() {
        return Component.text("Enemy Tracker");
    }

    @Override
    protected String provideDescription() {
        return "Tracks the nearest enemy";
    }

    @Override
    protected Rarity provideRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    protected @KeyPattern.Value String provideKey() {
        return "enemy_tracker";
    }

    @Override
    protected boolean validate(final Player player, final Player trackPlayer) {
        final GameTeam team = GameTeam.ofPlayer(player);
        return !Game.running() ||
               team.players().size() == 1 ||
               !team.players().contains(trackPlayer);
    }
}
