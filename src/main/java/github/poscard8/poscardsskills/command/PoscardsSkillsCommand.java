package github.poscard8.poscardsskills.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillInstance;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class PoscardsSkillsCommand {

    private PoscardsSkillsCommand() {}

    private static final SimpleCommandExceptionType NO_SKILLS = new SimpleCommandExceptionType(Component.translatable("command.poscardsskills.no_skill"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        LiteralArgumentBuilder<CommandSourceStack> argumentBuilder = Commands.literal(PoscardsSkills.ID).requires(player -> player.hasPermission(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("skill", SkillArgumentType.of())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .then(Commands.literal("xp")
                                                        .executes(ctx -> addXP(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                                .then(Commands.literal("level")
                                                        .executes(ctx -> addLevel(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                                .executes(ctx -> addXP(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))))
                        .then(Commands.argument("skill", SkillArgumentType.of())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .then(Commands.literal("xp")
                                                .executes(ctx -> addXP(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException()), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                        .then(Commands.literal("level")
                                                .executes(ctx -> addLevel(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException()), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                        .executes(ctx -> addXP(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException()), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("skill", SkillArgumentType.of())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .then(Commands.literal("xp")
                                                        .executes(ctx -> setXP(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                                .then(Commands.literal("level")
                                                        .executes(ctx -> setLevel(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                                .executes(ctx -> setXP(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))))
                        .then(Commands.argument("skill", SkillArgumentType.of())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .then(Commands.literal("xp")
                                                .executes(ctx -> setXP(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException()), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                        .then(Commands.literal("level")
                                                .executes(ctx -> setLevel(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException()), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                        .executes(ctx -> setXP(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException()), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))))
                .then(Commands.literal("reset")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.argument("skill", SkillArgumentType.of())
                                        .executes(ctx -> reset(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ctx.getArgument("skill", String.class)))))
                        .then(Commands.argument("skill", SkillArgumentType.of())
                                .executes(ctx -> reset(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException()), ctx.getArgument("skill", String.class))))
                        .executes(ctx -> reset(ctx.getSource(), List.of(ctx.getSource().getPlayerOrException()), "#all")));

        dispatcher.register(argumentBuilder);

    }

    static int addXP(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string, int xp) throws CommandSyntaxException {

        List<Skill> skills = skillsFromString(string);
        boolean levelUp;
        boolean playSounds = true;

        for (Player player : players) {

            for (Skill skill : skills) {

                levelUp = SkillInstance.of(player, skill).addXP(player, xp, null, true, playSounds);
                if (levelUp) playSounds = false;
            }
        }

        if (skills.size() == 0) { throw NO_SKILLS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.skill_add_xp_success", xp, skills.size(), players.size()), true);
        return xp;
    }

    static int addLevel(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string, int level) throws CommandSyntaxException {

        List<Skill> skills = skillsFromString(string);
        boolean levelUp;
        boolean playSounds = true;

        for (Player player : players) {

            for (Skill skill : skills) {

                levelUp = SkillInstance.of(player, skill).addLevel(player, level, playSounds);
                if (levelUp) playSounds = false;
            }
        }

        if (skills.size() == 0) { throw NO_SKILLS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.skill_add_level_success", level, skills.size(), players.size()), true);
        return level;
    }

    static int setXP(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string, int xp) throws CommandSyntaxException {

        List<Skill> skills = skillsFromString(string);
        boolean levelUp;
        boolean playSounds = true;

        for (Player player : players) {

            for (Skill skill : skills) {

                levelUp = SkillInstance.of(player, skill).setXP(player, xp, playSounds);
                if (levelUp) playSounds = false;
            }
        }

        if (skills.size() == 0) { throw NO_SKILLS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.skill_set_xp_success", skills.size(), players.size(), xp), true);
        return xp;
    }

    static int setLevel(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string, int level) throws CommandSyntaxException {

        List<Skill> skills = skillsFromString(string);
        boolean levelUp;
        boolean playSounds = true;

        for (Player player : players) {

            for (Skill skill : skills) {

                levelUp = SkillInstance.of(player, skill).setLevel(player, level, playSounds);
                if (levelUp) playSounds = false;
            }
        }

        if (skills.size() == 0) { throw NO_SKILLS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.skill_set_level_success", skills.size(), players.size(), level), true);
        return level;
    }

    static int reset(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string) throws CommandSyntaxException {

        List<Skill> skills = skillsFromString(string);
        for (Player player : players) {

            for (Skill skill : skills) { SkillInstance.of(player, skill).reset(player); }
        }

        if (skills.size() == 0) { throw NO_SKILLS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.skill_reset_success", skills.size(), players.size()), true);
        return 0;
    }

    private static List<Skill> skillsFromString(String string) {

        ResourceLocation location = ResourceLocation.tryParse(string);
        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byLocation(location);

        if (Objects.equals(string, "#all")) {

            return PoscardsSkills.getSkillHandler().getValues().stream().toList();

        } else return optional.map(List::of).orElse(List.of());

    }


}
