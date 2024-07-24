package github.poscard8.poscardsskills.module;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.command.SkillArgumentType;
import github.poscard8.poscardsskills.item.BrilliantShardItem;
import github.poscard8.poscardsskills.item.MenditeItem;
import github.poscard8.poscardsskills.item.RuneItem;
import github.poscard8.poscardsskills.ui.menu.PoscardsSkillsMenu;
import github.poscard8.poscardsskills.ui.menu.SkillCraftingMenu;
import github.poscard8.poscardsskills.ui.menu.SkillMenu;
import github.poscard8.poscardsskills.util.item.PSRarities;
import github.poscard8.poscardsskills.util.wrapper.BlockSet;
import github.poscard8.poscardsskills.util.wrapper.BlockType;
import github.poscard8.poscardsskills.util.wrapper.BlockWrapper;
import github.poscard8.poscardsskills.worldgen.DoubleOreConfiguration;
import github.poscard8.poscardsskills.worldgen.DoubleOreFeature;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@SuppressWarnings("unused")
public final class BaseModule extends Module {


    BaseModule() { super(""); }

    @Override
    public boolean isPresent() { return true; }

    @Override
    protected void whenPresent(IEventBus bus) {

        Attributes.ALL.register(bus);
        CommandArgumentTypes.ALL.register(bus);
        ConfiguredFeatures.ALL.register(bus);
        Features.ALL.register(bus);
        Items.ALL.register(bus);
        MenuTypes.ALL.register(bus);
        PlacedFeatures.ALL.register(bus);
        SoundEvents.ALL.register(bus);

        BlockWrapper.BASE_BLOCK_REGISTRY.register(bus);
        BlockWrapper.BASE_ITEM_REGISTRY.register(bus);

        Blocks.IGNORED.register(bus);
    }

    @Override
    protected void whenAbsent(IEventBus bus) {
    }


    public static class Attributes {

        static final DeferredRegister<Attribute> ALL = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, PoscardsSkills.ID);

