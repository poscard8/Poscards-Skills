package github.poscard8.poscardsskills.secret;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import github.poscard8.poscardsskills.registry.PSBlocks;
import github.poscard8.poscardsskills.secret.types.AdvancementSecret;
import github.poscard8.poscardsskills.secret.types.BlockSecret;
import github.poscard8.poscardsskills.secret.types.SkillSecret;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillMilestone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static github.poscard8.poscardsskills.PoscardsSkills.asResource;

/**
 * Class for registering secrets and storing secret data.
 * <p>New secrets are registered with {@link #register(ResourceLocation, Secret)}</p>
 */
@SuppressWarnings("unused")
public class Secrets {

    protected static final Map<ResourceLocation, Secret> REGISTRY_MAP = new HashMap<>();
    protected static final Map<Secret, Integer> INDEX_MAP = new HashMap<>();

    public static final Secret

        PROFILE_BUTTON = register(asResource("profile_button"), new Secret(1)),

        WOODCUTTING = register(asResource("woodcutting"), new SkillSecret(Skill.WOODCUTTING_KEY, 17)),
        MINING = register(asResource("mining"), new SkillSecret(Skill.MINING_KEY, 35)),
        FARMING = register(asResource("farming"), new SkillSecret(Skill.FARMING_KEY, 21)),
        COMBAT = register(asResource("combat"), new SkillSecret(Skill.COMBAT_KEY, 50, 6)),
        EXPLORING = register(asResource("exploring"), new SkillSecret(Skill.EXPLORING_KEY, 25)),
        ENCHANTING = register(asResource("enchanting"), new SkillSecret(Skill.ENCHANTING_KEY, 30, 4)),

        JADE = register(asResource("jade"), new BlockSecret(state -> state.is(PSBlocks.ROUGH_JADE.get()), 0.001F, 1)),
        JASPER = register(asResource("jasper"), new BlockSecret(state -> state.is(PSBlocks.ROUGH_JASPER.get()), 0.001F, 2)),
        MARBLE = register(asResource("marble"), new BlockSecret(state -> state.is(PSBlocks.ROUGH_MARBLE.get()), 0.001F, 3)),
        DIAMOND = register(asResource("diamond"), new BlockSecret(state -> state.is(Blocks.DIAMOND_ORE), 0.04F, 6)),
        SAND = register(asResource("sand"), new BlockSecret(state -> state.is(BlockTags.SAND), 0.0002F, 4)),

        POTIONS = register(asResource("potions"), new AdvancementSecret(new ResourceLocation("nether/all_potions"), 5)),
        END_CITY = register(asResource("end_city"), new AdvancementSecret(new ResourceLocation("end/find_end_city"), 3));

    public static Map<ResourceLocation, Secret> getRegistry() { return ImmutableMap.copyOf(REGISTRY_MAP); }

    public static List<ResourceLocation> getKeys() { return ImmutableList.copyOf(REGISTRY_MAP.keySet()); }

    public static List<Secret> getValues() { return ImmutableList.copyOf(REGISTRY_MAP.values()); }

    public static List<Secret> getSortedValues() {

        return REGISTRY_MAP.values().stream().filter(Secret::isRegistered).sorted(Comparator.comparingInt(Secret::getIndex)).toList();
    }

    public static <T extends Secret> T register(ResourceLocation key, T secret) {

        REGISTRY_MAP.put(key, secret);
        INDEX_MAP.put(secret, INDEX_MAP.size());
        return secret;
    }

    @Nullable
    public static Secret remove(ResourceLocation key) {

        Secret removed = REGISTRY_MAP.remove(key);
        if (removed != null) INDEX_MAP.remove(removed);

        return removed;
    }

    public static Secret byKey(ResourceLocation key) { return REGISTRY_MAP.get(key); }

    @Nullable
    public static ResourceLocation keyOf(Secret secret) {

        for (ResourceLocation key : REGISTRY_MAP.keySet()) {

            if (secret.equals(byKey(key))) return key;
        }
        return null;
    }

    public static int indexOf(Secret secret) { return INDEX_MAP.getOrDefault(secret, -1); }

    public static int getTotalCount() { return getValues().size(); }

    public static void handleSkill(ServerPlayer player, SkillMilestone milestone) {

        for (Secret secret : getValues()) {

            if (secret instanceof SkillSecret skillSecret) skillSecret.tryUnlock(player, milestone);
        }
    }

    public static void handleBlock(ServerPlayer player, BlockState state) {

        for (Secret secret : getValues()) {

            if (secret instanceof BlockSecret blockSecret) blockSecret.tryUnlock(player, state);
        }
    }

    public static void handleAdvancement(ServerPlayer player, ResourceLocation key) {

        for (Secret secret : getValues()) {

            if (secret instanceof AdvancementSecret advancementSecret) {

                advancementSecret.tryUnlock(player, key);
            }
        }
    }

}
