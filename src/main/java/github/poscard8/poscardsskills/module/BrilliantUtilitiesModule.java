package github.poscard8.poscardsskills.module;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.item.BrilliantBookItem;
import github.poscard8.poscardsskills.item.BrilliantFertilizerItem;
import github.poscard8.poscardsskills.item.BrilliantKeyItem;
import github.poscard8.poscardsskills.item.BrilliantPearlItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public final class BrilliantUtilitiesModule extends Module {

    public static final String CONFIG_KEY = "brilliantUtilitiesModule";


    BrilliantUtilitiesModule() { super(CONFIG_KEY); }

    @Override
    protected void whenPresent(IEventBus bus) {

        Items.ALL.register(bus);
        SoundEvents.ALL.register(bus);
    }

    @Override
    protected void whenAbsent(IEventBus bus) {

        clearAllEntries(ForgeRegistries.ITEMS, Items.ALL);
        clearAllEntries(ForgeRegistries.SOUND_EVENTS, SoundEvents.ALL);
    }


    public static class Items {

        static final DeferredRegister<Item> ALL = DeferredRegister.create(ForgeRegistries.ITEMS, PoscardsSkills.ID);

        public static final RegistryObject<Item>

                BRILLIANT_FERTILIZER = ALL.register("brilliant_fertilizer", () -> new BrilliantFertilizerItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).tab(PoscardsSkills.CREATIVE_TAB))),
                BRILLIANT_PEARL = ALL.register("brilliant_pearl", () -> new BrilliantPearlItem(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON).tab(PoscardsSkills.CREATIVE_TAB))),
                BRILLIANT_BOOK = ALL.register("brilliant_book", () -> new BrilliantBookItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).tab(PoscardsSkills.CREATIVE_TAB))),
                BRILLIANT_KEY = ALL.register("brilliant_key", () -> new BrilliantKeyItem(new Item.Properties().rarity(Rarity.RARE).tab(PoscardsSkills.CREATIVE_TAB)));

    }

    public static class SoundEvents {

        static final DeferredRegister<SoundEvent> ALL = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PoscardsSkills.ID);

        public static final RegistryObject<SoundEvent> KEY_USE = ALL.register("key_use", () -> new SoundEvent(PoscardsSkills.asResource("key_use")));

    }

}
