package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.enchantment.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSEnchantments {

    public static final DeferredRegister<Enchantment> ALL = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PoscardsSkills.ID);

    public static final RegistryObject<PSEnchantment>

            TRUE_EFFICIENCY = ALL.register("true_efficiency", () -> new TrueEfficiencyEnchantment(EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND})),
            VITALITY = ALL.register("vitality", () -> new VitalityEnchantment(EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST})),
            EXPLOITATION = ALL.register("exploitation", () -> new ExploitationEnchantment(EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND})),
            DOMINANCE = ALL.register("dominance", () -> new DominanceEnchantment(EnchantmentCategory.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD}));

    public static void register(IEventBus bus) { ALL.register(bus); }

}
