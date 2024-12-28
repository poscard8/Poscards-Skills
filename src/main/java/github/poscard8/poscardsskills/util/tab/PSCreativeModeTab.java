package github.poscard8.poscardsskills.util.tab;

import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.registry.PSBlocks;
import github.poscard8.poscardsskills.registry.PSItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
public class PSCreativeModeTab extends CreativeModeTab {

    public PSCreativeModeTab() { super(""); }

    @Override
    public Component getDisplayName() { return Component.translatable("generic.poscardsskills.name"); }

    @Override
    public ItemStack makeIcon() { return PSItems.BRILLIANT_SHARD.get().getDefaultInstance(); }

    @Override
    public void fillItemList(@NotNull NonNullList<ItemStack> stacks) {

        if (PoscardsSkillsCommonConfig.DECORATIVE_BLOCKS.get()) {

            stacks.add(PSBlocks.JADE_BRICKS.getItemStack());
            stacks.add(PSBlocks.JADE_BRICK_STAIRS.getItemStack());
            stacks.add(PSBlocks.JADE_BRICK_SLAB.getItemStack());
            stacks.add(PSBlocks.JADE_BRICK_WALL.getItemStack());
            stacks.add(PSBlocks.CHISELED_JADE.getItemStack());
            stacks.add(PSBlocks.JADE_PILLAR.getItemStack());
            stacks.add(PSBlocks.LAYERED_JADE.getItemStack());
            stacks.add(PSBlocks.SHINY_JADE.getItemStack());
        }

        stacks.add(PSBlocks.ROUGH_JADE.getItemStack());

        if (PoscardsSkillsCommonConfig.DECORATIVE_BLOCKS.get()) {

            stacks.add(PSBlocks.JASPER_BRICKS.getItemStack());
            stacks.add(PSBlocks.JASPER_BRICK_STAIRS.getItemStack());
            stacks.add(PSBlocks.JASPER_BRICK_SLAB.getItemStack());
            stacks.add(PSBlocks.JASPER_BRICK_WALL.getItemStack());
            stacks.add(PSBlocks.CHISELED_JASPER.getItemStack());
            stacks.add(PSBlocks.JASPER_PILLAR.getItemStack());
            stacks.add(PSBlocks.LAYERED_JASPER.getItemStack());
            stacks.add(PSBlocks.SHINY_JASPER.getItemStack());
        }

        stacks.add(PSBlocks.ROUGH_JASPER.getItemStack());

        if (PoscardsSkillsCommonConfig.DECORATIVE_BLOCKS.get()) {

            stacks.add(PSBlocks.MARBLE_BRICKS.getItemStack());
            stacks.add(PSBlocks.MARBLE_BRICK_STAIRS.getItemStack());
            stacks.add(PSBlocks.MARBLE_BRICK_SLAB.getItemStack());
            stacks.add(PSBlocks.MARBLE_BRICK_WALL.getItemStack());
            stacks.add(PSBlocks.CHISELED_MARBLE.getItemStack());
            stacks.add(PSBlocks.MARBLE_PILLAR.getItemStack());
            stacks.add(PSBlocks.LAYERED_MARBLE.getItemStack());
            stacks.add(PSBlocks.SHINY_MARBLE.getItemStack());
        }
        stacks.add(PSBlocks.ROUGH_MARBLE.getItemStack());

        stacks.add(PSItems.ELEGANT_RUNE.get().getDefaultInstance());
        stacks.add(PSItems.ETHEREAL_RUNE.get().getDefaultInstance());
        stacks.add(PSItems.CLASSICAL_RUNE.get().getDefaultInstance());

        stacks.add(PSItems.BRILLIANT_SHARD.get().getDefaultInstance());

        if (PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get()) {

            stacks.add(PSItems.BLESSED_SHARD.get().getDefaultInstance());
            stacks.add(PSItems.DIVINE_SHARD.get().getDefaultInstance());
        }

        stacks.add(PSItems.BRILLIANT_REPAIR_STONE.get().getDefaultInstance());

        if (PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get()) {

            stacks.add(PSItems.BLESSED_REPAIR_STONE.get().getDefaultInstance());
            stacks.add(PSItems.DIVINE_REPAIR_STONE.get().getDefaultInstance());
        }

        stacks.add(PSItems.BRILLIANT_CATALYST.get().getDefaultInstance());

        if (PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get()) {

            stacks.add(PSItems.BLESSED_CATALYST.get().getDefaultInstance());
            stacks.add(PSItems.DIVINE_CATALYST.get().getDefaultInstance());
        }
    }


}
