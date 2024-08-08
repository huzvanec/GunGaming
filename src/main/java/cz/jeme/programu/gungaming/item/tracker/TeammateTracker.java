package cz.jeme.programu.gungaming.item.tracker;

import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.game.GameTeam;
import cz.jeme.programu.gungaming.loot.Rarity;
import cz.jeme.programu.gungaming.loot.SingleLoot;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeammateTracker extends PlayerTracker implements SingleLoot {
    @Override
    protected @NotNull Component provideName() {
        return Component.text("Teammate Tracker");
    }

    @Override
    protected @NotNull String provideDescription() {
        return "Hold this in your hand to track the nearest teammate";
    }

    @Override
    protected @NotNull Rarity provideRarity() {
        return Rarity.UNOBTAINABLE;
    }

    @Override
    protected @KeyPattern.Value @NotNull String provideKey() {
        return "teammate_tracker";
    }

    @Override
    protected @NotNull Integer provideCustomModelData() {
        return 2;
    }

    @Override
    protected boolean validate(final @NotNull Player player, final @NotNull Player trackPlayer) {
        return Game.running() &&
               GameConfig.TEAM_PLAYERS.get() > 1 &&
               GameTeam.ofPlayer(player).players().contains(trackPlayer);
    }
}
