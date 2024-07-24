package github.poscard8.poscardsskills.item;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.component.ColorPalette;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;

public class BrilliantFertilizerItem extends ItemWithDescription {

    public BrilliantFertilizerItem(Properties property) { super(property); }

    @Override
    protected Collection<Component> getDescriptionComponents() {

        String name = getDescription().getString();

        Component description = Component.translatable("tooltip.poscardsskills.brilliant_fertilizer_desc", name)
                .withStyle(PoscardsSkills.getComponentHandler().getColorPalette().colorOf(ColorPalette.Key.DESCRIPTION));
        return PSComponents.split(description);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {

        if (ctx.getPlayer() == null) return InteractionResult.PASS;

        Level level = ctx.getLevel();
        BlockPos position = ctx.getClickedPos();
        BlockPos position2 = position.relative(ctx.getClickedFace());

        InteractionHand hand = ctx.getHand();
        ItemStack copy = ctx.getItemInHand().copy();

        int count = copy.getCount();
        int cooldown = Math.round((float) (40 / Math.sqrt(count)));

        if (BoneMealItem.applyBonemeal(ctx.getItemInHand(), level, position, ctx.getPlayer())) {

            if (!level.isClientSide) level.levelEvent(1505, position, 0);

            ctx.getPlayer().setItemInHand(hand, copy);
            ctx.getPlayer().getCooldowns().addCooldown(this, cooldown);
            return InteractionResult.sidedSuccess(level.isClientSide);

        } else {

            BlockState state = level.getBlockState(position);
            boolean flag = state.isFaceSturdy(level, position, ctx.getClickedFace());

            if (flag && BoneMealItem.growWaterPlant(ctx.getItemInHand(), level, position2, ctx.getClickedFace())) {

                if (!level.isClientSide) level.levelEvent(1505, position2, 0);

                ctx.getPlayer().setItemInHand(hand, copy);
                ctx.getPlayer().getCooldowns().addCooldown(this, cooldown);
                return InteractionResult.sidedSuccess(level.isClientSide);

            } else return InteractionResult.PASS;
        }
    }
}
