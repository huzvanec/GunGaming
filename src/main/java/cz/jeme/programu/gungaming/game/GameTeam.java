package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public enum GameTeam {
    BLUE("Blue", NamedTextColor.BLUE),
    RED("Red", NamedTextColor.RED),
    GREEN("Green", NamedTextColor.GREEN),
    YELLOW("Yellow", NamedTextColor.YELLOW),
    AQUA("Aqua", NamedTextColor.AQUA),
    PINK("Pink", NamedTextColor.LIGHT_PURPLE),
    GOLD("Gold", NamedTextColor.GOLD),
    PURPLE("Purple", NamedTextColor.DARK_PURPLE),
    WHITE("White", NamedTextColor.WHITE),
    BLACK("Black", NamedTextColor.BLACK),
    DARK_BLUE("Dark Blue", NamedTextColor.DARK_BLUE),
    DARK_RED("Dark Red", NamedTextColor.DARK_RED),
    DARK_GREEN("Dark Green", NamedTextColor.DARK_GREEN),
    DARK_AQUA("Dark Aqua", NamedTextColor.DARK_AQUA),
    GRAY("Gray", NamedTextColor.GRAY),
    DARK_GRAY("Dark Gray", NamedTextColor.DARK_GRAY);

    private final @NotNull String displayName;
    private final @NotNull NamedTextColor color;
    private final @NotNull Component colorComponent;
    private final @NotNull List<Player> players = new ArrayList<>();
    private final @NotNull List<Player> removedPlayers = new ArrayList<>();
    private @Nullable Team team = null;
    private @Nullable Objective kills = null;
    private int score = 0;

    GameTeam(final @NotNull String displayName, final @NotNull NamedTextColor color) {
        this.displayName = displayName;
        this.color = color;
        this.colorComponent = Components.of("<" + color.asHexString() + ">");
    }

    public @NotNull String displayName() {
        return displayName;
    }

    public @NotNull Component color() {
        return colorComponent;
    }

    public void register(final @NotNull Objective kills) {
        this.kills = kills;
        team = Objects.requireNonNull(kills.getScoreboard()).registerNewTeam(displayName);
        team.color(color);
        team.setAllowFriendlyFire(true); // handled in GameEventHandler#onEntityDamageByEntity
        team.setCanSeeFriendlyInvisibles(true);
        ACTIVE_TEAMS.add(this);
    }

    public boolean registered() {
        return kills != null;
    }

    public void unregister() {
        team().unregister();
        team = null;
        kills = null;
        players.forEach(player -> PLAYER_TEAMS.remove(player.getUniqueId()));
        ACTIVE_TEAMS.remove(this);
        players.clear();
        removedPlayers.clear();
    }

    private @NotNull Team team() {
        return Objects.requireNonNull(team, "Not registered yet!");
    }

    private @NotNull Objective kills() {
        return Objects.requireNonNull(kills, "Not registered yet!");
    }

    public void addPlayer(final @NotNull Player player) {
        team().addPlayer(player);
        kills().getScore(player).setScore(0);
        players.add(player);
        PLAYER_TEAMS.put(player.getUniqueId(), this);
    }

    public boolean removePlayer(final @NotNull Player player) {
        if (players.remove(player)) {
            removedPlayers.add(player);
            PLAYER_TEAMS.remove(player.getUniqueId());
            if (players.isEmpty()) {
                unregister();
                return true;
            }
        }
        return false;
    }

    public int score() {
        return score;
    }

    public int score(final @NotNull Player player) {
        if (!players.contains(player))
            throw new IllegalArgumentException("This player is not on this team!");
        return kills().getScore(player).getScore();
    }

    public void addScore(final @NotNull Player player, final int score) {
        if (!players.contains(player))
            throw new IllegalArgumentException("This player is not on this team!");
        kills().getScore(player).setScore(score(player) + score);
        this.score += score;
    }

    public void removeScore(final @NotNull Player player, final int score) {
        addScore(player, -score);
    }

    public @NotNull List<Player> players() {
        return players;
    }

    public @NotNull List<Player> removedPlayers() {
        return removedPlayers;
    }

    private static final GameTeam @NotNull [] VALUES = values();
    public static final int TEAM_COUNT = VALUES.length;

    public static @NotNull GameTeam ofOrdinal(final int ordinal) {
        return VALUES[ordinal];
    }

    private static final @NotNull Map<UUID, GameTeam> PLAYER_TEAMS = new HashMap<>();

    public static @NotNull GameTeam ofPlayer(final @NotNull Player player) {
        return Objects.requireNonNull(PLAYER_TEAMS.get(player.getUniqueId()), "Unknown player!");
    }

    public static void unregisterAll() {
        Arrays.stream(VALUES)
                .filter(GameTeam::registered)
                .forEach(GameTeam::unregister);
        PLAYER_TEAMS.clear();
        ACTIVE_TEAMS.clear();
    }

    private static final @NotNull List<GameTeam> ACTIVE_TEAMS = new ArrayList<>();

    public static @NotNull List<GameTeam> activeTeams() {
        return ACTIVE_TEAMS;
    }
}