        public static final RegistryObject<Attribute> CHEST_LUCK = ALL.register("player.chest_luck", () -> new RangedAttribute("poscardsskills.player.chest_luck", 0D, -100D, 1024D));
        public static final RegistryObject<Attribute> WISDOM = ALL.register("player.wisdom", () -> new RangedAttribute("poscardsskills.player.wisdom", 0D, -100D, 1024D));

    }

    public static class Blocks {

        // is there so RegisterEvent reads this class
        static DeferredRegister<Block> IGNORED = DeferredRegister.create(ForgeRegistries.BLOCKS, PoscardsSkills.ID);

        public static final BlockWrapper

                ROUGH_JADE = new BlockWrapper(BlockSet.JADE, BlockType.ROUGH),
                ROUGH_JASPER = new BlockWrapper(BlockSet.JASPER, BlockType.ROUGH),
                ROUGH_MARBLE = new BlockWrapper(BlockSet.MARBLE, BlockType.ROUGH);

    }

    public static class CommandArgumentTypes {

        static final DeferredRegister<ArgumentTypeInfo<?, ?>> ALL = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, PoscardsSkills.ID);

        public static final RegistryObject<SingletonArgumentInfo<SkillArgumentType>> SKILL = ALL.register("skill",
                () -> ArgumentTypeInfos.registerByClass(SkillArgumentType.class, SingletonArgumentInfo.contextFree(SkillArgumentType::of)));

    }

    public static class ConfiguredFeatures {

        static final DeferredRegister<ConfiguredFeature<?, ?>> ALL = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, PoscardsSkills.ID);

        public static final RegistryObject<ConfiguredFeature<?, ?>> ORE_JADE = ALL.register("ore_jade",
                () -> new ConfiguredFeature<>(Features.DOUBLE_ORE.get(), new DoubleOreConfiguration(OreFeatures.NATURAL_STONE, Blocks.ROUGH_JADE.defaultBlockState(), net.minecraft.world.level.block.Blocks.SMOOTH_BASALT.defaultBlockState(), 70)));

        public static final RegistryObject<ConfiguredFeature<?, ?>> ORE_JASPER = ALL.register("ore_jasper",
                () -> new ConfiguredFeature<>(Features.DOUBLE_ORE.get(), new DoubleOreConfiguration(OreFeatures.NATURAL_STONE, Blocks.ROUGH_JASPER.defaultBlockState(), net.minecraft.world.level.block.Blocks.SMOOTH_BASALT.defaultBlockState(), 70)));

        public static final RegistryObject<ConfiguredFeature<?, ?>> ORE_MARBLE = ALL.register("ore_marble",
                () -> new ConfiguredFeature<>(Features.DOUBLE_ORE.get(), new DoubleOreConfiguration(OreFeatures.NATURAL_STONE, Blocks.ROUGH_MARBLE.defaultBlockState(), net.minecraft.world.level.block.Blocks.SMOOTH_BASALT.defaultBlockState(), 70)));

    }

    public static class Features {

        static final DeferredRegister<Feature<?>> ALL = DeferredRegister.create(ForgeRegistries.FEATURES, PoscardsSkills.ID);

        public static final RegistryObject<Feature<DoubleOreConfiguration>> DOUBLE_ORE = ALL.register("double_ore", () -> new DoubleOreFeature(DoubleOreConfiguration.CODEC));

    }

    public static class Items {

        static final DeferredRegister<Item> ALL = DeferredRegister.create(ForgeRegistries.ITEMS, PoscardsSkills.ID);

        public static final RegistryObject<Item>

                ELEGANT_RUNE = ALL.register("elegant_rune", () -> new RuneItem(new Item.Properties().tab(PoscardsSkills.CREATIVE_TAB), PSRarities.ELEGANT)),
                ETHEREAL_RUNE = ALL.register("ethereal_rune", () -> new RuneItem(new Item.Properties().tab(PoscardsSkills.CREATIVE_TAB), PSRarities.ETHEREAL)),
                CLASSICAL_RUNE = ALL.register("classical_rune", () -> new RuneItem(new Item.Properties().tab(PoscardsSkills.CREATIVE_TAB), PSRarities.CLASSICAL)),
                BRILLIANT_RUNE = ALL.register("brilliant_rune", () -> new RuneItem(new Item.Properties(), PSRarities.BRILLIANT)),
                RADIANT_RUNE = ALL.register("radiant_rune", () -> new RuneItem(new Item.Properties(), PSRarities.RADIANT)),

                MENDITE = ALL.register("mendite", () -> new MenditeItem(new Item.Properties().rarity(Rarity.UNCOMMON).tab(PoscardsSkills.CREATIVE_TAB))),
                BRILLIANT_SHARD = ALL.register("brilliant_shard", () -> new BrilliantShardItem(new Item.Properties().stacksTo(50).rarity(Rarity.RARE).tab(PoscardsSkills.CREATIVE_TAB)));

    }

    public static class MenuTypes {

        static final DeferredRegister<MenuType<?>> ALL = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PoscardsSkills.ID);

        public static final RegistryObject<MenuType<PoscardsSkillsMenu>> MAIN = ALL.register("main", () -> new MenuType<>(PoscardsSkillsMenu::new));
        public static final RegistryObject<MenuType<SkillMenu>> SKILL = ALL.register("skill", () -> new MenuType<>(SkillMenu::new));
        public static final RegistryObject<MenuType<SkillCraftingMenu>> SKILL_CRAFTING = ALL.register("skill_crafting", () -> new MenuType<>(SkillCraftingMenu::new));

    }

    public static class PlacedFeatures {

        static final DeferredRegister<PlacedFeature> ALL = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, PoscardsSkills.ID);

        public static final RegistryObject<PlacedFeature>

                ORE_JADE_PLACED = ALL.register("ore_jade", () -> new PlacedFeature(ConfiguredFeatures.ORE_JADE.getHolder().orElseThrow(), psOrePlacement())),
                ORE_JASPER_PLACED = ALL.register("ore_jasper", () -> new PlacedFeature(ConfiguredFeatures.ORE_JASPER.getHolder().orElseThrow(), psOrePlacement())),
                ORE_MARBLE_PLACED = ALL.register("ore_marble", () -> new PlacedFeature(ConfiguredFeatures.ORE_MARBLE.getHolder().orElseThrow(), psOrePlacement()));

        private static List<PlacementModifier> psOrePlacement() {

            PlacementModifier count = RarityFilter.onAverageOnceEvery(2);
            PlacementModifier spread = InSquarePlacement.spread();
            PlacementModifier height = HeightRangePlacement.uniform(VerticalAnchor.absolute(8), VerticalAnchor.absolute(56));
            PlacementModifier biome = BiomeFilter.biome();

            return List.of(count, spread, height, biome);
        }

    }

    public static class SoundEvents {

        static final DeferredRegister<SoundEvent> ALL = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PoscardsSkills.ID);

        public static final RegistryObject<SoundEvent>

                XP_GAIN = registerSound("xp_gain"),
                LEVEL_UP = registerSound("level_up"),
                RUNE_APPLY = registerSound("rune_apply"),
                MENDITE_APPLY = registerSound("mendite_apply");

        private static RegistryObject<SoundEvent> registerSound(String name) { return ALL.register(name, () -> new SoundEvent(PoscardsSkills.asResource(name))); }

    }

}
