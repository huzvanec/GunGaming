package cz.jeme.programu.gungaming.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.item.CustomItem;
import cz.jeme.programu.gungaming.loot.crate.CrateGenerator;
import cz.jeme.programu.gungaming.loot.crate.impl.AirDrop;
import cz.jeme.programu.gungaming.util.Components;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;


@SuppressWarnings("UnstableApiUsage")
public final class GGCommand {
    private static final int SUCCESS = Command.SINGLE_SUCCESS;
    private static final int FAILURE = 0;

    private GGCommand() {
        throw new AssertionError();
    }

    public static void register(final @NotNull JavaPlugin plugin, final @NotNull Commands commands) {
        commands.register(
                plugin.getPluginMeta(),
                build(),
                "Main GunGaming command",
                List.of()
        );
    }

    private static @NotNull LiteralCommandNode<CommandSourceStack> build() {
        return literal("gg")
                .requires(source -> source.getSender().hasPermission("gungaming.gg"))
                .executes(GGCommand::version)
                .then(literal("version")
                        .executes(GGCommand::version)
                )
//                .then(literal("reload")
//                        .executes(GGCommand::reload)
//                )
                .then(literal("give")
                        .then(argument("targets", ArgumentTypes.players())
                                .then(argument("tag", new CustomItemTagCommandArgument())
                                        .then(argument("item", new CustomItemCommandArgument())
                                                .executes(ctx -> give(ctx, 1))
                                                .then(argument("count", IntegerArgumentType.integer(1))
                                                        .executes(ctx -> give(ctx, IntegerArgumentType.getInteger(ctx, "count")))
                                                )
                                        )
                                )
                        )
                )
                .then(literal("crates")
                        .then(literal("generation")
                                .then(literal("start")
                                        .then(argument("width", IntegerArgumentType.integer(1, 10_000))
                                                .then(argument("length", IntegerArgumentType.integer(1, 10_000))
                                                        .executes(ctx -> {
                                                            final Location location = ctx.getSource().getLocation();
                                                            return generate(ctx, location.getBlockX(), location.getBlockZ(), 1000);
                                                        })
                                                        .then(argument("center_x", IntegerArgumentType.integer(-30_000_000, 30_000_000))
                                                                .then(argument("center_z", IntegerArgumentType.integer(-30_000_000, 30_000_000))
                                                                        .executes(ctx -> {
                                                                            final int x = IntegerArgumentType.getInteger(ctx, "center_x");
                                                                            final int z = IntegerArgumentType.getInteger(ctx, "center_z");
                                                                            return generate(ctx, x, z, 1000);
                                                                        })
                                                                        .then(argument("bps", IntegerArgumentType.integer(100, 5000))
                                                                                .executes(ctx -> {
                                                                                    final int x = IntegerArgumentType.getInteger(ctx, "center_x");
                                                                                    final int z = IntegerArgumentType.getInteger(ctx, "center_z");
                                                                                    final int bps = IntegerArgumentType.getInteger(ctx, "bps");
                                                                                    return generate(ctx, x, z, bps);
                                                                                })
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                                .then(literal("cancel")
                                        .executes(GGCommand::cancelGeneration)
                                )
                        )
                        .then(literal("remove")
                                .executes(GGCommand::removeCrates)
                        )
                        .then(literal("contents")
                                .then(literal("clear")
                                        .executes(GGCommand::clearCrates)
                                )
                                .then(literal("refill")
                                        .executes(GGCommand::refillCrates)
                                )
                        )
                        .then(literal("airdrop")
                                .then(literal("generate")
                                        .then(argument("pos", ArgumentTypes.blockPosition())
                                                .executes(GGCommand::generateAirDrop)
                                        )
                                )
                        )
                )
                .then(literal("game")
                        .then(literal("start")
                                .then(argument("size", IntegerArgumentType.integer(50, 10_000))
                                        .executes(ctx -> {
                                            final Location location = ctx.getSource().getLocation();
                                            return startGame(ctx, location.getBlockX(), location.getBlockZ(), 1000, 20);
                                        })
                                        .then(argument("center_x", IntegerArgumentType.integer(-30_000_000, 30_000_000))
                                                .then(argument("center_z", IntegerArgumentType.integer(-30_000_000, 30_000_000))
                                                        .executes(ctx -> {
                                                            final int x = IntegerArgumentType.getInteger(ctx, "center_x");
                                                            final int z = IntegerArgumentType.getInteger(ctx, "center_z");
                                                            return startGame(ctx, x, z, 1000, 20);
                                                        })
                                                        .then(argument("bps", IntegerArgumentType.integer(100, 5000))
                                                                .executes(ctx -> {
                                                                    final int x = IntegerArgumentType.getInteger(ctx, "center_x");
                                                                    final int z = IntegerArgumentType.getInteger(ctx, "center_z");
                                                                    final int bps = IntegerArgumentType.getInteger(ctx, "bps");
                                                                    return startGame(ctx, x, z, bps, 20);
                                                                })
                                                                .then(argument("duration_minutes", IntegerArgumentType.integer(1, 1000))
                                                                        .executes(ctx -> {
                                                                            final int x = IntegerArgumentType.getInteger(ctx, "center_x");
                                                                            final int z = IntegerArgumentType.getInteger(ctx, "center_z");
                                                                            final int bps = IntegerArgumentType.getInteger(ctx, "bps");
                                                                            final int duration = IntegerArgumentType.getInteger(ctx, "duration_minutes");
                                                                            return startGame(ctx, x, z, bps, duration);
                                                                        })
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(literal("stop")
                                .executes(GGCommand::stopGame)
                        )
                        .then(literal("end")
                                .executes(GGCommand::endGame)
                        )
                )
                .build();
    }

    private static int version(final @NotNull CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().getSender().sendMessage(Components.prefix("<green>Running <#6786C8>Gun</#6786C8><#4C618D>Gaming</#4C618D> v" + GunGaming.plugin().getPluginMeta().getVersion()));
        return SUCCESS;
    }

    private static int reload(final @NotNull CommandContext<CommandSourceStack> ctx) {
        return SUCCESS;
    }

    private static int give(final @NotNull CommandContext<CommandSourceStack> ctx, final int count) throws CommandSyntaxException {
        final List<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource());
        final CustomItem customItem = ctx.getArgument("item", CustomItem.class);
        final ItemStack item = customItem.item();
        final int maxCount = item.getMaxStackSize() * 100;
        if (count > maxCount) {
            ctx.getSource().getSender().sendMessage(
                    Component.text("Can't give more than %d of ".formatted(maxCount))
                            .color(NamedTextColor.RED)
                            .append(Component.text()
                                    .append(Component.text("["))
                                    .append(customItem.name())
                                    .append(Component.text("]"))
                                    .color(NamedTextColor.WHITE)
                                    .build()
                                    .hoverEvent(item))
            );
            return FAILURE;
        }
        targets.forEach(player -> give(player, item, count));
        return SUCCESS;
    }


    public static void give(final @NotNull HumanEntity player, final @NotNull ItemStack itemStack, final int count) {
        final ItemStack item = itemStack.clone();
        final int maxStackSize = item.getMaxStackSize();
        final PlayerInventory inventory = player.getInventory();
        final World world = player.getWorld();
        final Location location = player.getLocation();

        int itemsLeft = count;
        final ItemStack[] contents = inventory.getStorageContents();
        // enlarge existing stacks
        for (final ItemStack slotContent : contents) {
            if (slotContent == null || !slotContent.isSimilar(item)) continue;
            final int slotAmount = slotContent.getAmount();
            final int addAmount = Math.min(itemsLeft, maxStackSize - slotAmount);
            slotContent.setAmount(slotAmount + addAmount);
            itemsLeft -= addAmount;
            if (itemsLeft == 0) return;
        }
        // create new stacks in empty space
        for (int slot = 0; slot < contents.length; slot++) {
            if (contents[slot] != null) continue;
            final int slotAmount = Math.min(itemsLeft, maxStackSize);
            item.setAmount(slotAmount);
            inventory.setItem(slot, item);
            itemsLeft -= slotAmount;
            if (itemsLeft == 0) return;
        }
        // drop the rest on the ground
        while (itemsLeft > 0) {
            final int dropAmount = Math.min(itemsLeft, maxStackSize);
            item.setAmount(dropAmount);
            world.dropItem(location, item);
            itemsLeft -= dropAmount;
        }
    }

    private static int generate(final @NotNull CommandContext<CommandSourceStack> ctx, final int centerX, final int centerZ, final int bps) {
        final Location location = ctx.getSource().getLocation();
        final int width = IntegerArgumentType.getInteger(ctx, "width");
        final int length = IntegerArgumentType.getInteger(ctx, "length");
        final int x1 = centerX - width / 2;
        final int z1 = centerZ - length / 2;
        final int x2 = x1 + width;
        final int z2 = z1 + length;
        final CommandSender sender = ctx.getSource().getSender();
        return CrateGenerator.INSTANCE.generate(sender, location, x1, z1, x2, z2, bps) ? SUCCESS : FAILURE;
    }

    private static int removeCrates(final @NotNull CommandContext<CommandSourceStack> ctx) {
        return CrateGenerator.INSTANCE.removeCrates(ctx.getSource().getSender()) ? SUCCESS : FAILURE;
    }

    private static int clearCrates(final @NotNull CommandContext<CommandSourceStack> ctx) {
        return CrateGenerator.INSTANCE.clearCrates(ctx.getSource().getSender()) ? SUCCESS : FAILURE;
    }

    private static int refillCrates(final @NotNull CommandContext<CommandSourceStack> ctx) {
        return CrateGenerator.INSTANCE.refillCrates(ctx.getSource().getSender()) ? SUCCESS : FAILURE;
    }

    private static int cancelGeneration(final @NotNull CommandContext<CommandSourceStack> ctx) {
        return CrateGenerator.INSTANCE.cancel(ctx.getSource().getSender()) ? SUCCESS : FAILURE;
    }

    private static int startGame(final @NotNull CommandContext<CommandSourceStack> ctx, final int centerX, final int centerZ, final int bps, final int duration) {
        final CommandSender sender = ctx.getSource().getSender();
        if (Game.running()) {
            sender.sendMessage(Components.prefix("<red>A game is already running!"));
            return FAILURE;
        }
        if (Bukkit.getOnlinePlayers().size() <= 1) {
            sender.sendMessage(Components.prefix("<red>There must be at least 2 players online to start a game!"));
            return FAILURE;
        }
        new Game(
                sender,
                ctx.getSource().getLocation(),
                duration,
                IntegerArgumentType.getInteger(ctx, "size"),
                centerX,
                centerZ,
                bps
        );
        return SUCCESS;
    }

    private static int generateAirDrop(final @NotNull CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final BlockPositionResolver resolver = ctx.getArgument("pos", BlockPositionResolver.class);
        final BlockPosition pos = resolver.resolve(ctx.getSource());
        final Location location = pos.toLocation(ctx.getSource().getLocation().getWorld());
        final Block block = location.getBlock();
        final CommandSender sender = ctx.getSource().getSender();
        if (!block.isEmpty()) {
            sender.sendMessage(Components.prefix("<red>Air drop location must be empty!"));
            return FAILURE;
        }
        CrateGenerator.INSTANCE.generateCrate(CustomElement.of(AirDrop.class), location);
        sender.sendMessage(Components.prefix("<green>Air drop generated successfully!"));
        return SUCCESS;
    }

    private static int stopGame(final @NotNull CommandContext<CommandSourceStack> ctx) {
        final CommandSender sender = ctx.getSource().getSender();
        if (!Game.running()) {
            sender.sendMessage(Components.prefix("<red>No game is running!"));
            return FAILURE;
        }
        Game.instance().stop();
        sender.sendMessage(Components.prefix("<green>Game stopped successfully"));
        return SUCCESS;
    }

    private static int endGame(final @NotNull CommandContext<CommandSourceStack> ctx) {
        final CommandSender sender = ctx.getSource().getSender();
        if (!Game.running()) {
            sender.sendMessage(Components.prefix("<red>No game is running!"));
            return FAILURE;
        }
        Game.instance().endGame();
        sender.sendMessage(Components.prefix("<green>Game ended successfully"));
        return SUCCESS;
    }
}
