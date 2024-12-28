package github.poscard8.poscardsskills.enchantment;

import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.util.PSUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * My attempt at a balanced vein-miner enchantment.
 * Random adjacent blocks are broken if they are the same block.
 * <p>Can be disabled via crouching.
 */
public class TrueEfficiencyEnchantment extends PSEnchantment {

    protected static final Random RANDOM = new Random();

    public TrueEfficiencyEnchantment(EnchantmentCategory category, EquipmentSlot[] slots) { super(category, slots); }

    public static int breakAdjacentBlocks(Player player, BlockPos brokenPosition, BlockState brokenState) {

        int count = 1;
        if (!(player.isSpectator() || player.isCreative() || player.isCrouching())) {

            ItemStack stack = player.getMainHandItem();
            double value = PSUtils.getTrueEfficiencyValue(player);
            if (value > 0) {

                Level level = player.level();
                List<BlockPos> adjacentPositions = pickAdjacentPositions(level, brokenPosition, brokenState);

                while (!adjacentPositions.isEmpty() && value > 0) {

                    int adjacentCount = adjacentPositions.size();
                    int randomIndex = RANDOM.nextInt(adjacentCount);
                    BlockPos position = adjacentPositions.get(randomIndex);

                    float randomFloat = RANDOM.nextFloat();
                    if (randomFloat < value) {

                        BlockState state = level.getBlockState(position);
                        Block block = state.getBlock();
                        @Nullable BlockEntity blockEntity = level.getBlockEntity(position);
                        FluidState fluidState = level.getFluidState(position);

                        state.onDestroyedByPlayer(level, position, player, false, fluidState);

                        player.awardStat(Stats.BLOCK_MINED.get(block));
                        Block.dropResources(state, level, brokenPosition, blockEntity, player, stack, true);

                        ClientLevel clientLevel = Minecraft.getInstance().level;
                        if (clientLevel != null) clientLevel.addDestroyBlockEffect(position, state);

                        adjacentPositions.remove(position);
                        count++;
                    }

                    value--;
                }
            }
        }

        return count;
    }

    protected static ArrayList<BlockPos> pickAdjacentPositions(Level level, BlockPos brokenPosition, BlockState brokenState) {

        ArrayList<BlockPos> positions = new ArrayList<>();
        Block block = brokenState.getBlock();

        if (brokenState.isCollisionShapeFullBlock(level, brokenPosition)) {

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -1; k <= 1; k++) {

                        positions.add(brokenPosition.offset(i, j, k));
                    }
                }
            }

            positions.remove(13);
            positions.removeIf(position -> !level.getBlockState(position).is(block));
        }

        return positions;
    }

    @Override
    protected ForgeConfigSpec.DoubleValue[] configs() {

        return new ForgeConfigSpec.DoubleValue[]{

                PoscardsSkillsCommonConfig.TRUE_EFFICIENCY_1_BREAK_VALUE,
                PoscardsSkillsCommonConfig.TRUE_EFFICIENCY_2_BREAK_VALUE,
                PoscardsSkillsCommonConfig.TRUE_EFFICIENCY_3_BREAK_VALUE,
        };
    }

}
