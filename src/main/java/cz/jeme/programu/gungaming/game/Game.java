package cz.jeme.programu.gungaming.game;

import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.data.Data;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import cz.jeme.programu.gungaming.util.Components;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;

public final class Game {
    public static final @NotNull Data<Byte, Boolean> FROZEN_DATA = Data.ofBoolean(GunGaming.namespaced("frozen"));
    public static final @NotNull Data<Byte, Boolean> GLIDING_DATA = Data.ofBoolean(GunGaming.namespaced("gliding"));
    public static final @NotNull Data<Byte, Boolean> INVULNERABLE_DATA = Data.ofBoolean(GunGaming.namespaced("invulnerable"));

    public static final @NotNull Sound DING_SOUND = Sound.sound(GunGaming.namespaced("game.ding"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound DONG_SOUND = Sound.sound(GunGaming.namespaced("game.dong"), Sound.Source.MASTER, 1, 1);
    public static final @NotNull Sound END_SOUND = Sound.sound(GunGaming.namespaced("game.end"), Sound.Source.MASTER, 1, 1);

    private static final @NotNull String GRACE_PERIOD_TEAM_NAME = GunGaming.namespaced("grace_period").asString();

    private static @Nullable Game instance = null;

    @SuppressWarnings("UnstableApiUsage")
    private final @NotNull BossBar bossBar = BossBar.bossBar(
            Components.of("<b><#6786C8>Gun</#6786C8><#4C618D>Gaming</#4C618D> <#717B95>"
                          + GunGaming.plugin().getPluginMeta().getVersion()
            ),
            1,
            BossBar.Color.BLUE,
            BossBar.Overlay.PROGRESS
    );

    private final @NotNull Audience audience;
    private final @NotNull Location audienceLocation;
    private final @NotNull World world;
    private final int duration; // minutes
    private final int size;
    private final int centerX;
    private final int centerZ;
    private final int xMin;
    private final int zMin;
    private final int xMax;
    private final int zMax;
    private final @NotNull Location spawn;
    private final @NotNull Team team;
    private final @NotNull Objective kills;
    private final @NotNull Scoreboard scoreboard;

    final @NotNull List<Player> players;

    public Game(final @NotNull Audience audience,
                final @NotNull Location location,
                final int duration,
                final int size,
                final int centerX,
                final int centerZ,
                final int bps) {
        if (running()) throw new IllegalStateException("There can be only one instance of Game!");
        instance = this;
        this.audience = audience;
        this.audienceLocation = location;
        this.world = location.getWorld();
        this.duration = duration;
        this.size = size;
        this.centerX = centerX;
        this.centerZ = centerZ;
        spawn = new Location(world, centerX, 350, centerZ);

        players = new ArrayList<>(Bukkit.getOnlinePlayers());

        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
        world.setTime(1000);
        world.setClearWeatherDuration(duration * 1200);
        world.getEntities().stream()
                .filter(e -> e.getType() != EntityType.PLAYER)
                .forEach(Entity::remove);
        final WorldBorder worldBorder = world.getWorldBorder();
        worldBorder.setCenter(centerX, centerZ);
        worldBorder.setSize(size);

        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        final Team existingTeam = scoreboard.getTeam(GRACE_PERIOD_TEAM_NAME);
        if (existingTeam != null) existingTeam.unregister();
        team = scoreboard.registerNewTeam(GRACE_PERIOD_TEAM_NAME);
        team.setAllowFriendlyFire(false);
        team.setCanSeeFriendlyInvisibles(true);
        scoreboard.getObjectives().forEach(Objective::unregister);
        kills = scoreboard.registerNewObjective(
                GunGaming.namespaced("kills").asString(),
                Criteria.DUMMY,
                Components.of("<#00FFFF><b>" + Components.latinString("Kills"))
        );
        kills.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (final Player player : players) {
            player.closeInventory();
            player.spigot().respawn();
            player.teleport(spawn);
            player.setGameMode(GameMode.SPECTATOR);
            FROZEN_DATA.write(player, true);
            player.clearActivePotionEffects();
            player.getInventory().clear();
            player.setHealth(20);
            final AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            assert maxHealth != null;
            maxHealth.setBaseValue(20);
            player.setAbsorptionAmount(0);
            player.setFoodLevel(20);
            player.setExp(0);
            player.setLevel(0);
            player.activeBossBars().forEach(player::hideBossBar);
            player.showBossBar(bossBar);
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.INVISIBILITY,
                    -1,
                    255,
                    false,
                    false,
                    false
            ));
            kills.getScore(player).setScore(0);
        }

        xMin = centerX - size / 2;
        zMin = centerZ - size / 2;
        xMax = xMin + size;
        zMax = zMin + size;

        CrateGenerator.INSTANCE.generate(
                audience,
                audienceLocation,
                xMin, zMin, xMax, zMax,
                bps
        );

        new BukkitRunnable() {
            private int dotsCount = 1;

            @Override
            public void run() {
                if (!CrateGenerator.INSTANCE.generating()) {
                    for (final Player player : players) {
                        team.addPlayer(player);
                        player.clearTitle();
                    }
                    new StartCountdown(Game.this);
                    cancel();
                    return;
                }
                if (dotsCount >= 3) dotsCount = 1;
                else dotsCount++;

                final Title title = Title.title(
                        Components.of("<blue>Loading..."),
                        Components.of("<gold>" + Components.latinString("Generating loot") + ".".repeat(dotsCount)),
                        Title.Times.times(Duration.ZERO, Duration.ofSeconds(30), Duration.ZERO)
                );
                players.forEach(p -> p.showTitle(title));
            }
        }.runTaskTimer(GunGaming.plugin(), 0L, 20L);
    }

