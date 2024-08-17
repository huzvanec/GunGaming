package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public enum GameTeam {
    BLUE(GunGaming.namespaced("blue"), "Blue", NamedTextColor.BLUE),
    RED(GunGaming.namespaced("red"), "Red", NamedTextColor.RED),
    GREEN(GunGaming.namespaced("green"), "Green", NamedTextColor.GREEN),
    YELLOW(GunGaming.namespaced("yellow"), "Yellow", NamedTextColor.YELLOW),
    AQUA(GunGaming.namespaced("aqua"), "Aqua", NamedTextColor.AQUA),
    PINK(GunGaming.namespaced("pink"), "Pink", NamedTextColor.LIGHT_PURPLE),
    GOLD(GunGaming.namespaced("gold"), "Gold", NamedTextColor.GOLD),
    PURPLE(GunGaming.namespaced("purple"), "Purple", NamedTextColor.DARK_PURPLE),
    WHITE(GunGaming.namespaced("white"), "White", NamedTextColor.WHITE),
    GRAY(GunGaming.namespaced("gray"), "Gray", NamedTextColor.GRAY),
    DARK_BLUE(GunGaming.namespaced("dark_blue"), "Dark Blue", NamedTextColor.DARK_BLUE),
    DARK_RED(GunGaming.namespaced("dark_red"), "Dark Red", NamedTextColor.DARK_RED),
    DARK_GREEN(GunGaming.namespaced("dark_green"), "Dark Green", NamedTextColor.DARK_GREEN),
    DARK_AQUA(GunGaming.namespaced("dark_aqua"), "Dark Aqua", NamedTextColor.DARK_AQUA),
    DARK_GRAY(GunGaming.namespaced("dark_gray"), "Dark Gray", NamedTextColor.DARK_GRAY),
    BLACK(GunGaming.namespaced("black"), "Black", NamedTextColor.BLACK);

    private final @NotNull Key key;
    private final @NotNull String displayName;
    private final @NotNull NamedTextColor color;
    private final @NotNull Component colorComponent;
    private final @NotNull Set<UUID> overrides = new HashSet<>();
    private final @NotNull List<Player> players = new ArrayList<>();
    private final @NotNull List<Player> removedPlayers = new ArrayList<>();
    private @Nullable Team team = null;
    private @Nullable Objective objective = null;
    private int score = 0;

    GameTeam(final @NotNull Key key, final @NotNull String displayName, final @NotNull NamedTextColor color) {
        this.key = key;
        this.displayName = displayName;
        this.color = color;
        this.colorComponent = Components.of("<" + color.asHexString() + ">");
    }

    public @NotNull Key key() {
        return key;
    }

    public @NotNull String displayName() {
        return displayName;
    }

    public @NotNull Component color() {
        return colorComponent;
    }

    private static final @NotNull Map<UUID, GameTeam> OVERRIDE_REGISTRY = new HashMap<>();

    @ApiStatus.Internal
    public static @NotNull Map<UUID, GameTeam> overrideRegistry() {
        return OVERRIDE_REGISTRY;
    }

    public boolean hasOverrides() {
        return !overrides.isEmpty();
    }

    public void addOverride(final @NotNull Player player) {
        final UUID uuid = player.getUniqueId();
        final GameTeam current = OVERRIDE_REGISTRY.get(uuid);
        if (current != null) current.removeOverride(player);
        OVERRIDE_REGISTRY.put(uuid, this);
        overrides.add(uuid);
        OVERRIDE_TEAMS.add(this);
    }

    public boolean removeOverride(final @NotNull Player player) {
        final UUID uuid = player.getUniqueId();
        if (overrides.remove(uuid)) {
            OVERRIDE_REGISTRY.remove(uuid);
            if (overrides.isEmpty())
                OVERRIDE_TEAMS.remove(this);
            return true;
        }
        return false;
    }

    public boolean clearOverrides() {
        if (overrides.isEmpty()) return false;
        overrides.forEach(OVERRIDE_REGISTRY::remove);
        overrides.clear();
        OVERRIDE_TEAMS.remove(this);
        return true;
    }

    public boolean overrides(final @NotNull Player player) {
        return overrides.contains(player.getUniqueId());
    }

    private static final @NotNull Set<GameTeam> OVERRIDE_TEAMS = new HashSet<>();

    @ApiStatus.Internal
    public static @NotNull Set<GameTeam> overrideTeams() {
        return OVERRIDE_TEAMS;
    }

    public void register(final @NotNull Objective objective) {
        this.objective = objective;
        team = Objects.requireNonNull(objective.getScoreboard()).registerNewTeam(key.asString());
        team.color(color);
        team.setAllowFriendlyFire(true); // handled in GameEventHandler#onEntityDamageByEntity
        team.setCanSeeFriendlyInvisibles(true);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OWN_TEAM);
        if (!GameConfig.SHOW_PLAYER_NAMETAGS.get())
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        ACTIVE_TEAMS.add(this);
    }

    public boolean registered() {
        return objective != null;
    }

    public void unregister() {
        team().unregister();
        team = null;
        final List<Player> allPlayers = new ArrayList<>(players);
        allPlayers.addAll(removedPlayers);
        allPlayers.forEach(player -> {
            PLAYER_TEAMS.remove(player.getUniqueId());
            objective().getScore(player).resetScore();
        });
        objective = null;
        ACTIVE_TEAMS.remove(this);
        players.clear();
        removedPlayers.clear();
    }

    private @NotNull Team team() {
        return Objects.requireNonNull(team, "Not registered yet!");
    }

    private @NotNull Objective objective() {
        return Objects.requireNonNull(objective, "Not registered yet!");
    }

    public void addPlayer(final @NotNull Player player) {
        team().addPlayer(player);
        objective().getScore(player).setScore(0);
        players.add(player);
        PLAYER_TEAMS.put(player.getUniqueId(), this);
    }

    public boolean removePlayer(final @NotNull Player player) {
        if (players.remove(player)) {
            removedPlayers.add(player);
            System.out.println(players);
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
        return objective().getScore(player).getScore();
    }

    public void addScore(final @NotNull Player player, final int score) {
        if (!players.contains(player))
            throw new IllegalArgumentException("This player is not on this team!");
        objective().getScore(player).setScore(score(player) + score);
        this.score += score;
    }

    public void removeScore(final @NotNull Player player, final int score) {
        addScore(player, -score);
    }

    public @NotNull List<Player> players() {
        return players;
    }

    public int size() {
        return players.size();
    }

    public @NotNull List<Player> removedPlayers() {
        return removedPlayers;
    }

    private static final @NotNull List<GameTeam> VALUES = List.of(values());
    public static final int COUNT = VALUES.size();

    public static @NotNull List<GameTeam> cached() {
        return VALUES;
    }

    private static final @NotNull Map<String, GameTeam> REGISTRY = new HashMap<>();

    static {
        VALUES.forEach(team -> REGISTRY.put(team.key.asString(), team));
    }

    public static @NotNull GameTeam ofKey(final @NotNull String key) {
        return Objects.requireNonNull(REGISTRY.get(key), "Unknown key!");
    }

    public static boolean exists(final @NotNull String key) {
        return REGISTRY.containsKey(key);
    }

    public static @NotNull GameTeam ofOrdinal(final int ordinal) {
        if (ordinal < 0 || ordinal >= COUNT)
            throw new IllegalArgumentException("Invalid ordinal " + ordinal + "!");
        return VALUES.get(ordinal);
    }

    private static final @NotNull Map<UUID, GameTeam> PLAYER_TEAMS = new HashMap<>();

    public static @NotNull GameTeam ofPlayer(final @NotNull Player player) {
        return Objects.requireNonNull(PLAYER_TEAMS.get(player.getUniqueId()), "Unknown player!");
    }

    public static boolean isPlayer(final @NotNull Player player) {
        return PLAYER_TEAMS.containsKey(player.getUniqueId());
    }

    public static void unregisterAll() {
        VALUES.stream()
                .filter(GameTeam::registered)
                .forEach(GameTeam::unregister);
        PLAYER_TEAMS.clear();
        ACTIVE_TEAMS.clear();
    }

    private static final @NotNull List<GameTeam> ACTIVE_TEAMS = new ArrayList<>();

    @ApiStatus.Internal
    public static @NotNull List<GameTeam> activeTeams() {
        return ACTIVE_TEAMS;
    }
}
