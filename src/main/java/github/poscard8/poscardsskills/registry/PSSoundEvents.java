package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSSoundEvents {

    public static final DeferredRegister<SoundEvent> ALL = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PoscardsSkills.ID);

    public static final RegistryObject<SoundEvent> REPAIR_STONE_APPLY = registerSound("repair_stone_apply");

    static RegistryObject<SoundEvent> registerSound(String name) { return ALL.register(name, () -> SoundEvent.createVariableRangeEvent(PoscardsSkills.asResource(name))); }

    public static void register(IEventBus bus) { ALL.register(bus); }

}
