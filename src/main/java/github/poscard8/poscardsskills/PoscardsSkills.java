package github.poscard8.poscardsskills;

import com.mojang.logging.LogUtils;
import github.poscard8.peritia.util.serialization.Loadable;
import github.poscard8.poscardsskills.config.PoscardsSkillsServerConfig;
import github.poscard8.poscardsskills.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@SuppressWarnings("unused")
@Mod(PoscardsSkills.ID)
public final class PoscardsSkills
{
    public static final String ID = "poscardsskills";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation asResource(String path) { return new ResourceLocation(ID, path); }

    public PoscardsSkills(FMLJavaModLoadingContext context)
    {
        ModContainer container = context.getContainer();
        container.addConfig(new ModConfig(ModConfig.Type.SERVER, PoscardsSkillsServerConfig.SPEC, container, "poscardsskills-server.toml"));

        IEventBus bus = context.getModEventBus();

        PSBannerPatterns.register(bus);
        PSBlocks.register(bus);
        PSCreativeModeTabs.register(bus);
        PSEnchantments.register(bus);
        PSFeatures.register(bus);
        PSItems.register(bus);
        PSRecipeSerializers.register(bus);
        PSSoundEvents.register(bus);

        Loadable.addDefaultNamespace(ID);
    }

}
