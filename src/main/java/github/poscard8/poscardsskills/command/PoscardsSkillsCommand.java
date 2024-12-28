package github.poscard8.poscardsskills.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourceData;
import github.poscard8.poscardsskills.secret.Secret;
import github.poscard8.poscardsskills.secret.SecretData;
import github.poscard8.poscardsskills.secret.Secrets;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillData;
import github.poscard8.poscardsskills.skill.SkillInstance;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * All-in-one command for the mod.
 * <p>{@code /poscardsskills skill add ...}: Adds xp to skills.</p>
 * <p>{@code /poscardsskills skill set ...}: Sets total xp of skills.</p>
 * <p>{@code /poscardsskills skill reset ...}: Resets skills. Does not reset secrets and ascensions.</p>
 * <p>{@code /poscardsskills secret unlock ...}: Unlocks secrets.</p>
 * <p>{@code /poscardsskills secret lock ...}: Locks secrets. </p>
 * <p>{@code /poscardsskills ascension ascend ...}: Ascends the player without a catalyst. Resets the skill progress</p>
 * <p>{@code /poscardsskills ascension set ...}: Sets the ascension count of the player to the given count.
 * Does not reset the skill progress</p>
 * <p>{@code /poscardsskills xp_source ... reset}: Resets the experience source data of the player.</p>
 * <p>{@code /poscardsskills max_out ...} Quick way to get maximum skills.</p>
 * <p>{@code /poscardsskills reset ...}: Resets the skill data of the player. This includes skills, secrets and ascensions.</p>
 */
public final class PoscardsSkillsCommand {

    private PoscardsSkillsCommand() {}

