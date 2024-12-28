package github.poscard8.poscardsskills.experiencesource;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

/**
 * Utility class.
 */
public class ExperienceSourcePredicates {

    public static final Predicate<BlockState> ALWAYS_TRUE_BLOCK_STATE = state -> true;
    public static final Predicate<ItemStack> ALWAYS_TRUE_ITEM_STACK = stack -> true;
    public static final Predicate<Entity> ALWAYS_TRUE_ENTITY = entity -> true;
    public static final Predicate<ResourceLocation> ALWAYS_TRUE_RESOURCE_LOCATION = location -> true;

}
