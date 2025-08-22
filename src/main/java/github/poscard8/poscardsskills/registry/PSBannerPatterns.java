package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class PSBannerPatterns
{
    public static final DeferredRegister<BannerPattern> ALL = DeferredRegister.create(Registries.BANNER_PATTERN, PoscardsSkills.ID);

    public static final RegistryObject<BannerPattern> ROMAN_1 = ALL.register("roman_1", () -> new BannerPattern("rm_1"));
    public static final RegistryObject<BannerPattern> ROMAN_5 = ALL.register("roman_5", () -> new BannerPattern("rm_5"));
    public static final RegistryObject<BannerPattern> ROMAN_10 = ALL.register("roman_10", () -> new BannerPattern("rm_10"));
    public static final RegistryObject<BannerPattern> ROMAN_50 = ALL.register("roman_50", () -> new BannerPattern("rm_50"));
    public static final RegistryObject<BannerPattern> ROMAN_100 = ALL.register("roman_100", () -> new BannerPattern("rm_100"));
    public static final RegistryObject<BannerPattern> ROMAN_500 = ALL.register("roman_500", () -> new BannerPattern("rm_500"));
    public static final RegistryObject<BannerPattern> ROMAN_1000 = ALL.register("roman_1000", () -> new BannerPattern("rm_1000"));

    public static final RegistryObject<BannerPattern> NUMBER_0 = ALL.register("number_0", () -> new BannerPattern("no_0"));
    public static final RegistryObject<BannerPattern> NUMBER_1 = ALL.register("number_1", () -> new BannerPattern("no_1"));
    public static final RegistryObject<BannerPattern> NUMBER_2 = ALL.register("number_2", () -> new BannerPattern("no_2"));
    public static final RegistryObject<BannerPattern> NUMBER_3 = ALL.register("number_3", () -> new BannerPattern("no_3"));
    public static final RegistryObject<BannerPattern> NUMBER_4 = ALL.register("number_4", () -> new BannerPattern("no_4"));
    public static final RegistryObject<BannerPattern> NUMBER_5 = ALL.register("number_5", () -> new BannerPattern("no_5"));
    public static final RegistryObject<BannerPattern> NUMBER_6 = ALL.register("number_6", () -> new BannerPattern("no_6"));
    public static final RegistryObject<BannerPattern> NUMBER_7 = ALL.register("number_7", () -> new BannerPattern("no_7"));
    public static final RegistryObject<BannerPattern> NUMBER_8 = ALL.register("number_8", () -> new BannerPattern("no_8"));
    public static final RegistryObject<BannerPattern> NUMBER_9 = ALL.register("number_9", () -> new BannerPattern("no_9"));

    public static final RegistryObject<BannerPattern> GEMSTONE = ALL.register("gemstone", () -> new BannerPattern("gem"));
    public static final RegistryObject<BannerPattern> HEART = ALL.register("heart", () -> new BannerPattern("hrt"));
    public static final RegistryObject<BannerPattern> PICKAXE = ALL.register("pickaxe", () -> new BannerPattern("pck"));
    public static final RegistryObject<BannerPattern> AXE = ALL.register("axe", () -> new BannerPattern("axe"));
    public static final RegistryObject<BannerPattern> HOE = ALL.register("hoe", () -> new BannerPattern("hoe"));
    public static final RegistryObject<BannerPattern> SWORD = ALL.register("sword", () -> new BannerPattern("swd"));
    public static final RegistryObject<BannerPattern> HAMMER = ALL.register("hammer", () -> new BannerPattern("hmr"));

    public static void register(IEventBus bus) { ALL.register(bus); }

}
