package github.poscard8.poscardsskills.util.client.model;

import github.poscard8.poscardsskills.block.PillarBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static github.poscard8.poscardsskills.util.client.model.BakedQuadHelper.copy;
import static github.poscard8.poscardsskills.util.client.model.BakedQuadHelper.cropPillar;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
public class PillarBlockModel extends ShiftedTextureModel
{
    public static final ModelProperty<Boolean> TOP = new ModelProperty<>();
    public static final ModelProperty<Boolean> BOTTOM = new ModelProperty<>();

    public PillarBlockModel(BakedModel baked) { super(baked); }

    public static boolean hasTop(ModelData data) { return Boolean.TRUE.equals(data.get(TOP)); }

    public static boolean hasBottom(ModelData data) { return Boolean.TRUE.equals(data.get(BOTTOM)); }

    @Override
    protected List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, ModelData data, List<BakedQuad> original)
    {
        if (state == null || side == null) return original;
        List<BakedQuad> quads = new ArrayList<>();

        if (side.getAxis() == state.getValue(PillarBlock.FACING).getAxis()) { return original; } else
        {
            for (BakedQuad quad : original)
            {
                BakedQuad copy = copy(quad);

                TextureAtlasSprite sprite = quad.getSprite();
                ResourceLocation textureLocation = sprite.contents().name();
                String name = textureLocation.getPath();

                if (hasTop(data) && hasBottom(data))
                {
                    if (!name.contains("_ends")) quads.add(copy);
                }
                else if (!hasTop(data) && !hasBottom(data))
                {
                    if (name.contains("_ends")) quads.add(copy);
                }
                else
                {
                    BakedQuad newQuad = cropPillar(copy, state, data);
                    quads.add(newQuad);
                }
            }
        }
        return quads;
    }

    @Override
    protected ModelData getModelData(BlockAndTintGetter level, BlockPos position, BlockState state)
    {
        return ModelData.builder()
                .with(TOP, checkBlock(level, position.relative(state.getValue(PillarBlock.FACING), 1), state))
                .with(BOTTOM, checkBlock(level, position.relative(state.getValue(PillarBlock.FACING), -1), state))
                .build();
    }

    protected boolean checkBlock(BlockAndTintGetter level, BlockPos position, BlockState state)
    {
        return level.getBlockState(position).is(state.getBlock()) &&
                level.getBlockState(position).getValue(PillarBlock.FACING) == state.getValue(PillarBlock.FACING);
    }


}
