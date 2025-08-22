package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.block.BlockWrapper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class PSCreativeModeTabs
{
    public static final DeferredRegister<CreativeModeTab> ALL = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PoscardsSkills.ID);

    public static final RegistryObject<CreativeModeTab> MAIN = ALL.register("main", () -> CreativeModeTab.builder()

            .title(Component.translatable("generic.poscardsskills.name"))
            .icon(PSItems.BRILLIANT_GEMSTONE.get()::getDefaultInstance)
            .displayItems(((params, output) ->
            {
                BlockWrapper.forEach(wrapper -> accept(output, wrapper));
                PSItems.ALL.getEntries().forEach(object -> accept(output, object));
            }))
            .build());

    static void accept(CreativeModeTab.Output output, ItemLike itemLike) { output.accept(itemLike); }

    static void accept(CreativeModeTab.Output output, Supplier<Item> itemGetter) { output.accept(itemGetter.get()); }

    public static void register(IEventBus bus) { ALL.register(bus); }

}
