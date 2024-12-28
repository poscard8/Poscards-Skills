package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.command.SecretArgumentType;
import github.poscard8.poscardsskills.command.SkillArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class PSCommandArgumentTypes {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ALL = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, PoscardsSkills.ID);

    public static final RegistryObject<SingletonArgumentInfo<SkillArgumentType>> SKILL = ALL.register("skill",
            () -> ArgumentTypeInfos.registerByClass(SkillArgumentType.class, SingletonArgumentInfo.contextFree(SkillArgumentType::of)));
    public static final RegistryObject<SingletonArgumentInfo<SecretArgumentType>> SECRET = ALL.register("secret",
            () -> ArgumentTypeInfos.registerByClass(SecretArgumentType.class, SingletonArgumentInfo.contextFree(SecretArgumentType::of)));

    public static void register(IEventBus bus) { ALL.register(bus); }

}
