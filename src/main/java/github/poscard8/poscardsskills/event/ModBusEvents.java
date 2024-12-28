package github.poscard8.poscardsskills.event;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.block.ShiftedTextureBlock;
import github.poscard8.poscardsskills.datagen.PSBlockStateProvider;
import github.poscard8.poscardsskills.datagen.PSItemModelProvider;
import github.poscard8.poscardsskills.datagen.PSLootTableProvider;
import github.poscard8.poscardsskills.particle.PoscardsSkillsParticle;
import github.poscard8.poscardsskills.registry.PSAttributes;
import github.poscard8.poscardsskills.registry.PSMenuTypes;
import github.poscard8.poscardsskills.registry.PSParticleTypes;
import github.poscard8.poscardsskills.ui.screen.PoscardsSkillsScreen;
import github.poscard8.poscardsskills.ui.screen.SkillCraftingScreen;
import github.poscard8.poscardsskills.ui.screen.SkillScreen;
import github.poscard8.poscardsskills.util.model.ShiftedTextureModel;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
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

    /**
     * Data generation.
     */
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

        MenuScreens.register(PSMenuTypes.MAIN.get(), PoscardsSkillsScreen::new);
        MenuScreens.register(PSMenuTypes.SKILL.get(), SkillScreen::new);
        MenuScreens.register(PSMenuTypes.SKILL_CRAFTING.get(), SkillCraftingScreen::new);
    }

    @SubscribeEvent
    static void addKeyMappings(RegisterKeyMappingsEvent event) {

        event.register(PoscardsSkills.KEY_POSCARDS_SKILLS_MENU);
    }

    @SubscribeEvent
    static void registerParticleProviders(RegisterParticleProvidersEvent event) {

        event.register(PSParticleTypes.LEVEL_UP.get(), PoscardsSkillsParticle.LevelUpProvider::new);
        event.register(PSParticleTypes.BRILLIANT.get(), PoscardsSkillsParticle.AscensionProvider::brilliant);
        event.register(PSParticleTypes.BLESSED.get(), PoscardsSkillsParticle.AscensionProvider::blessed);
        event.register(PSParticleTypes.DIVINE.get(), PoscardsSkillsParticle.AscensionProvider::divine);
    }

    @SubscribeEvent
    static void onAttributeModification(EntityAttributeModificationEvent event) {

        for (EntityType<? extends LivingEntity> entityType : event.getTypes()) event.add(entityType, PSAttributes.CRIT_DAMAGE.get());

        event.add(EntityType.PLAYER, PSAttributes.CHEST_LUCK.get());
        event.add(EntityType.PLAYER, PSAttributes.WISDOM.get());
        event.add(EntityType.PLAYER, PSAttributes.LEGACY.get());
    }

    /**
     * Replacing existing models of certain blocks with {@link ShiftedTextureModel}.
     */

    @SubscribeEvent
    static void onModelBake(ModelEvent.BakingCompleted event) {

        for (ResourceLocation location : event.getModels().keySet()) {

            String blockString = location.toString().split("#")[0];

            Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(blockString));

            if (block instanceof ShiftedTextureBlock) {

                event.getModels().compute(location, (k, baked) -> new ShiftedTextureModel(baked));
            }
        }
    }

}
