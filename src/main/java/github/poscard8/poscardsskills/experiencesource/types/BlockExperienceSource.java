package github.poscard8.poscardsskills.experiencesource.types;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.experiencesource.DataExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourceData;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourcePredicates;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Gives xp when the player breaks blocks.
 * <p>When the player places those blocks, debt is added
 * and the player cannot gain xp until those blocks are
 * broken again.</p>
 * <p>{@code "properties"} can be defined in the format.
 * Different state conditions can be added with {@code "properties"}.
 * Those conditions can be inverted by adding {@code '!'} at the beginning.</p>
 * <p>See the wiki and {@code "data/github.poscard8.poscardsskills/poscardsmods/xp_sources/farming.json"} for more info.</p>
 */
public final class BlockExperienceSource implements DataExperienceSource<Integer> {

    public final Skill skill;
    public final int xp;
    public final Predicate<BlockState> predicate;

    final String name;
    final Map<ServerPlayer, Integer> xpDebtMap = new HashMap<>();

    BlockExperienceSource(Skill skill, int xp, Predicate<BlockState> predicate, String name) {

        this.skill = skill;
        this.xp = xp;
        this.predicate = predicate;
        this.name = name;
    }

    @SuppressWarnings("deprecation")
    public static BlockExperienceSource fromJsonObject(ResourceLocation location, JsonObject jsonObject) {

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byKey(location);
        if (optional.isEmpty()) return null;

        Skill skill = optional.get();
        int xp = GsonHelper.getAsInt(jsonObject, "xp");
        String blockArg;
        String name;
        Predicate<BlockState> predicate;

        if (jsonObject.has("block")) {

            blockArg = GsonHelper.getAsString(jsonObject, "block");
            name = String.format("block,single,%s", blockArg);

            if (blockArg.equals("ALL")) {

                return new BlockExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_BLOCK_STATE, name);
            }

            ResourceLocation blockKey = ResourceLocation.tryParse(blockArg);
            Block block = ForgeRegistries.BLOCKS.getValue(blockKey);
            assert block != null;

            if (jsonObject.has("properties")) {

                String defaultPredicateArg = GsonHelper.getAsString(jsonObject, "properties");
                boolean invert = defaultPredicateArg.startsWith("!");

                String predicateArg = invert ? defaultPredicateArg.substring(1) : defaultPredicateArg;
                String stateKey = blockArg + predicateArg;

                try {

                    BlockState parsed = BlockStateParser.parseForBlock(Registry.BLOCK, stateKey, true).blockState();
                    Predicate<BlockState> rawPredicate = state -> isSameState(state, parsed);
                    predicate = invert ? rawPredicate.negate() : rawPredicate;

                } catch (CommandSyntaxException e) { throw new RuntimeException("Parsing error loading experience sources."); }

            } else { predicate = state -> state.is(block); }

        } else {

            blockArg = GsonHelper.getAsString(jsonObject, "tag");
            name = String.format("block,tag,%s", blockArg);

            if (blockArg.equals("ALL")) {

                return new BlockExperienceSource(skill, xp, ExperienceSourcePredicates.ALWAYS_TRUE_BLOCK_STATE, name);
            }

            ResourceLocation tagLocation = ResourceLocation.tryParse(blockArg);

            assert tagLocation != null;
            TagKey<Block> tag = TagKey.create(ForgeRegistries.Keys.BLOCKS, tagLocation);
            predicate = state -> state.is(tag);
        }
        return new BlockExperienceSource(skill, xp, predicate, name);
    }

    public static void handleBreak(@Nullable ServerPlayer player, BlockState state, int multiplier) {

        if (player == null) return;

        for (BlockExperienceSource xpSource : ExperienceSource.filterBy(BlockExperienceSource.class)) {

            if (xpSource.predicate.test(state) && !player.isSpectator() && !player.isCreative()) { xpSource.addXP(player, multiplier); }
        }
    }

    public static void handlePlace(@Nullable ServerPlayer player, BlockState state) {

        if (player == null) return;

        for (BlockExperienceSource xpSource : ExperienceSource.filterBy(BlockExperienceSource.class)) {

            if (xpSource.predicate.test(state) && !player.isSpectator() && !player.isCreative()) { xpSource.setDebt(player, xpSource.getDebt(player) + 1); }
        }
    }

    /**
     * Utility method to check block states.
     */
    static boolean isSameState(BlockState first, BlockState second) {

        boolean blockCheck = first.getBlock() == second.getBlock();
        if (!blockCheck) return false;

        for (Property<?> property : first.getProperties()) {

            if (second.hasProperty(property)) {

                if (first.getValue(property).equals(second.getValue(property))) continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public int getXP() { return xp; }

    @Override
    public Skill getSkill() { return skill; }

    public int getDebt(ServerPlayer player) { return xpDebtMap.getOrDefault(player, parseData(player)); }

    public void setDebt(ServerPlayer player, int debt) {

        xpDebtMap.put(player, debt);
        ExperienceSourceData.of(player.getServer(), this).update(player, debt);
    }

    public void addXP(ServerPlayer player, int multiplier) {

        if (getDebt(player) == 0) {

            applyTo(player, multiplier);

        } else if (getDebt(player) < multiplier) {

            int debt = getDebt(player);
            setDebt(player, 0);

            int newMultiplier = multiplier - debt;
            applyTo(player, newMultiplier);

        } else setDebt(player, getDebt(player) - multiplier);
    }

    @Override
    public Integer parseData(ServerPlayer player) {

        JsonObject jsonFile = ExperienceSourceData.jsonFile(player.getServer());
        String serialized = this.serializedName();
        String uuid = player.getStringUUID();

        if (!jsonFile.has(serialized)) return 0;

        JsonObject jsonObject = GsonHelper.getAsJsonObject(jsonFile, serialized);

        if (!jsonObject.has(uuid)) return 0;
        return GsonHelper.getAsInt(jsonObject, uuid);
    }

    @Override
    public Pair<String, JsonObject> serialize() {

        JsonObject playerMap = new JsonObject();

        for (ServerPlayer player : xpDebtMap.keySet()) { playerMap.add(player.getStringUUID(), new JsonPrimitive(getDebt(player))); }
        return Pair.of(serializedName(), playerMap);
    }

    @Override
    public String serializedName() { return name; }

    @Override
    public Map<ServerPlayer, Integer> getPlayerMap() { return xpDebtMap; }

}
