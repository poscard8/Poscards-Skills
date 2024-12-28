package github.poscard8.poscardsskills.experiencesource;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static github.poscard8.poscardsskills.PoscardsSkills.asResource;

/**
 * The ways players can gain skill xp. Learn more on the wiki.
 */
public interface ExperienceSource {

    ResourceLocation

            BLOCK_KEY = asResource("block"),
            CONSUME_KEY = asResource("consume"),
            CRAFT_KEY = asResource("craft"),
            ENCHANTING_TABLE_KEY = asResource("enchanting_table"),
            FISH_KEY = asResource("fish"),
            ENTITY_KEY = asResource("entity"),
            CHEST_KEY = asResource("chest"),
            SMELT_KEY = asResource("smelt"),
            ADVANCEMENT_KEY = asResource("advancement"),
            ANVIL_ENCHANT_KEY = asResource("anvil_enchant"),
            STRUCTURE_KEY = asResource("structure");

    static Collection<ExperienceSource> filterBy(Skill skill) { return PoscardsSkills.getXPSourceHandler().stream().filter(xpSource -> xpSource.getSkill().equals(skill)).toList(); }

    static <E extends ExperienceSource> Collection<E> filterBy(Class<E> clazz) { return PoscardsSkills.getXPSourceHandler().stream().filter(xpSource -> xpSource.getClass().equals(clazz)).map(clazz::cast).toList(); }

    static boolean canGainXP(@Nullable Player player) { return player != null && !player.isSpectator() && !player.isCreative(); }

    default void applyTo(ServerPlayer player) { applyTo(player, 1); }

    default void applyTo(ServerPlayer player, int multiplier) { SkillInstance.of(player, getSkill()).addXP(player, getXP() * multiplier); }

    int getXP();

    Skill getSkill();

}
