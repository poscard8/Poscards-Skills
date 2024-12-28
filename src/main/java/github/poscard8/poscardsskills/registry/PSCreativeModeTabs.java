package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class PSCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> ALL = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PoscardsSkills.ID);

    public static final RegistryObject<CreativeModeTab> MAIN = ALL.register("main", () -> CreativeModeTab.builder()

            .title(Component.translatable("generic.poscardsskills.name"))
            .icon(() -> PSItems.BRILLIANT_SHARD.get().getDefaultInstance())
            .displayItems(((params, output) -> {
                if (PoscardsSkillsCommonConfig.DECORATIVE_BLOCKS.get()) {

                    output.accept(PSBlocks.JADE_BRICKS);
                    output.accept(PSBlocks.JADE_BRICK_STAIRS);
                    output.accept(PSBlocks.JADE_BRICK_SLAB);
                    output.accept(PSBlocks.JADE_BRICK_WALL);
                    output.accept(PSBlocks.CHISELED_JADE);
                    output.accept(PSBlocks.JADE_PILLAR);
                    output.accept(PSBlocks.LAYERED_JADE);
                    output.accept(PSBlocks.SHINY_JADE);
                }

                output.accept(PSBlocks.ROUGH_JADE);

                if (PoscardsSkillsCommonConfig.DECORATIVE_BLOCKS.get()) {

                    output.accept(PSBlocks.JASPER_BRICKS);
                    output.accept(PSBlocks.JASPER_BRICK_STAIRS);
                    output.accept(PSBlocks.JASPER_BRICK_SLAB);
                    output.accept(PSBlocks.JASPER_BRICK_WALL);
                    output.accept(PSBlocks.CHISELED_JASPER);
                    output.accept(PSBlocks.JASPER_PILLAR);
                    output.accept(PSBlocks.LAYERED_JASPER);
                    output.accept(PSBlocks.SHINY_JASPER);
                }

                output.accept(PSBlocks.ROUGH_JASPER);

                if (PoscardsSkillsCommonConfig.DECORATIVE_BLOCKS.get()) {

                    output.accept(PSBlocks.MARBLE_BRICKS);
                    output.accept(PSBlocks.MARBLE_BRICK_STAIRS);
                    output.accept(PSBlocks.MARBLE_BRICK_SLAB);
                    output.accept(PSBlocks.MARBLE_BRICK_WALL);
                    output.accept(PSBlocks.CHISELED_MARBLE);
                    output.accept(PSBlocks.MARBLE_PILLAR);
                    output.accept(PSBlocks.LAYERED_MARBLE);
                    output.accept(PSBlocks.SHINY_MARBLE);
                }
                output.accept(PSBlocks.ROUGH_MARBLE);
                output.accept(PSItems.ELEGANT_RUNE.get());
                output.accept(PSItems.ETHEREAL_RUNE.get());
                output.accept(PSItems.CLASSICAL_RUNE.get());
                output.accept(PSItems.ELEGANT_ARMOR_TRIM_SMITHING_TEMPLATE.get());
                output.accept(PSItems.ETHEREAL_ARMOR_TRIM_SMITHING_TEMPLATE.get());
                output.accept(PSItems.CLASSICAL_ARMOR_TRIM_SMITHING_TEMPLATE.get());

                output.accept(PSItems.BRILLIANT_SHARD.get());

                if (PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get()) {

                    output.accept(PSItems.BLESSED_SHARD.get());
                    output.accept(PSItems.DIVINE_SHARD.get());
                }

                output.accept(PSItems.BRILLIANT_REPAIR_STONE.get());

                if (PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get()) {

                    output.accept(PSItems.BLESSED_REPAIR_STONE.get());
                    output.accept(PSItems.DIVINE_REPAIR_STONE.get());
                }

                output.accept(PSItems.BRILLIANT_CATALYST.get());

                if (PoscardsSkillsCommonConfig.EXTRA_PROGRESSION.get()) {

                    output.accept(PSItems.BLESSED_CATALYST.get());
                    output.accept(PSItems.DIVINE_CATALYST.get());
                }

            })).build());

    public static void register(IEventBus bus) { ALL.register(bus); }

}
