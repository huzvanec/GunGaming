package cz.jeme.gungaming.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import cz.jeme.gungaming.command.argument.ChatModeArgument;
import cz.jeme.gungaming.game.Game;
import cz.jeme.gungaming.game.GameTeam;
import cz.jeme.gungaming.util.Components;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
@ApiStatus.Internal
public final class ChatCommand {
    private static final int SUCCESS = Command.SINGLE_SUCCESS;
    private static final int FAILURE = 0;

    private final Plugin plugin;

    public ChatCommand(final Plugin plugin, final Commands commands) {
        this.plugin = plugin;
        commands.register(
                plugin.getPluginMeta(),
                build(),
                "Command to switch chat modes during a GunGaming game",
                List.of("c")
        );
    }

    private LiteralCommandNode<CommandSourceStack> build() {
        return literal("chat")
                .then(argument("mode", new ChatModeArgument())
                        .executes(this::chat)
                )
                .build();
    }

    private int chat(final com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        if (!(ctx.getSource().getSender() instanceof final Player player)) return FAILURE;
        if (!Game.running()) {
            player.sendMessage(Components.prefix("<red>Game is not running!"));
            return FAILURE;
        }
        final Game.ChatMode mode = ctx.getArgument("mode", Game.ChatMode.class);
        if (!GameTeam.isPlayer(player)) {
            player.sendMessage(Components.prefix("<red>You cannot change chat modes!"));
            return FAILURE;
        }
        Game.instance().chatMode(player, mode);
        player.sendMessage(Components.prefix("<green>Chat mode changed to <yellow>" + mode));
        return SUCCESS;
    }
}
