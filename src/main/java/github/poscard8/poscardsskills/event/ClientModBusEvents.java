package github.poscard8.poscardsskills.event;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.block.LayeredBlock;
import github.poscard8.poscardsskills.block.PillarBlock;
import github.poscard8.poscardsskills.util.client.model.LayeredBlockModel;
import github.poscard8.poscardsskills.util.client.model.PillarBlockModel;
import github.poscard8.poscardsskills.util.client.model.ShiftedTextureModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PoscardsSkills.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents
{
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event)
    {
        ShiftedTextureModel.registerForClass(PillarBlock.class, (key, baked) -> new PillarBlockModel(baked));
        ShiftedTextureModel.registerForClass(LayeredBlock.class, (key, baked) -> new LayeredBlockModel(baked));

        PoscardsSkills.LOGGER.info("Registered custom block models");
    }

    /**
     * Replacing existing models of certain blocks with {@link ShiftedTextureModel}.
     */
    @SubscribeEvent
    static void onModelBake(ModelEvent.ModifyBakingResult event)
    {
        Map<ResourceLocation, BakedModel> models = event.getModels();

        for (ResourceLocation location : models.keySet())
        {
            String blockString = location.toString().split("#")[0];
            Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(blockString));

            if (block != null && ShiftedTextureModel.hasCustomModel(block)) models.compute(location, (key, baked) -> ShiftedTextureModel.constructForBlock(block, key, baked));
        }
    }

}
