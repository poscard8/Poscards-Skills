package github.poscard8.poscardsskills.block;

import github.poscard8.poscardsskills.util.model.BakedQuadHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.ArrayList;
import java.util.List;

import static github.poscard8.poscardsskills.util.model.BakedQuadHelper.copy;
import static github.poscard8.poscardsskills.util.model.BakedQuadHelper.cropPillar;

/**
 * Pillar block with vertically connected texture. See {@link ShiftedTextureBlock} and {@link BakedQuadHelper} for more info.
 */
public class PillarBlock extends RotatedPillarBlock implements ShiftedTextureBlock {

    public static final ModelProperty<Boolean> TOP = new ModelProperty<>();
    public static final ModelProperty<Boolean> BOTTOM = new ModelProperty<>();

    public PillarBlock(Properties property) { super(property); }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, ModelData data, List<BakedQuad> original) {

        if (side == null) return original;
        List<BakedQuad> quads = new ArrayList<>();

        if (side.getAxis() == state.getValue(AXIS)) { return original; } else {

            for (BakedQuad quad : original) {

                BakedQuad copy = copy(quad);

                TextureAtlasSprite sprite = quad.getSprite();
                ResourceLocation textureLocation = sprite.contents().name();
                String name = textureLocation.getPath();

                if (Boolean.TRUE.equals(data.get(TOP)) && Boolean.TRUE.equals(data.get(BOTTOM))) {

                    if (!name.contains("_ends")) quads.add(copy);

                } else if (Boolean.FALSE.equals(data.get(TOP)) && Boolean.FALSE.equals(data.get(BOTTOM))) {

                    if (name.contains("_ends")) quads.add(copy);
                } else {

                    BakedQuad newQuad = cropPillar(copy, state, data);
                    quads.add(newQuad);
                }
            }
        }
        return quads;
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos position, BlockState state) {

        return ModelData.builder()
                .with(TOP, checkBlock(level, position.relative(state.getValue(AXIS), 1), state))
                .with(BOTTOM, checkBlock(level, position.relative(state.getValue(AXIS), -1), state))
                .build();
    }

    private boolean checkBlock(BlockAndTintGetter level, BlockPos position, BlockState state) {

        return level.getBlockState(position).is(state.getBlock()) && level.getBlockState(position).getValue(AXIS) == state.getValue(AXIS);
    }

}
