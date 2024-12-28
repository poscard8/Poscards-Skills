package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.ui.menu.PoscardsSkillsMenu;
import github.poscard8.poscardsskills.ui.menu.SkillCraftingMenu;
import github.poscard8.poscardsskills.ui.menu.SkillMenu;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSMenuTypes {

    public static final DeferredRegister<MenuType<?>> ALL = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PoscardsSkills.ID);

    public static final RegistryObject<MenuType<PoscardsSkillsMenu>> MAIN = ALL.register("main", () -> new MenuType<>(PoscardsSkillsMenu::new, FeatureFlagSet.of()));
    public static final RegistryObject<MenuType<SkillMenu>> SKILL = ALL.register("skill", () -> new MenuType<>(SkillMenu::new, FeatureFlagSet.of()));
    public static final RegistryObject<MenuType<SkillCraftingMenu>> SKILL_CRAFTING = ALL.register("skill_crafting", () -> new MenuType<>(SkillCraftingMenu::new, FeatureFlagSet.of()));

    public static void register(IEventBus bus) { ALL.register(bus); }

}
