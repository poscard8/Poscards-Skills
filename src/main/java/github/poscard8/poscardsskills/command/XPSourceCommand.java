package github.poscard8.poscardsskills.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourceData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.List;

public final class XPSourceCommand {

    private XPSourceCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        LiteralArgumentBuilder<CommandSourceStack> argumentBuilder = Commands.literal("xpsource").requires(player -> player.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.literal("reset")
                                .executes(ctx -> resetXPSources(ctx.getSource(), EntityArgument.getPlayers(ctx, "player")))))
                .then(Commands.literal("reset").executes(ctx -> resetXPSources(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException()))));

        dispatcher.register(argumentBuilder);
    }

    static int resetXPSources(CommandSourceStack sourceStack, Collection<ServerPlayer> players) {

        try {

            for (ServerPlayer player : players) ExperienceSourceData.reset(player);

            sourceStack.sendSuccess(Component.translatable("command.poscardsskills.xp_source_success", players.size()), true);
            return 1;

        } catch (Exception exception) { return -1; }
    }

}
