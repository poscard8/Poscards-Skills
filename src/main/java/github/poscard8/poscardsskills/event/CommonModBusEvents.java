package github.poscard8.poscardsskills.event;

import github.poscard8.peritia.Peritia;
import github.poscard8.peritia.event.mod.AscensionSystemConfigureEvent;
import github.poscard8.peritia.reward.ItemReward;
import github.poscard8.peritia.skill.SkillAttributeInstance;
import github.poscard8.peritia.util.skill.AtFunction;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.datagen.PSBlockStateProvider;
import github.poscard8.poscardsskills.datagen.PSItemModelProvider;
import github.poscard8.poscardsskills.datagen.PSLootTableProvider;
import github.poscard8.poscardsskills.datagen.PSWorldGenProvider;
import github.poscard8.poscardsskills.registry.PSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PoscardsSkills.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModBusEvents
{
    /**
     * Data generation.
     */
    @SubscribeEvent
    static void generateData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> completableFuture = event.getLookupProvider();

        generator.addProvider(true, new PSBlockStateProvider(packOutput, fileHelper));
        generator.addProvider(true, new PSItemModelProvider(packOutput, fileHelper));
        generator.addProvider(true, new PSLootTableProvider(packOutput));
        generator.addProvider(true, new PSWorldGenProvider(packOutput, completableFuture));
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    static void registerTabContents(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
        {
            event.getEntries().putAfter(Items.NETHERITE_HOE.getDefaultInstance(), PSItems.VOLATILE_HOE.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(Items.NETHERITE_HOE.getDefaultInstance(), PSItems.VOLATILE_AXE.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(Items.NETHERITE_HOE.getDefaultInstance(), PSItems.VOLATILE_PICKAXE.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(Items.NETHERITE_HOE.getDefaultInstance(), PSItems.VOLATILE_SHOVEL.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        else if (event.getTabKey() == CreativeModeTabs.COMBAT)
        {
            event.getEntries().putAfter(Items.NETHERITE_SWORD.getDefaultInstance(), PSItems.VOLATILE_SWORD.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(Items.NETHERITE_AXE.getDefaultInstance(), PSItems.VOLATILE_AXE.get().getDefaultInstance(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    @SubscribeEvent
    static void configureAscension(AscensionSystemConfigureEvent event)
    {
        event.setIcon(PSItems.BRILLIANT_GEMSTONE.get());

        event.addReward(new ItemReward(new AtFunction.Range(1, 640), PSItems.BRILLIANT_GEMSTONE.get().getDefaultInstance()));
        event.addReward(new ItemReward(new AtFunction.Single(640), Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance()));

        event.addAttribute(new SkillAttributeInstance(Peritia.asResource("player.wisdom"), 1.0D, new AtFunction.Range(1, 100)));
        event.addAttribute(new SkillAttributeInstance(Peritia.asResource("player.chest_luck"), 0.1D, new AtFunction.Range(101, 500)));
        event.addAttribute(new SkillAttributeInstance(Peritia.asResource("player.extra_crit_chance"), 0.05D, new AtFunction.Range(501, 640)));
    }

}
