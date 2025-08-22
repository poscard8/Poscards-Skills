package github.poscard8.poscardsskills.util.client.model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import github.poscard8.poscardsskills.block.PillarBlock;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

import java.util.Arrays;

/**
 * Utility class.
 */
@OnlyIn(Dist.CLIENT)
public class BakedQuadHelper
{
    public static final int VERTEX_STRIDE = DefaultVertexFormat.BLOCK.getIntegerSize();

    public static BakedQuad copy(BakedQuad quad)
    {
        return new BakedQuad(Arrays.copyOf(quad.getVertices(), quad.getVertices().length), quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade());
    }

    public static BakedQuad copy(BakedQuad quad, int[] vertices)
    {
        return new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade());
    }

    public static Vec3 getXYZ(int[] vertices, int vertex)
    {
        float x = Float.intBitsToFloat(vertices[vertex * VERTEX_STRIDE]);
        float y = Float.intBitsToFloat(vertices[vertex * VERTEX_STRIDE + 1]);
        float z = Float.intBitsToFloat(vertices[vertex * VERTEX_STRIDE + 2]);
        return new Vec3(x, y, z);
    }

    public static void setXYZ(int[] vertices, int vertex, Vec3 xyz)
    {
        vertices[vertex * VERTEX_STRIDE] = Float.floatToRawIntBits((float) xyz.x);
        vertices[vertex * VERTEX_STRIDE + 1] = Float.floatToRawIntBits((float) xyz.y);
        vertices[vertex * VERTEX_STRIDE + 2] = Float.floatToRawIntBits((float) xyz.z);
    }

    public static void moveXYZ(int[] vertices, int vertex, Direction direction, double delta)
    {
        double x = direction.getAxis() == Direction.Axis.X ? direction == Direction.EAST ? delta : -delta : 0;
        double y = direction.getAxis() == Direction.Axis.Y ? direction == Direction.UP ? delta : -delta : 0;
        double z = direction.getAxis() == Direction.Axis.Z ? direction == Direction.SOUTH ? delta : -delta : 0;

        moveXYZ(vertices, vertex, x, y, z);
    }

    public static void moveXYZ(int[] vertices, int vertex, double x, double y , double z) { setXYZ(vertices, vertex, getXYZ(vertices, vertex).add(x, y, z)); }

    public static float getU(int[] vertices, int vertex) { return Float.intBitsToFloat(vertices[vertex * VERTEX_STRIDE + 4]); }

    public static void setU(int[] vertices, int vertex, float u) { vertices[vertex * VERTEX_STRIDE + 4] = Float.floatToRawIntBits(u); }

    public static void moveU(int[] vertices, int vertex, float u) { setU(vertices, vertex, getU(vertices, vertex) + u); }

    public static float getV(int[] vertices, int vertex) { return Float.intBitsToFloat(vertices[vertex * VERTEX_STRIDE + 5]); }

    public static void setV(int[] vertices, int vertex, float v) { vertices[vertex * VERTEX_STRIDE + 5] = Float.floatToRawIntBits(v); }

    public static void moveV(int[] vertices, int vertex, float v) { setV(vertices, vertex, getV(vertices, vertex) + v); }

    public static BakedQuad crop(BakedQuad quad, float u0, float v0, float u1, float v1) { return crop(quad, u0, v0, u1, v1, Direction.Axis.Y); }

    public static BakedQuad crop(BakedQuad quad, float u0, float v0, float u1, float v1, Direction.Axis axis)
    {
        Direction uDirection = quad.getDirection() == Direction.UP ? Direction.EAST : quad.getDirection().getCounterClockWise(axis);
        Direction vDirection = quad.getDirection() == Direction.UP ? Direction.SOUTH : quad.getDirection() == Direction.DOWN ? Direction.NORTH : Direction.DOWN;

        BakedQuad copy = copy(quad);
        int[] vertices = Arrays.copyOf(copy.getVertices(), 32);
        TextureAtlasSprite sprite = copy.getSprite();

        moveXYZ(vertices, 0, uDirection, u0 / 16);
        moveXYZ(vertices, 0, vDirection, v0 / 16);
        moveXYZ(vertices, 1, uDirection, u0 / 16);
        moveXYZ(vertices, 1, vDirection, (v1 - 16) / 16);
        moveXYZ(vertices, 2, uDirection, (u1 - 16) / 16);
        moveXYZ(vertices, 2, vDirection, (v1 - 16) / 16);
        moveXYZ(vertices, 3, uDirection, (u1 - 16) / 16);
        moveXYZ(vertices, 3, vDirection, v0 / 16);

        moveU(vertices, 0, (sprite.getU(u0) - sprite.getU0()));
        moveV(vertices, 0, (sprite.getV(v0) - sprite.getV0()));
        moveU(vertices, 1, (sprite.getU(u0) - sprite.getU0()));
        moveV(vertices, 1, (sprite.getV(v1) - sprite.getV1()));
        moveU(vertices, 2, (sprite.getU(u1) - sprite.getU1()));
        moveV(vertices, 2, (sprite.getV(v1) - sprite.getV1()));
        moveU(vertices, 3, (sprite.getU(u1) - sprite.getU1()));
        moveV(vertices, 3, (sprite.getV(v0) - sprite.getV0()));

        return copy(quad, vertices);
    }

    // this method is a total mess, I barely understand how models work :(
    @SuppressWarnings("ConstantValue")
    public static BakedQuad cropPillar(BakedQuad quad, BlockState state, ModelData data)
    {
        Direction direction = state.getValue(PillarBlock.FACING);
        boolean negativeDirection = direction == Direction.WEST || direction == Direction.DOWN || direction == Direction.NORTH;

        Direction.Axis axis = direction.getAxis();
        Direction uDirection = negativeDirection ? quad.getDirection().getCounterClockWise(axis) : quad.getDirection().getClockWise(axis);
        Direction vDirection = direction.getOpposite();

        BakedQuad copy = copy(quad);
        int[] vertices = Arrays.copyOf(copy.getVertices(), 32);
        TextureAtlasSprite sprite = copy.getSprite();
        String name = sprite.contents().name().getPath();

        int[] vertexOrder;

        float u0;
        float v0;
        float u1;
        float v1;

        switch (axis)
        {
            default -> vertexOrder = new int[]{0, 1, 2, 3};
            case X -> vertexOrder = new int[]{3, 0, 1, 2};
            case Z ->
            {
                switch (quad.getDirection())
                {
                    default -> vertexOrder = new int[]{2, 3, 0, 1};
                    case EAST, WEST -> vertexOrder = new int[]{1, 2, 3, 0};
                }
            }
        }
        u0 = 0;
        u1 = 16;
        v0 = name.contains("_ends") ? 8 : 0;

        if (PillarBlockModel.hasBottom(data)) v0 = 8 - v0;
        v1 = v0 + 8;

        boolean flip = false;

        flip = flip || (direction == Direction.SOUTH && (quad.getDirection() == Direction.DOWN || quad.getDirection() == Direction.WEST));
        flip = flip || (direction == Direction.NORTH && (quad.getDirection() == Direction.UP || quad.getDirection() == Direction.EAST));
        flip = flip || (direction == Direction.EAST && quad.getDirection() == Direction.NORTH);
        flip = flip || (direction == Direction.WEST && quad.getDirection() != Direction.NORTH);
        flip = flip || direction == Direction.DOWN;

        if (flip)
        {
            vDirection = vDirection.getOpposite();

            v0 = 8 - v0;
            v1 = v0 + 8;
        }

        moveXYZ(vertices, vertexOrder[0], uDirection, u0 / 16);
        moveXYZ(vertices, vertexOrder[0], vDirection, v0 / 16);
        moveXYZ(vertices, vertexOrder[1], uDirection, u0 / 16);
        moveXYZ(vertices, vertexOrder[1], vDirection, (v1 - 16) / 16);
        moveXYZ(vertices, vertexOrder[2], uDirection, (u1 - 16) / 16);
        moveXYZ(vertices, vertexOrder[2], vDirection, (v1 - 16) / 16);
        moveXYZ(vertices, vertexOrder[3], uDirection, (u1 - 16) / 16);
        moveXYZ(vertices, vertexOrder[3], vDirection, v0 / 16);

        if (flip)
        {
            v1 *= 3;

            if (v0 == 8)
            {
                v0 = -8;
                v1 = 16;
            }
        }

        moveU(vertices, vertexOrder[0], (sprite.getU(u0) - sprite.getU0()));
        moveV(vertices, vertexOrder[0], (sprite.getV(v0) - sprite.getV0()));
        moveU(vertices, vertexOrder[1], (sprite.getU(u0) - sprite.getU0()));
        moveV(vertices, vertexOrder[1], (sprite.getV(v1) - sprite.getV1()));
        moveU(vertices, vertexOrder[2], (sprite.getU(u1) - sprite.getU1()));
        moveV(vertices, vertexOrder[2], (sprite.getV(v1) - sprite.getV1()));
        moveU(vertices, vertexOrder[3], (sprite.getU(u1) - sprite.getU1()));
        moveV(vertices, vertexOrder[3], (sprite.getV(v0) - sprite.getV0()));

        return copy(quad, vertices);
    }

}
