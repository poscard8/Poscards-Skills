package github.poscard8.poscardsskills;

import github.poscard8.poscardsskills.advancement.PSCriteriaTriggers;
import github.poscard8.poscardsskills.config.PoscardsSkillsClientConfig;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourceHandler;
import github.poscard8.poscardsskills.experiencesource.types.*;
import github.poscard8.poscardsskills.module.BaseModule;
import github.poscard8.poscardsskills.module.PSModules;
import github.poscard8.poscardsskills.skill.SkillHandler;
import github.poscard8.poscardsskills.util.component.ComponentHandler;
import github.poscard8.poscardsskills.util.item.PSCreativeModeTab;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("unused")
@Mod(PoscardsSkills.ID)
public class PoscardsSkills {

    public static final String ID = "poscardsskills";
    public static final String GROUP_ID = "poscardsmods";
    public static final String NAME = "Poscard's Skills";

    public static final LevelResource DIRECTORY = new LevelResource(GROUP_ID);

    public static final KeyMapping KEY_SKILL_MENU = new KeyMapping("key.poscardsskills.skill_menu", 82, "key.categories.poscardsskills");

    public static final CreativeModeTab CREATIVE_TAB = new PSCreativeModeTab();

    private static final SkillHandler SKILL_HANDLER = new SkillHandler();
    private static final ExperienceSourceHandler XP_SOURCE_HANDLER = new ExperienceSourceHandler();
    private static final ComponentHandler COMPONENT_HANDLER = new ComponentHandler();

    public static ResourceLocation asResource(String path) { return new ResourceLocation(ID, path); }

    public static SkillHandler getSkillHandler() { return SKILL_HANDLER; }

    public static ExperienceSourceHandler getXPSourceHandler() { return XP_SOURCE_HANDLER; }

    public static ComponentHandler getComponentHandler() { return COMPONENT_HANDLER; }

    public PoscardsSkills() {

        ModLoadingContext ctx = ModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.CLIENT, PoscardsSkillsClientConfig.SPEC, "poscardsskills-client.toml");
        ctx.registerConfig(ModConfig.Type.COMMON, PoscardsSkillsCommonConfig.SPEC, "poscardsskills-common.toml");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PSModules.onSetup(bus);

        PSCriteriaTriggers.registerAll();

        setDefaultSkillPositions();
        registerXPSourceTypes();
        setAttributeStyles();
    }

    private void setDefaultSkillPositions() {

        getSkillHandler()
                .setDefaultPosition(5, asResource("mining"))
                .setDefaultPosition(6, asResource("farming"))
                .setDefaultPosition(7, asResource("combat"))
                .setDefaultPosition(8, asResource("magic"))
                .setDefaultPosition(9, asResource("exploring"));
    }

    private void registerXPSourceTypes() {

        getXPSourceHandler()
                .registerType(asResource("break_block"), BreakBlockExperienceSource::fromJsonObject, BreakBlockExperienceSource.class)
                .registerType(asResource("brew_potion"), BrewPotionExperienceSource::fromJsonObject, BrewPotionExperienceSource.class)
                .registerType(asResource("consume_item"), ConsumeItemExperienceSource::fromJsonObject, ConsumeItemExperienceSource.class)
                .registerType(asResource("craft_item"), CraftItemExperienceSource::fromJsonObject, CraftItemExperienceSource.class)
                .registerType(asResource("enchant_item"), EnchantItemExperienceSource::fromJsonObject, EnchantItemExperienceSource.class)
                .registerType(asResource("fish"), FishExperienceSource::fromJsonObject, FishExperienceSource.class)
                .registerType(asResource("kill_entity"), KillEntityExperienceSource::fromJsonObject, KillEntityExperienceSource.class)
                .registerType(asResource("open_chest"), OpenChestExperienceSource::fromJsonObject, OpenChestExperienceSource.class)
                .registerType(asResource("smelt_item"), SmeltItemExperienceSource::fromJsonObject, SmeltItemExperienceSource.class)
                .registerType(asResource("unlock_advancement"), UnlockAdvancementExperienceSource::fromJsonObject, UnlockAdvancementExperienceSource.class)
                .registerType(asResource("use_anvil"), UseAnvilExperienceSource::fromJsonObject, UseAnvilExperienceSource.class)
                .registerType(asResource("visit_structure"), VisitStructureExperienceSource::fromJsonObject, VisitStructureExperienceSource.class);
    }

    private void setAttributeStyles() {

        getComponentHandler()
                .setAttributeStyle(() -> Attributes.MAX_HEALTH, ChatFormatting.RED, "❤")
                .setAttributeStyle(() -> Attributes.ATTACK_DAMAGE, ChatFormatting.DARK_RED, "⚔")
                .setAttributeStyle(() -> Attributes.MOVEMENT_SPEED, ChatFormatting.WHITE, "⏭")
                .setAttributeStyle(() -> Attributes.JUMP_STRENGTH, ChatFormatting.GREEN, "↑")
                .setAttributeStyle(() -> Attributes.ATTACK_SPEED, ChatFormatting.YELLOW, "⟳")
                .setAttributeStyle(() -> Attributes.ATTACK_KNOCKBACK, ChatFormatting.AQUA, "⇶")
                .setAttributeStyle(() -> Attributes.ARMOR, ChatFormatting.GRAY, "◇")
                .setAttributeStyle(() -> Attributes.ARMOR_TOUGHNESS, ChatFormatting.DARK_GRAY, "◆")
                .setAttributeStyle(() -> Attributes.KNOCKBACK_RESISTANCE, ChatFormatting.DARK_BLUE, "❖")
                .setAttributeStyle(() -> Attributes.LUCK, ChatFormatting.LIGHT_PURPLE, "☘")
                .setAttributeStyle(ForgeMod.SWIM_SPEED, ChatFormatting.DARK_AQUA, "⏭")
                .setAttributeStyle(ForgeMod.REACH_DISTANCE, ChatFormatting.BLUE, "⛏")
                .setAttributeStyle(ForgeMod.ATTACK_RANGE, ChatFormatting.GOLD, "⚔")
                .setAttributeStyle(BaseModule.Attributes.CHEST_LUCK, ChatFormatting.DARK_GREEN, "☘")
                .setAttributeStyle(BaseModule.Attributes.WISDOM, ChatFormatting.DARK_AQUA, "☯");
    }

}
