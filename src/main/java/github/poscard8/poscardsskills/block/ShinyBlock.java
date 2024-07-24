package github.poscard8.poscardsskills.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class ShinyBlock extends Block {

    public static final IntegerProperty PHASE = IntegerProperty.create("phase", 0, 1);

    public ShinyBlock(Properties property) {

        super(property);
        this.registerDefaultState(this.defaultBlockState().setValue(PHASE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(PHASE); }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {

        int x = ctx.getClickedPos().getX() % 2;
        int z = ctx.getClickedPos().getZ() % 2;
        return super.getStateForPlacement(ctx) != null ? this.defaultBlockState().setValue(PHASE, (x + z) % 2) : null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos position) { return 0.3F; }

}
