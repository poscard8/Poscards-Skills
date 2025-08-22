package github.poscard8.poscardsskills.registry;

import github.poscard8.peritia.util.text.ColorGradients;
import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.config.PoscardsSkillsServerConfig;
import github.poscard8.poscardsskills.item.*;
import github.poscard8.poscardsskills.util.PSTags;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class PSItems
{
    public static final DeferredRegister<Item> ALL = DeferredRegister.create(ForgeRegistries.ITEMS, PoscardsSkills.ID);

    public static final RegistryObject<Item>

            CLASSICAL_ARMOR_TRIM_SMITHING_TEMPLATE = ALL.register("classical_armor_trim_smithing_template", () -> SmithingTemplateItem.createArmorTrimTemplate(PoscardsSkills.asResource("classical"))),
            LUMINOUS_GEMSTONE = ALL.register("luminous_gemstone", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON))),
            BRILLIANT_GEMSTONE = ALL.register("brilliant_gemstone", () -> new Item(new Item.Properties().rarity(Rarity.RARE))),
            LUCKY_KEY = ALL.register("lucky_key", () -> new KeyItem(new Item.Properties(), PoscardsSkillsServerConfig.LUCKY_KEY_LUCK_VALUE, PoscardsSkillsServerConfig.LUCKY_KEY_WISDOM_VALUE)),
            MIGHTY_KEY = ALL.register("mighty_key", () -> new KeyItem(new Item.Properties().rarity(Rarity.UNCOMMON), PoscardsSkillsServerConfig.MIGHTY_KEY_LUCK_VALUE, PoscardsSkillsServerConfig.MIGHTY_KEY_WISDOM_VALUE)),
            REPAIR_STONE = ALL.register("repair_stone", () -> new RepairStoneItem(new Item.Properties(), PoscardsSkillsServerConfig.REPAIR_STONE_RESTORE_VALUE)),
            GOLDEN_RUNE = ALL.register("golden_rune", () -> new RuneItem(new Item.Properties(), ColorGradients.GOLD)),
            DIAMOND_RUNE = ALL.register("diamond_rune", () -> new RuneItem(new Item.Properties(), ColorGradients.DIAMOND)),
            NETHERITE_RUNE = ALL.register("netherite_rune", () -> new RuneItem(new Item.Properties().fireResistant(), ColorGradients.NETHERITE)),
            AMETHYST_RUNE = ALL.register("amethyst_rune", () -> new RuneItem(new Item.Properties(), ColorGradients.AMETHYST)),
            EMERALD_RUNE = ALL.register("emerald_rune", () -> new RuneItem(new Item.Properties(), ColorGradients.EMERALD)),
            COMPOSITE_RUNE = ALL.register("composite_rune", () -> new RuneItem(new Item.Properties().fireResistant(), ColorGradients.COMPOSITE)),
            GLASS_RUNE = ALL.register("glass_rune", () -> new EmptyRuneItem(new Item.Properties())),
            VOLATILE_SHOVEL = ALL.register("volatile_shovel", () -> new VolatileItem.Shovel(new Item.Properties().rarity(Rarity.RARE))),
            VOLATILE_PICKAXE = ALL.register("volatile_pickaxe", () -> new VolatileItem.Pickaxe(new Item.Properties().rarity(Rarity.RARE))),
            VOLATILE_AXE = ALL.register("volatile_axe", () -> new VolatileItem.Axe(new Item.Properties().rarity(Rarity.RARE))),
            VOLATILE_HOE = ALL.register("volatile_hoe", () -> new VolatileItem.Hoe(new Item.Properties().rarity(Rarity.RARE))),
            VOLATILE_SWORD = ALL.register("volatile_sword", () -> new VolatileItem.Sword(new Item.Properties().rarity(Rarity.RARE))),
            ROMAN_NUMERALS_BANNER_PATTERN = ALL.register("roman_numerals_banner_pattern", () -> new BannerPatternItem(PSTags.BannerPatterns.ROMAN_NUMERALS, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1))),
            NUMBERS_BANNER_PATTERN = ALL.register("numbers_banner_pattern", () -> new BannerPatternItem(PSTags.BannerPatterns.NUMBERS, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1))),
            ICONS_BANNER_PATTERN = ALL.register("icons_banner_pattern", () -> new BannerPatternItem(PSTags.BannerPatterns.ICONS, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));

    public static void register(IEventBus bus) { ALL.register(bus); }

}
