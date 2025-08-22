package github.poscard8.poscardsskills.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Ore clump shaped like an american football.
 * Blocks exposed to stone or liquids will have {@link DoubleOreConfiguration#inner} state,
 * non-exposed blocks will have {@link DoubleOreConfiguration#outer} state.
 */
@SuppressWarnings("unused")
public class DoubleOreFeature extends Feature<DoubleOreConfiguration>
{
    public static final Predicate<Float> OCCUPANCY_PREDICATE = f -> f >= 0.4F && f < 0.9F;
    protected static final Random RANDOM = new Random();

    public DoubleOreFeature(Codec<DoubleOreConfiguration> codec) { super(codec); }

    @Override
    public boolean place(FeaturePlaceContext<DoubleOreConfiguration> context)
    {
        List<BlockPos> positions = new ArrayList<>();

        Random random = new Random();
        RandomSource randomSource = context.random();
        BlockPos origin = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        DoubleOreConfiguration configuration = context.config();

        float length = ((float) configuration.size) / 4.0F;
        float multiplier = random.nextFloat(0.8F, 1.25F);
        float normalLength = length * multiplier;
        float radius = normalLength / 6.0F;
        int maxLength = normalLength > (int) normalLength ? (int) normalLength + 1 : (int) normalLength;

        double beta = random.nextDouble(Math.PI / 2.0F);
        double theta = random.nextDouble(Math.PI);

        float y = (float) (Math.sin(beta) * normalLength / 2.0F);

        float horizontalLength = (float) (Math.cos(beta) * normalLength / 2.0F);
        float x = (float) (Math.cos(theta) * horizontalLength);
        float z = (float) (Math.sin(theta) * horizontalLength);

        Vec3 A = new Vec3(origin.getX() - x, origin.getY() - y, origin.getZ() - z);
        Vec3 O = new Vec3(origin.getX(), origin.getY(), origin.getZ());
        Vec3 B = new Vec3(origin.getX() + x, origin.getY() + y, origin.getZ() + z);

        for (int i = -maxLength; i <= maxLength; i++)
        {
            for (int j = -maxLength; j <= maxLength; j++)
            {
                for (int k = -maxLength; k <= maxLength; k++)
                {
                    Vec3 vec3 = O.add(i, j, k);
                    BlockPos pos = origin.offset(i, j, k);
                    boolean inside = false;

                    if (vec3.distanceTo(O) < radius)
                    {
                        inside = true;
                    }
                    else
                    {
                        double distanceToA = vec3.distanceTo(A);
                        double distanceToB = vec3.distanceTo(B);

                        Vec3 difference = O.subtract(A);
                        Vec3 point;
                        Vec3 start = new Vec3(O.x, O.y, O.z);
                        Vec3 end;

                        if (distanceToA >= distanceToB)
                        {
                            end = B;
                            point = start.add(difference.normalize().multiply(radius, radius, radius));

                            for (int n = 0; n <= radius * 20.0F; n++)
                            {
                                if (vec3.distanceTo(point) * Math.sqrt(6) < point.distanceTo(end))
                                {
                                    inside = true;
                                    break;
                                }
                                point.add(difference.normalize().multiply(0.1D, 0.1D, 0.1D));
                            }
                        }
                        else
                        {
                            end = A;
                            point = start.add(difference.normalize().multiply(-radius, -radius, -radius));

                            for (int n = 0; n <= radius * 20.0F; n++)
                            {
                                if (vec3.distanceTo(point) * Math.sqrt(6) < point.distanceTo(end))
                                {
                                    inside = true;
                                    break;
                                }
                                point.add(difference.normalize().multiply(-0.1D, -0.1D, -0.1D));
                            }
                        }
                    }
                    if (inside) positions.add(pos);
                }
            }
        }

        boolean placed = place(configuration, worldgenlevel, randomSource, origin, positions);
        if (placed) modifyEdges(configuration, worldgenlevel, positions);
        return placed;
    }

    protected boolean place(DoubleOreConfiguration configuration, WorldGenLevel level, RandomSource randomSource, BlockPos origin, List<BlockPos> positions)
    {
        boolean placed = false;
        List<BlockState> states = positions.stream().map(level::getBlockState).toList();

        if (!OCCUPANCY_PREDICATE.test(getOccupancy(level, origin, positions))) return false; // don't place

        for (int i = 0; i < states.size(); i++)
        {
            BlockState state = states.get(i);
            BlockPos position = positions.get(i);

            if (!isAirOrLiquid(state) && configuration.inner.target.test(state, randomSource))
            {
                level.setBlock(position, configuration.inner.state, 2);
                placed = true;
            }
        }
        return placed;
    }

    protected void modifyEdges(DoubleOreConfiguration configuration, WorldGenLevel level, List<BlockPos> positions)
    {
        for (BlockPos position : positions)
        {
            if (isEdge(configuration, level, position) && RANDOM.nextFloat() < 0.75F)
            {
                level.setBlock(position, configuration.outer.state, 2);
            }
        }
    }

    protected boolean isEdge(DoubleOreConfiguration configuration, WorldGenLevel level, BlockPos position)
    {
        if (level.getBlockState(position).isAir()) return false;

        List<BlockPos> neighbors = new ArrayList<>();

        for (Direction direction : Direction.values()) neighbors.add(position.relative(direction));

        for (BlockPos pos : neighbors)
        {
            BlockState state = level.getBlockState(pos);
            if (!isFamiliarBlock(configuration, state)) return true;
        }
        return false;
    }

    protected float getOccupancy(WorldGenLevel level, BlockPos origin, List<BlockPos> positions)
    {
        float fullBlocks = 0;
        float allBlocks = 0;

        for (BlockPos position : positions)
        {
            double distance = Math.max(3, position.getCenter().distanceTo(origin.getCenter()));
            float weight = (float) (1 / distance);

            BlockState state = level.getBlockState(position);

            allBlocks += weight;
            if (!isAirOrLiquid(state)) fullBlocks += weight;
        }
        return fullBlocks / allBlocks;
    }

    protected boolean isAirOrLiquid(BlockState state) { return state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.LAVA); }

    protected boolean isFamiliarBlock(DoubleOreConfiguration configuration, BlockState state) { return isAirOrLiquid(state) || state.equals(configuration.inner.state) || state.equals(configuration.outer.state); }

}
