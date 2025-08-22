package github.poscard8.poscardsskills.registry;

import github.poscard8.peritia.enchantment.AttributeEnchantment;
import github.poscard8.peritia.enchantment.SymmetryEnchantment;
import github.poscard8.peritia.registry.PeritiaAttributes;
import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class PSEnchantments
{
    public static final DeferredRegister<Enchantment> ALL = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PoscardsSkills.ID);

    public static final RegistryObject<Enchantment> POCKET_WISDOM = ALL.register("pocket_wisdom", () -> new AttributeEnchantment(Enchantment.Rarity.RARE, PeritiaAttributes.WISDOM.get(), 10, 2));
    public static final RegistryObject<Enchantment> SYMMETRY = ALL.register("symmetry", () -> new SymmetryEnchantment(Enchantment.Rarity.RARE, (1 / 30.0D), 1));

    public static void register(IEventBus bus) { ALL.register(bus); }

}
