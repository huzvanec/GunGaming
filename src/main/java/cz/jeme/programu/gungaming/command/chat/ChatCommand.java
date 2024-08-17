package cz.jeme.programu.gungaming.command.chat;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import cz.jeme.programu.gungaming.game.Game;
import cz.jeme.programu.gungaming.game.GameTeam;
import cz.jeme.programu.gungaming.util.Components;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
public final class ChatCommand {
    private static final int SUCCESS = Command.SINGLE_SUCCESS;
    private static final int FAILURE = 0;

    private ChatCommand() {
        throw new AssertionError();
    }

    public static void register(final @NotNull JavaPlugin plugin, final @NotNull Commands commands) {
        commands.register(
                plugin.getPluginMeta(),
                build(),
                "Command to switch chat modes during a GunGaming game",
                List.of("c")
        );
    }

    private static @NotNull LiteralCommandNode<CommandSourceStack> build() {
        return literal("chat")
                .then(argument("mode", new ChatModeArgument())
                        .executes(ChatCommand::chat)
                )
                .build();
    }

    private static int chat(final @NotNull com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
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
