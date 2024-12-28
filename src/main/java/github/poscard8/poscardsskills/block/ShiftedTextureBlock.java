package github.poscard8.poscardsskills.block;

import github.poscard8.poscardsskills.event.ModBusEvents;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.List;

/**
 * Shifted textures are used for adding multiple models to blocks without adding more block states.
 * This interface is implemented by {@link LayeredBlock} and {@link PillarBlock}.
 * These are registered in {@link ModBusEvents}
 */
public interface ShiftedTextureBlock {

    List<BakedQuad> getQuads(BlockState state, Direction side, ModelData data, List<BakedQuad> original);

    ModelData getModelData(BlockAndTintGetter level, BlockPos position, BlockState state);

}
