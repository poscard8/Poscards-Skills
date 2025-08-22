package github.poscard8.poscardsskills.util.client.model;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Model with custom quads (that are edited in some blocks).
 */
@MethodsReturnNonnullByDefault
@OnlyIn(Dist.CLIENT)
public abstract class ShiftedTextureModel extends BakedModelWrapper<BakedModel>
{
    protected static final Map<Class<? extends Block>, BiFunction<ResourceLocation, BakedModel, BakedModel>> CLASS_MAP = new HashMap<>();

    public ShiftedTextureModel(BakedModel baked) { super(baked); }

    public static <T extends Block> boolean hasCustomModel(T t) { return CLASS_MAP.containsKey(t.getClass()); }

    public static <T extends Block> BakedModel constructForBlock(T t, ResourceLocation key, BakedModel baked)
    {
        try
        {
            BiFunction<ResourceLocation, BakedModel, BakedModel> constructor = CLASS_MAP.get(t.getClass());
            return constructor.apply(key, baked);
        }
        catch (Exception exception) { return baked; }
    }

    public static <T extends Block> void registerForClass(Class<T> clazz, BiFunction<ResourceLocation, BakedModel, BakedModel> constructor)  { CLASS_MAP.put(clazz, constructor); }


    protected abstract List<BakedQuad> getQuads(BlockState state, Direction side, ModelData data, List<BakedQuad> original);

    protected abstract ModelData getModelData(BlockAndTintGetter level, BlockPos position, BlockState state);

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType)
    {
        List<BakedQuad> quads = super.getQuads(state, side, rand, data, renderType);

        try
        {
            return state != null ? getQuads(state, side, data, quads) : quads;
        }
        catch (Exception exception) { return quads; }
    }

    @Override
    @ParametersAreNonnullByDefault
    public ModelData getModelData(BlockAndTintGetter level, BlockPos position, BlockState state, ModelData data)
    {
        try
        {
            return getModelData(level, position, state);
        }
        catch (Exception exception) { return data; }
    }

}