    void startGame() {
        for (final Player player : players) {
            FROZEN_DATA.write(player, false);
            INVULNERABLE_DATA.write(player, true);
            GLIDING_DATA.write(player, true);
            player.setGameMode(GameMode.SURVIVAL);
            player.setGliding(true);
        }
        new GracePeriodCountdown(this);
    }

    void gracePeriodEnd() {
        for (final Player player : players) {
            INVULNERABLE_DATA.write(player, false);
            GLIDING_DATA.write(player, false);
            team.setCanSeeFriendlyInvisibles(false);
            team.setAllowFriendlyFire(true);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

        new GameCountdown(this, duration * 60 + 5);
    }

    private static final @NotNull String GOLD = "<#D4AF37>";
    private static final @NotNull String SILVER = "<#C0C0C0>";
    private static final @NotNull String BRONZE = "<#D58E00>";
    private static final @NotNull String PEWTER = "<#E9EAEC>";

    void endGame() {
        instance = null;
        bossBar.color(BossBar.Color.BLUE);
        bossBar.name(Components.of("<green>Game ended!"));
        bossBar.progress(1);
        final Map<Integer, List<String>> scores = new HashMap<>();
        for (final String entry : scoreboard.getEntries()) {
            final int score = kills.getScore(entry).getScore();
            scores.putIfAbsent(score, new ArrayList<>());
            scores.get(score).add(entry);
        }
        final List<Integer> sorted = new ArrayList<>(scores.keySet());
        sorted.sort(Collections.reverseOrder());
        final List<String> first = scores.getOrDefault(sorted.isEmpty() ? null : sorted.getFirst(), null);
        final List<String> second = scores.getOrDefault(sorted.size() > 1 ? sorted.get(1) : null, null);
        final List<String> third = scores.getOrDefault(sorted.size() > 2 ? sorted.get(2) : null, null);
        for (final Player player : players) {
            player.setGameMode(GameMode.SPECTATOR);
            FROZEN_DATA.write(player, true);
            player.sendMessage(Components.of("<#00FF00>=== Game ended ==="));
            if (first != null) {
                player.sendMessage(Components.of(
                        GOLD + "<b>1" + Components.latinString("st") + ": "
                        + String.join(", ", first)
                ));
                if (second != null) {
                    player.sendMessage(Components.of(
                            SILVER + "<b>2" + Components.latinString("nd") + ": "
                            + String.join(", ", second)
                    ));
                    if (third != null) {
                        player.sendMessage(Components.of(
                                BRONZE + "<b>3" + Components.latinString("rd") + ": "
                                + String.join(", ", third)
                        ));
                    }
                }
            }

            final int rank = sorted.indexOf(kills.getScore(player).getScore()) + 1;
            final String rankStr = switch (rank) {
                case 1 -> GOLD + "1" + Components.latinString("st");
                case 2 -> SILVER + "2" + Components.latinString("nd");
                case 3 -> BRONZE + "3" + Components.latinString("rd");
                default -> PEWTER + rank + Components.latinString("th");
            };
            player.showTitle(Title.title(
                    Components.of("<b>" + rankStr),
                    Components.of("<gold>" + Components.latinString("You finished")),
                    Title.Times.times(Duration.ZERO, Duration.ofHours(1), Duration.ZERO)
            ));
            player.playSound(END_SOUND, player);
        }
    }

    public static @Nullable Game instance() {
        return instance;
    }

    public static boolean running() {
        return instance != null;
    }

    public @NotNull BossBar bossBar() {
        return bossBar;
    }

    public @NotNull World world() {
        return world;
    }

    public int duration() {
        return duration;
    }

    public int size() {
        return size;
    }

    public int centerX() {
        return centerX;
    }

    public int centerZ() {
        return centerZ;
    }

    public @NotNull Location spawn() {
        return spawn;
    }

    public int xMin() {
        return xMin;
    }

    public int zMin() {
        return zMin;
    }

    public int xMax() {
        return xMax;
    }

    public int zMax() {
        return zMax;
    }

    public @NotNull Team team() {
        return team;
    }

    public @NotNull Objective kills() {
        return kills;
    }
}
