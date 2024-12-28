package github.poscard8.poscardsskills.util.model;

import github.poscard8.poscardsskills.block.ShiftedTextureBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Model with custom quads (that are edited in some blocks).
 */
@MethodsReturnNonnullByDefault
public class ShiftedTextureModel extends BakedModelWrapper<BakedModel> {

    public ShiftedTextureModel(BakedModel baked) { super(baked); }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {

        List<BakedQuad> quads = super.getQuads(state, side, rand, data, renderType);
        return state != null ? ((ShiftedTextureBlock) state.getBlock()).getQuads(state, side, data, quads) : quads;
    }

    @Override
    @ParametersAreNonnullByDefault
    public ModelData getModelData(BlockAndTintGetter level, BlockPos position, BlockState state, ModelData data) {

        return ((ShiftedTextureBlock) state.getBlock()).getModelData(level, position, state);
    }
}
