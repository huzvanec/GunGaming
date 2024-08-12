package cz.jeme.programu.gungaming.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import cz.jeme.programu.gungaming.CustomElement;
import cz.jeme.programu.gungaming.GunGaming;
import cz.jeme.programu.gungaming.config.ConfigValue;
import cz.jeme.programu.gungaming.config.GameConfig;
import cz.jeme.programu.gungaming.config.GenerationConfig;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.game.lobby.Lobby;
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
import net.minecraft.core.BlockPos;
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
import java.util.Map;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;


@SuppressWarnings("UnstableApiUsage")
public final class GGCommand {
    private static final int SUCCESS = Command.SINGLE_SUCCESS;
    private static final int FAILURE = 0;

    private static boolean isAdmin(final @NotNull CommandSourceStack source) {
        return source.getSender().hasPermission("gungaming.gg.admin");
    }

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
                .then(literal("give")
                        .then(argument("targets", ArgumentTypes.players())
                                .then(argument("tag", new CustomItemTagArgument())
                                        .then(argument("item", new CustomItemArgument())
                                                .executes(ctx -> give(ctx, 1))
                                                .then(argument("count", IntegerArgumentType.integer(1))
                                                        .executes(ctx -> give(ctx, IntegerArgumentType.getInteger(ctx, "count")))
                                                )
                                        )
                                )
                        )
                )
                .then(literal("crates")
                        .requires(GGCommand::isAdmin)
                        .then(literal("generation")
                                .then(literal("start")
                                        .executes(GGCommand::startGeneration)
                                )
                                .then(literal("cancel")
                                        .executes(GGCommand::cancelGeneration)
                                )
                                .then(literal("air_drop")
                                        .then(argument("pos", ArgumentTypes.blockPosition())
                                                .executes(GGCommand::generateAirDrop)
                                        )
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
                )
                .then(literal("game")
                        .requires(GGCommand::isAdmin)
                        .then(literal("start")
                                .executes(GGCommand::startGame)
                        )
                        .then(literal("stop")
                                .executes(GGCommand::stopGame)
                        )
                        .then(literal("end")
                                .executes(GGCommand::endGame)
                        )
                        .then(literal("lobby")
                                .then(literal("enable")
                                        .executes(GGCommand::enableLobby)
                                )
                                .then(literal("disable")
                                        .executes(GGCommand::disableLobby)
                                )
                        )
                )
                .then(literal("config")
                        .requires(GGCommand::isAdmin)
                        .then(configVisitor("game", GameConfig.values()))
                        .then(configVisitor("generation", GenerationConfig.values()))
                )
                .build();
    }

    private static @NotNull LiteralArgumentBuilder<CommandSourceStack> configVisitor(final @NotNull String literal, final @NotNull Map<String, ConfigValue<?>> values) {
        final LiteralArgumentBuilder<CommandSourceStack> builder = literal(literal);
        for (final ConfigValue<?> value : values.values()) {
            final ArgumentType<?> type = value.type();
            final String name = value.name();
            builder.then(literal(name)
                    .executes(ctx -> {
                        final CommandSender sender = ctx.getSource().getSender();
                        final String valueStr = value.getString(((net.minecraft.commands.CommandSourceStack) ctx.getSource()));
                        sender.sendMessage(Components.prefix(
                                "<green><aqua>"
                                + literal + ":" + name
                                + "</aqua> is currently set to <yellow>" + valueStr));
                        return SUCCESS;
                    })
                    .then(argument("value", type)
                            .executes(ctx -> {
                                final CommandSender sender = ctx.getSource().getSender();
                                if (Game.running()) {
                                    sender.sendMessage(Components.prefix("<red>You can't modify configuration while a game is running!"));
                                    return FAILURE;
                                }
                                // i love this part
                                @SuppressWarnings("unchecked") final ConfigValue<Object> valueObj = (ConfigValue<Object>) value;
                                valueObj.set(ctx.getArgument(
                                        "value",
                                        Object.class
                                ));
                                final String valueStr = value.getString(((net.minecraft.commands.CommandSourceStack) ctx.getSource()));
                                sender.sendMessage(Components.prefix("<green>Set <aqua>" + literal + ":" + name
                                                                     + "</aqua> to <yellow>" + valueStr));
                                return SUCCESS;
                            })
                    )
            );
        }
        return builder;
    }

    private static int version(final @NotNull CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().getSender().sendMessage(Components.prefix("<green>Running <#6786C8>Gun</#6786C8><#4C618D>Gaming</#4C618D> v" + GunGaming.plugin().getPluginMeta().getVersion()));
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

    private static int startGeneration(final @NotNull CommandContext<CommandSourceStack> ctx) {
        final Location location = ctx.getSource().getLocation();
        final int width = GenerationConfig.WIDTH.get();
        final int length = GenerationConfig.LENGTH.get();
        final BlockPos center = GenerationConfig.CENTER.get()
                .getBlockPos((net.minecraft.commands.CommandSourceStack) ctx.getSource());
        final int x1 = center.getX() - width / 2;
        final int z1 = center.getZ() - length / 2;
        final int x2 = x1 + width;
        final int z2 = z1 + length;
        final CommandSender sender = ctx.getSource().getSender();
        return CrateGenerator.INSTANCE.generate(sender, location, x1, z1, x2, z2, GenerationConfig.BPS.get())
                ? SUCCESS
                : FAILURE;
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

    private static int startGame(final @NotNull CommandContext<CommandSourceStack> ctx) {
        // errors are handled in Game constructor
        try {
            new Game(ctx.getSource());
            return SUCCESS;
        } catch (final IllegalStateException e) {
            return FAILURE;
        }
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
        Game.instance().stopGame();
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

    private static int enableLobby(final @NotNull CommandContext<CommandSourceStack> ctx) {
        // errors are handled in Lobby constructor
        try {
            new Lobby(ctx.getSource());
            return SUCCESS;
        } catch (final IllegalStateException e) {
            return FAILURE;
        }
    }

    private static int disableLobby(final @NotNull CommandContext<CommandSourceStack> ctx) {
        final CommandSender sender = ctx.getSource().getSender();
        if (!Lobby.enabled()) {
            sender.sendMessage(Components.prefix("<red>Lobby is not enabled!"));
            return FAILURE;
        }
        Lobby.instance().disable();
        sender.sendMessage(Components.prefix("<green>Lobby disabled successfully"));
        return SUCCESS;
    }
}