    private static final SimpleCommandExceptionType NO_SKILLS = new SimpleCommandExceptionType(Component.translatable("command.github.poscard8.poscardsskills.no_skill"));
    private static final SimpleCommandExceptionType NO_SECRETS = new SimpleCommandExceptionType(Component.translatable("command.github.poscard8.poscardsskills.no_secret"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        LiteralArgumentBuilder<CommandSourceStack> argumentBuilder = Commands.literal(PoscardsSkills.ID).requires(player -> player.hasPermission(2))
                .then(Commands.literal("skill")
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
                                                        .executes(ctx -> addXP(ctx.getSource(), player(ctx), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                                .then(Commands.literal("level")
                                                        .executes(ctx -> addLevel(ctx.getSource(), player(ctx), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                                .executes(ctx -> addXP(ctx.getSource(), player(ctx), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))))
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
                                                        .executes(ctx -> setXP(ctx.getSource(), player(ctx), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                                .then(Commands.literal("level")
                                                        .executes(ctx -> setLevel(ctx.getSource(), player(ctx), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))
                                                .executes(ctx -> setXP(ctx.getSource(), player(ctx), ctx.getArgument("skill", String.class), IntegerArgumentType.getInteger(ctx, "amount"))))))
                        .then(Commands.literal("reset")
                                .then(Commands.argument("player", EntityArgument.players())
                                        .then(Commands.argument("skill", SkillArgumentType.of())
                                                .executes(ctx -> resetSkill(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ctx.getArgument("skill", String.class)))))
                                .then(Commands.argument("skill", SkillArgumentType.of())
                                        .executes(ctx -> resetSkill(ctx.getSource(), player(ctx), ctx.getArgument("skill", String.class))))))
                .then(Commands.literal("secret")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.literal("unlock")
                                        .then(Commands.argument("secret", SecretArgumentType.of())
                                                .executes(ctx -> unlockSecret(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ctx.getArgument("secret", String.class)))))
                                .then(Commands.literal("lock")
                                        .then(Commands.argument("secret", SecretArgumentType.of())
                                                .executes(ctx -> lockSecret(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ctx.getArgument("secret", String.class))))))
                        .then(Commands.literal("unlock")
                                .then(Commands.argument("secret", SecretArgumentType.of())
                                        .executes(ctx -> unlockSecret(ctx.getSource(), player(ctx), ctx.getArgument("secret", String.class)))))
                        .then(Commands.literal("lock")
                                .then(Commands.argument("secret", SecretArgumentType.of())
                                        .executes(ctx -> lockSecret(ctx.getSource(), player(ctx), ctx.getArgument("secret", String.class))))))
                .then(Commands.literal("ascension")
                        .then(Commands.literal("ascend")
                                .then(Commands.argument("player", EntityArgument.players())
                                        .executes(ctx -> ascend(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"))))
                                .executes(ctx -> ascend(ctx.getSource(), player(ctx))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("player", EntityArgument.players())
                                        .then(Commands.argument("count", IntegerArgumentType.integer(0))
                                                .executes(ctx -> setAscensions(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), IntegerArgumentType.getInteger(ctx, "count")))))))
                .then(Commands.literal("xp_source")
                        .then(Commands.argument("player", EntityArgument.players())
                                .then(Commands.literal("reset")
                                        .executes(ctx -> resetXPSources(ctx.getSource(), EntityArgument.getPlayers(ctx, "player")))))
                        .then(Commands.literal("reset")
                                .executes(ctx -> resetXPSources(ctx.getSource(), player(ctx)))))
                .then(Commands.literal("max_out")
                        .then(Commands.argument("player", EntityArgument.players())
                                .executes(ctx -> maxOut(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"))))
                        .executes(ctx -> maxOut(ctx.getSource(), player(ctx))))
                .then(Commands.literal("reset")
                        .then(Commands.argument("player", EntityArgument.players())
                                .executes(ctx -> reset(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"))))
                        .executes(ctx -> reset(ctx.getSource(), player(ctx))));

        dispatcher.register(argumentBuilder);
    }

    static int addXP(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string, int xp) throws CommandSyntaxException {

        List<Skill> skills = skillsFromString(string);
        boolean levelUp;
        boolean playSounds = true;

        for (ServerPlayer player : players) {

            for (Skill skill : skills) {

                levelUp = SkillInstance.of(player, skill).addXP(player, xp, false, playSounds);
                if (levelUp) playSounds = false;
            }
        }

        if (skills.isEmpty()) { throw NO_SKILLS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.skill_add_xp_success", xp, skills.size(), players.size()), true);
        return xp;
    }

    static int addLevel(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string, int level) throws CommandSyntaxException {

        List<Skill> skills = skillsFromString(string);
        boolean levelUp;
        boolean playSounds = true;

        for (ServerPlayer player : players) {

            for (Skill skill : skills) {

                levelUp = SkillInstance.of(player, skill).addLevel(player, level, playSounds);
                if (levelUp) playSounds = false;
            }
        }

        if (skills.isEmpty()) { throw NO_SKILLS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.skill_add_level_success", level, skills.size(), players.size()), true);
        return level;
    }

    static int setXP(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string, int xp) throws CommandSyntaxException {

        List<Skill> skills = skillsFromString(string);
        boolean levelUp;
        boolean playSounds = true;

        for (ServerPlayer player : players) {

            for (Skill skill : skills) {

                levelUp = SkillInstance.of(player, skill).setXP(player, xp, playSounds);
                if (levelUp) playSounds = false;
            }
        }

        if (skills.isEmpty()) { throw NO_SKILLS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.skill_set_xp_success", skills.size(), players.size(), xp), true);
        return xp;
    }

    static int setLevel(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string, int level) throws CommandSyntaxException {

        List<Skill> skills = skillsFromString(string);
        boolean levelUp;
        boolean playSounds = true;

        for (ServerPlayer player : players) {

            for (Skill skill : skills) {

                levelUp = SkillInstance.of(player, skill).setLevel(player, level, playSounds);
                if (levelUp) playSounds = false;
            }
        }

        if (skills.isEmpty()) { throw NO_SKILLS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.skill_set_level_success", skills.size(), players.size(), level), true);
        return level;
    }

    static int resetSkill(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string) throws CommandSyntaxException {

        List<Skill> skills = skillsFromString(string);

        for (ServerPlayer player : players) {

            for (Skill skill : skills) SkillInstance.of(player, skill).reset(player);
        }

        if (skills.isEmpty()) { throw NO_SKILLS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.skill_reset_success", skills.size(), players.size()), true);
        return 0;
    }

    static int unlockSecret(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string) throws CommandSyntaxException {

        List<Secret> secrets = secretsFromString(string);

        for (Secret secret : secrets) {

            for (ServerPlayer player : players) SecretData.of(player).unlock(player, secret, false);
        }

        if (secrets.isEmpty()) { throw NO_SECRETS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.secret_unlock_success", secrets.size(), players.size()), true);
        return 0;
    }

    static int lockSecret(CommandSourceStack sourceStack, Collection<ServerPlayer> players, String string) throws CommandSyntaxException {

        List<Secret> secrets = secretsFromString(string);

        for (Secret secret : secrets) {

            for (ServerPlayer player : players) SecretData.of(player).lock(player, secret);
        }

        if (secrets.isEmpty()) { throw NO_SECRETS.create(); }

        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.secret_lock_success", secrets.size(), players.size()), true);
        return 0;
    }

    static int ascend(CommandSourceStack sourceStack, Collection<ServerPlayer> players) {

        for (ServerPlayer player : players) SkillData.of(player).ascend();
        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.ascension_success", players.size()), true);
        return 0;
    }

    static int setAscensions(CommandSourceStack sourceStack, Collection<ServerPlayer> players, int count) {

        for (ServerPlayer player : players) SkillData.of(player).setAscensions(count);
        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.ascension_set_success", players.size(), count), true);
        return 0;
    }

    static int resetXPSources(CommandSourceStack sourceStack, Collection<ServerPlayer> players) {

        try {

            for (ServerPlayer player : players) ExperienceSourceData.reset(player);

            sourceStack.sendSuccess(Component.translatable("command.poscardsskills.xp_source_success", players.size()), true);
            return 1;

        } catch (Exception exception) { return -1; }
    }

    static int maxOut(CommandSourceStack sourceStack, Collection<ServerPlayer> players) {

        for (ServerPlayer player : players) SkillData.of(player).maxOut();
        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.max_out_success", players.size()), true);
        return 0;
    }

    static int reset(CommandSourceStack sourceStack, Collection<ServerPlayer> players) {

        for (ServerPlayer player : players) SkillData.of(player).reset();
        sourceStack.sendSuccess(Component.translatable("command.poscardsskills.reset_success", players.size()), true);
        return 0;
    }

    static Collection<ServerPlayer> player(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {

        return List.of(ctx.getSource().getPlayerOrException());
    }

    static List<Skill> skillsFromString(String string) {

        if (string.equals("#all")) return PoscardsSkills.getSkillHandler().getValues().stream().toList();

        ResourceLocation location = ResourceLocation.tryParse(string);
        if (location == null) return List.of();

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);
        return optional.map(List::of).orElse(List.of());
    }

    static List<Secret> secretsFromString(String string) {

        if (string.equals("#all")) return Secrets.getValues();

        ResourceLocation location = ResourceLocation.tryParse(string);
        if (location == null) return List.of();

        List<Secret> secrets = new ArrayList<>();
        Secret secret = Secrets.byKey(location);

        if (secret != null) secrets.add(secret);
        return secrets;
    }

}
