package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSSoundEvents {

    public static final DeferredRegister<SoundEvent> ALL = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PoscardsSkills.ID);

    public static final RegistryObject<SoundEvent>

            XP_GAIN = registerSound("xp_gain"),
            LEVEL_UP = registerSound("level_up"),
            UNLOCK_SECRET = registerSound("unlock_secret"),
            RUNE_APPLY = registerSound("rune_apply"),
            REPAIR_STONE_APPLY = registerSound("repair_stone_apply"),
            CATALYST_USE = registerSound("catalyst_use");

    static RegistryObject<SoundEvent> registerSound(String name) { return ALL.register(name, () -> SoundEvent.createVariableRangeEvent(PoscardsSkills.asResource(name))); }

    public static void register(IEventBus bus) { ALL.register(bus); }

}
