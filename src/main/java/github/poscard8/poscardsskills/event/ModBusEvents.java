package github.poscard8.poscardsskills.event;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.block.ShiftedTextureBlock;
import github.poscard8.poscardsskills.client.layer.AnimatedArmorLayer;
import github.poscard8.poscardsskills.datagen.PSBlockStateProvider;
import github.poscard8.poscardsskills.datagen.PSItemModelProvider;
import github.poscard8.poscardsskills.datagen.PSLootTableProvider;
import github.poscard8.poscardsskills.mixin.accessor.ModelBakeryAccessor;
import github.poscard8.poscardsskills.module.BaseModule;
import github.poscard8.poscardsskills.ui.screen.PoscardsSkillsScreen;
import github.poscard8.poscardsskills.ui.screen.SkillCraftingScreen;
import github.poscard8.poscardsskills.ui.screen.SkillScreen;
import github.poscard8.poscardsskills.util.model.ShiftedTextureModel;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PoscardsSkills.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBusEvents {

    private ModBusEvents() {}

    @SubscribeEvent
    static void generateData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new PSBlockStateProvider(generator, fileHelper));
        generator.addProvider(true, new PSItemModelProvider(generator, fileHelper));
        generator.addProvider(true, new PSLootTableProvider(generator));
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

        MenuScreens.register(BaseModule.MenuTypes.MAIN.get(), PoscardsSkillsScreen::new);
        MenuScreens.register(BaseModule.MenuTypes.SKILL.get(), SkillScreen::new);
        MenuScreens.register(BaseModule.MenuTypes.SKILL_CRAFTING.get(), SkillCraftingScreen::new);

        ModelBakeryAccessor.getUnreferencedTextures().add(AnimatedArmorLayer.BRILLIANT_OUTER);
        ModelBakeryAccessor.getUnreferencedTextures().add(AnimatedArmorLayer.BRILLIANT_INNER);
    }

    @SubscribeEvent
    static void addKeyMappings(RegisterKeyMappingsEvent event) {

        event.register(PoscardsSkills.KEY_SKILL_MENU);
    }

    @SubscribeEvent
    static void onAttributeModification(EntityAttributeModificationEvent event) {

        event.add(EntityType.PLAYER, BaseModule.Attributes.CHEST_LUCK.get());
        event.add(EntityType.PLAYER, BaseModule.Attributes.WISDOM.get());
    }

    @SubscribeEvent
    static void onModelBake(ModelEvent.BakingCompleted event) {

        for (ResourceLocation location : event.getModels().keySet()) {

            String blockString = location.toString().split("#")[0];

            Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(blockString));

            if (block instanceof ShiftedTextureBlock) {

                BakedModel baked = event.getModels().get(location);
                event.getModels().put(location, new ShiftedTextureModel(baked));
            }
        }
    }

}
