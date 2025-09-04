package cz.jeme.gungaming.game;

import cz.jeme.gungaming.GunGaming;
import cz.jeme.gungaming.config.GameConfig;
import cz.jeme.gungaming.util.Components;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;

@NullMarked
public enum GameTeam {
    BLUE(GunGaming.key("blue"), "Blue", NamedTextColor.BLUE),
    RED(GunGaming.key("red"), "Red", NamedTextColor.RED),
    GREEN(GunGaming.key("green"), "Green", NamedTextColor.GREEN),
    YELLOW(GunGaming.key("yellow"), "Yellow", NamedTextColor.YELLOW),
    AQUA(GunGaming.key("aqua"), "Aqua", NamedTextColor.AQUA),
    PINK(GunGaming.key("pink"), "Pink", NamedTextColor.LIGHT_PURPLE),
    GOLD(GunGaming.key("gold"), "Gold", NamedTextColor.GOLD),
    PURPLE(GunGaming.key("purple"), "Purple", NamedTextColor.DARK_PURPLE),
    WHITE(GunGaming.key("white"), "White", NamedTextColor.WHITE),
    GRAY(GunGaming.key("gray"), "Gray", NamedTextColor.GRAY),
    DARK_BLUE(GunGaming.key("dark_blue"), "Dark Blue", NamedTextColor.DARK_BLUE),
    DARK_RED(GunGaming.key("dark_red"), "Dark Red", NamedTextColor.DARK_RED),
    DARK_GREEN(GunGaming.key("dark_green"), "Dark Green", NamedTextColor.DARK_GREEN),
    DARK_AQUA(GunGaming.key("dark_aqua"), "Dark Aqua", NamedTextColor.DARK_AQUA),
    DARK_GRAY(GunGaming.key("dark_gray"), "Dark Gray", NamedTextColor.DARK_GRAY),
    BLACK(GunGaming.key("black"), "Black", NamedTextColor.BLACK);

    private final Key key;
    private final String displayName;
    private final NamedTextColor color;
    private final Component colorComponent;
    private final Set<UUID> overrides = new HashSet<>();
    private final List<Player> players = new ArrayList<>();
    private final List<Player> removedPlayers = new ArrayList<>();
    private @Nullable Team team = null;
    private @Nullable Objective objective = null;
    private int score = 0;

    GameTeam(final Key key, final String displayName, final NamedTextColor color) {
        this.key = key;
        this.displayName = displayName;
        this.color = color;
        this.colorComponent = Components.of("<" + color.asHexString() + ">");
    }

    public Key key() {
        return key;
    }

    public String displayName() {
        return displayName;
    }

    public Component color() {
        return colorComponent;
    }

    private static final Map<UUID, GameTeam> OVERRIDE_REGISTRY = new HashMap<>();

    @ApiStatus.Internal
    public static Map<UUID, GameTeam> overrideRegistry() {
        return OVERRIDE_REGISTRY;
    }

    public boolean hasOverrides() {
        return !overrides.isEmpty();
    }

    public void addOverride(final Player player) {
        final UUID uuid = player.getUniqueId();
        final GameTeam current = OVERRIDE_REGISTRY.get(uuid);
        if (current != null) current.removeOverride(player);
        OVERRIDE_REGISTRY.put(uuid, this);
        overrides.add(uuid);
        OVERRIDE_TEAMS.add(this);
    }

    public boolean removeOverride(final Player player) {
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

    public boolean overrides(final Player player) {
        return overrides.contains(player.getUniqueId());
    }

    private static final Set<GameTeam> OVERRIDE_TEAMS = new HashSet<>();

    @ApiStatus.Internal
    public static Set<GameTeam> overrideTeams() {
        return OVERRIDE_TEAMS;
    }

    public void register(final Objective objective) {
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

    private Team team() {
        return Objects.requireNonNull(team, "Not registered yet!");
    }

    private Objective objective() {
        return Objects.requireNonNull(objective, "Not registered yet!");
    }

    public void addPlayer(final Player player) {
        team().addPlayer(player);
        objective().getScore(player).setScore(0);
        players.add(player);
        PLAYER_TEAMS.put(player.getUniqueId(), this);
    }

    public boolean removePlayer(final Player player) {
        if (players.remove(player)) {
            removedPlayers.add(player);
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

    public int score(final Player player) {
        if (!players.contains(player))
            throw new IllegalArgumentException("This player is not on this team!");
        return objective().getScore(player).getScore();
    }

    public void addScore(final Player player, final int score) {
        if (!players.contains(player))
            throw new IllegalArgumentException("This player is not on this team!");
        objective().getScore(player).setScore(score(player) + score);
        this.score += score;
    }

    public void removeScore(final Player player, final int score) {
        addScore(player, -score);
    }

    public List<Player> players() {
        return players;
    }

    public int size() {
        return players.size();
    }

    public List<Player> removedPlayers() {
        return removedPlayers;
    }

    private static final List<GameTeam> VALUES = List.of(values());
    public static final int COUNT = VALUES.size();

    public static List<GameTeam> cached() {
        return VALUES;
    }

    private static final Map<String, GameTeam> REGISTRY = new HashMap<>();

    static {
        VALUES.forEach(team -> REGISTRY.put(team.key.asString(), team));
    }

    public static GameTeam ofKey(final String key) {
        return Objects.requireNonNull(REGISTRY.get(key), "Unknown key!");
    }

    public static boolean exists(final String key) {
        return REGISTRY.containsKey(key);
    }

    public static GameTeam ofOrdinal(final int ordinal) {
        if (ordinal < 0 || ordinal >= COUNT)
            throw new IllegalArgumentException("Invalid ordinal " + ordinal + "!");
        return VALUES.get(ordinal);
    }

    private static final Map<UUID, GameTeam> PLAYER_TEAMS = new HashMap<>();

    public static GameTeam ofPlayer(final Player player) {
        return Objects.requireNonNull(PLAYER_TEAMS.get(player.getUniqueId()), "Unknown player!");
    }

    public static boolean isPlayer(final Player player) {
        return PLAYER_TEAMS.containsKey(player.getUniqueId());
    }

    public static void unregisterAll() {
        VALUES.stream()
                .filter(GameTeam::registered)
                .forEach(GameTeam::unregister);
        PLAYER_TEAMS.clear();
        ACTIVE_TEAMS.clear();
    }

    private static final List<GameTeam> ACTIVE_TEAMS = new ArrayList<>();

    @ApiStatus.Internal
    public static List<GameTeam> activeTeams() {
        return ACTIVE_TEAMS;
    }
}
