package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSAttributes {

    public static final DeferredRegister<Attribute> ALL = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, PoscardsSkills.ID);

    public static final RegistryObject<Attribute>

            CRIT_DAMAGE = ALL.register("generic.crit_damage", () -> new RangedAttribute("poscardsskills.generic.crit_damage", 50D, -100D, 1024D)),
            CHEST_LUCK = ALL.register("player.chest_luck", () -> new RangedAttribute("poscardsskills.player.chest_luck", 0D, -100D, 1024D)),
            WISDOM = ALL.register("player.wisdom", () -> new RangedAttribute("poscardsskills.player.wisdom", 0D, -100D, 1024D)),
            LEGACY = ALL.register("player.legacy", () -> new RangedAttribute("poscardsskills.player.legacy", 0D, 0D, 24D));


    public static void register(IEventBus bus) { ALL.register(bus); }

}
