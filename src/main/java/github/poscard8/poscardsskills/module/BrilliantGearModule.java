package github.poscard8.poscardsskills.module;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.util.item.BrilliantGearUtils;
import github.poscard8.poscardsskills.util.item.PSArmorMaterials;
import github.poscard8.poscardsskills.util.item.PSTiers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public final class BrilliantGearModule extends Module {

    public static final String CONFIG_KEY = "brilliantGearModule";


    BrilliantGearModule() { super(CONFIG_KEY); }

    @Override
    public boolean isPresentByDefault() { return false; }

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

                BRILLIANT_SWORD = ALL.register("brilliant_sword", () -> new SwordItem(PSTiers.BRILLIANT, 3, -2.4F, BrilliantGearUtils.baseProperties())),
                BRILLIANT_SHOVEL = ALL.register("brilliant_shovel", () -> new ShovelItem(PSTiers.BRILLIANT, 1.5F, -3.0F, BrilliantGearUtils.baseProperties())),
                BRILLIANT_PICKAXE = ALL.register("brilliant_pickaxe", () -> new PickaxeItem(PSTiers.BRILLIANT, 1, -2.8F, BrilliantGearUtils.baseProperties())),
                BRILLIANT_AXE = ALL.register("brilliant_axe", () -> new AxeItem(PSTiers.BRILLIANT, 5, -3.0F, BrilliantGearUtils.baseProperties())),
                BRILLIANT_HOE = ALL.register("brilliant_hoe", () -> new HoeItem(PSTiers.BRILLIANT, -3, -0.0F, BrilliantGearUtils.baseProperties())),

                BRILLIANT_HELMET = ALL.register("brilliant_helmet", () -> new ArmorItem(PSArmorMaterials.BRILLIANT, EquipmentSlot.HEAD, BrilliantGearUtils.baseProperties())),
                BRILLIANT_CHESTPLATE = ALL.register("brilliant_chestplate", () -> new ArmorItem(PSArmorMaterials.BRILLIANT, EquipmentSlot.CHEST, BrilliantGearUtils.baseProperties())),
                BRILLIANT_LEGGINGS = ALL.register("brilliant_leggings", () -> new ArmorItem(PSArmorMaterials.BRILLIANT, EquipmentSlot.LEGS, BrilliantGearUtils.baseProperties())),
                BRILLIANT_BOOTS = ALL.register("brilliant_boots", () -> new ArmorItem(PSArmorMaterials.BRILLIANT, EquipmentSlot.FEET, BrilliantGearUtils.baseProperties()));

    }

    public static class SoundEvents {

        static final DeferredRegister<SoundEvent> ALL = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PoscardsSkills.ID);

        public static final RegistryObject<SoundEvent> ARMOR_EQUIP_BRILLIANT = ALL.register("item.armor.equip_brilliant", () -> new SoundEvent(PoscardsSkills.asResource("item.armor.equip_brilliant")));
    }

}
