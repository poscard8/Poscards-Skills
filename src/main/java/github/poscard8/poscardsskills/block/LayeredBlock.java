package github.poscard8.poscardsskills.block;

import github.poscard8.poscardsskills.util.model.BakedQuadHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static github.poscard8.poscardsskills.util.model.BakedQuadHelper.copy;
import static github.poscard8.poscardsskills.util.model.BakedQuadHelper.crop;

/**
 * Block with horizontally connected texture. See {@link ShiftedTextureBlock} and {@link BakedQuadHelper} for more info.
 */
public class LayeredBlock extends Block implements ShiftedTextureBlock {

    public static final ModelProperty<Boolean> NORTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH = new ModelProperty<>();
    public static final ModelProperty<Boolean> EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> NORTH_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> NORTH_WEST = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH_EAST = new ModelProperty<>();
    public static final ModelProperty<Boolean> SOUTH_WEST = new ModelProperty<>();

    protected static Map<ModelProperty<Boolean>, ModelProperty<Boolean>> MIRROR_MAP = Map.of(

            NORTH, SOUTH, NORTH_WEST, SOUTH_WEST, NORTH_EAST, SOUTH_EAST,
            SOUTH, NORTH, SOUTH_WEST, NORTH_WEST, SOUTH_EAST, NORTH_EAST
    );

