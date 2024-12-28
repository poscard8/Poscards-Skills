package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSParticleTypes {

    public static final DeferredRegister<ParticleType<?>> ALL = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, PoscardsSkills.ID);

    public static final RegistryObject<SimpleParticleType>

            LEVEL_UP = ALL.register("level_up", () -> new SimpleParticleType(false)),
            BRILLIANT = ALL.register("brilliant", () -> new SimpleParticleType(false)),
            BLESSED = ALL.register("blessed", () -> new SimpleParticleType(false)),
            DIVINE = ALL.register("divine", () -> new SimpleParticleType(false));

    public static void register(IEventBus bus) { ALL.register(bus); }

}