    public LayeredBlock(Properties property) { super(property); }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, ModelData data, List<BakedQuad> original) {

        if (side == null) return original;
        List<BakedQuad> quads = new ArrayList<>();

        if (side.getAxis().isVertical()) {

            for (BakedQuad quad : original) {

                BakedQuad copy = copy(quad);
                BakedQuad leftQuad;
                BakedQuad middleQuad;
                BakedQuad rightQuad;

                ResourceLocation textureLoc = quad.getSprite().getName();
                String name = textureLoc.getPath();

                if (name.contains("_inner")) {

                    float v0 = has(data, quad, NORTH_WEST) ? 0 : 5;
                    float v1 = has(data, quad, SOUTH_WEST) ? 16 : 11;
                    leftQuad = BakedQuadHelper.crop(copy, 0, v0, 5, v1, Direction.Axis.Z);

                    v0 = has(data, quad, NORTH) ? 0 : 5;
                    v1 = has(data, quad, SOUTH) ? 16 : 11;
                    middleQuad = BakedQuadHelper.crop(copy, 5, v0, 11, v1, Direction.Axis.Z);

                    v0 = has(data, quad, NORTH_EAST) ? 0 : 5;
                    v1 = has(data, quad, SOUTH_EAST) ? 16 : 11;
                    rightQuad = BakedQuadHelper.crop(copy, 11, v0, 16, v1, Direction.Axis.Z);

                    if (has(data, quad, WEST)) quads.add(leftQuad);
                    if (has(data, quad, EAST)) quads.add(rightQuad);
                    quads.add(middleQuad);

                } else if (name.contains("_middle")) {

                    float u0 = has(data, quad, WEST) ? 0 : 4;
                    leftQuad = BakedQuadHelper.crop(copy, u0, 4, 5, 5, Direction.Axis.Z);
                    BakedQuad leftQuad2 = BakedQuadHelper.crop(copy, 4, 5, 5, 11, Direction.Axis.Z);
                    BakedQuad leftQuad3 = BakedQuadHelper.crop(copy, u0, 11, 5, 12, Direction.Axis.Z);

                    middleQuad = BakedQuadHelper.crop(copy, 5, 4, 11, 5, Direction.Axis.Z);
                    BakedQuad middleQuad2 = BakedQuadHelper.crop(copy, 5, 11, 11, 12, Direction.Axis.Z);

                    float u1 = has(data, quad, EAST) ? 16 : 12;
                    rightQuad = BakedQuadHelper.crop(copy, 11, 4, u1, 5, Direction.Axis.Z);
                    BakedQuad rightQuad2 = BakedQuadHelper.crop(copy, 11, 5, 12, 11, Direction.Axis.Z);
                    BakedQuad rightQuad3 = BakedQuadHelper.crop(copy, 11, 11, u1, 12, Direction.Axis.Z);

                    BakedQuad topQuad = BakedQuadHelper.crop(copy, 4, 0, 5, 4, Direction.Axis.Z);
                    BakedQuad topQuad2 = BakedQuadHelper.crop(copy, 11, 0, 12, 4, Direction.Axis.Z);

                    BakedQuad bottomQuad = BakedQuadHelper.crop(copy, 4, 12, 5, 16, Direction.Axis.Z);
                    BakedQuad bottomQuad2 = BakedQuadHelper.crop(copy, 11, 12, 12, 16, Direction.Axis.Z);

                    if (!has(data, quad, NORTH_WEST)) quads.add(leftQuad);
                    if (!has(data, quad, WEST)) quads.add(leftQuad2);
                    if (!has(data, quad, SOUTH_WEST)) quads.add(leftQuad3);
                    if (!has(data, quad, NORTH)) quads.add(middleQuad);
                    if (!has(data, quad, SOUTH)) quads.add(middleQuad2);
                    if (!has(data, quad, NORTH_EAST)) quads.add(rightQuad);
                    if (!has(data, quad, EAST)) quads.add(rightQuad2);
                    if (!has(data, quad, SOUTH_EAST)) quads.add(rightQuad3);

                    if (has(data, quad, NORTH) && !has(data, quad, NORTH_WEST)) quads.add(topQuad);
                    if (has(data, quad, NORTH) && !has(data, quad, NORTH_EAST)) quads.add(topQuad2);
                    if (has(data, quad, SOUTH) && !has(data, quad, SOUTH_WEST)) quads.add(bottomQuad);
                    if (has(data, quad, SOUTH) && !has(data, quad, SOUTH_EAST)) quads.add(bottomQuad2);

                } else {

                    leftQuad = BakedQuadHelper.crop(copy, 0, 0, 4, 4, Direction.Axis.Z);
                    BakedQuad leftQuad2 = BakedQuadHelper.crop(copy, 0, 4, 4, 12, Direction.Axis.Z);
                    BakedQuad leftQuad3 = BakedQuadHelper.crop(copy, 0, 12, 4, 16, Direction.Axis.Z);

                    middleQuad = BakedQuadHelper.crop(copy, 4, 0, 12, 4, Direction.Axis.Z);
                    BakedQuad middleQuad2 = BakedQuadHelper.crop(copy, 4, 12, 12, 16, Direction.Axis.Z);

                    rightQuad = BakedQuadHelper.crop(copy, 12, 0, 16, 4, Direction.Axis.Z);
                    BakedQuad rightQuad2 = BakedQuadHelper.crop(copy, 12, 4, 16, 12, Direction.Axis.Z);
                    BakedQuad rightQuad3 = BakedQuadHelper.crop(copy, 12, 12, 16, 16, Direction.Axis.Z);

                    if (!has(data, quad, NORTH_WEST)) quads.add(leftQuad);
                    if (!has(data, quad, WEST)) quads.add(leftQuad2);
                    if (!has(data, quad, SOUTH_WEST)) quads.add(leftQuad3);
                    if (!has(data, quad, NORTH)) quads.add(middleQuad);
                    if (!has(data, quad, SOUTH)) quads.add(middleQuad2);
                    if (!has(data, quad, NORTH_EAST)) quads.add(rightQuad);
                    if (!has(data, quad, EAST)) quads.add(rightQuad2);
                    if (!has(data, quad, SOUTH_EAST)) quads.add(rightQuad3);
                }
            }
        } else {

            for (BakedQuad quad : original) {

                BakedQuad copy = copy(quad);
                BakedQuad newQuad;

                ResourceLocation textureLoc = quad.getSprite().getName();
                String name = textureLoc.getPath();

                if (hasLeft(data, quad) && hasRight(data, quad)) {

                    if (name.contains("_side")) quads.add(quad);

                } else if (hasLeft(data, quad) && !hasRight(data, quad)) {

                    int u0 = name.contains("_side") ? 0 : 2;
                    int u1 = name.contains("_side") ? 2 : 16;

                    newQuad = crop(copy, u0, 0, u1, 16);
                    quads.add(newQuad);

                } else if (!hasLeft(data, quad) && hasRight(data, quad)) {

                    int u0 = name.contains("_side") ? 2 : 0;
                    int u1 = name.contains("_side") ? 16 : 2;

                    newQuad = crop(copy, u0, 0, u1, 16);
                    quads.add(newQuad);

                } else if (!hasLeft(data, quad) && !hasRight(data, quad)) {

                    if (!name.contains("_side")) quads.add(quad);
                }
            }
        }
        return quads;
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos position, BlockState state) {

        ModelData.Builder builder = ModelData.builder();

        builder
                .with(NORTH, checkBlocks(level, position.north()))
                .with(SOUTH, checkBlocks(level, position.south()))
                .with(EAST, checkBlocks(level, position.east()))
                .with(WEST, checkBlocks(level, position.west()))
                .with(NORTH_EAST, checkBlocks(level, position.north(), position.north().east(), position.east()))
                .with(NORTH_WEST, checkBlocks(level, position.north(), position.north().west(), position.west()))
                .with(SOUTH_EAST, checkBlocks(level, position.south(), position.south().east(), position.east()))
                .with(SOUTH_WEST, checkBlocks(level, position.south(), position.south().west(), position.west()));

        return builder.build();
    }

    protected boolean checkBlocks(BlockAndTintGetter level, BlockPos... positions) {

        for (BlockPos position : positions) {

            if (!level.getBlockState(position).is(this)) return false;
        }
        return true;
    }

    protected boolean has(ModelData data, BakedQuad quad, ModelProperty<Boolean> property) {

        ModelProperty<Boolean> newProperty = quad.getDirection() == Direction.DOWN ? MIRROR_MAP.getOrDefault(property, property) : property;
        return Boolean.TRUE.equals(data.get(newProperty));
    }

    protected boolean hasLeft(ModelData data, BakedQuad quad) {

        Map<Direction, ModelProperty<Boolean>> map = Map.of(Direction.NORTH, EAST, Direction.SOUTH, WEST, Direction.EAST, SOUTH, Direction.WEST, NORTH);

        if (!data.has(map.get(quad.getDirection()))) return false;
        return Boolean.TRUE.equals(data.get(map.get(quad.getDirection())));
    }

    protected boolean hasRight(ModelData data, BakedQuad quad) {

        Map<Direction, ModelProperty<Boolean>> map = Map.of(Direction.NORTH, WEST, Direction.SOUTH, EAST, Direction.EAST, NORTH, Direction.WEST, SOUTH);

        if (!data.has(map.get(quad.getDirection()))) return false;
        return Boolean.TRUE.equals(data.get(map.get(quad.getDirection())));
    }

}
