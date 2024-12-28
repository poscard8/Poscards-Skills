package github.poscard8.poscardsskills;

import github.poscard8.poscardsskills.advancement.PSCriteriaTriggers;
import github.poscard8.poscardsskills.config.PoscardsSkillsClientConfig;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.experiencesource.ExperienceSource;
import github.poscard8.poscardsskills.experiencesource.ExperienceSourceHandler;
import github.poscard8.poscardsskills.experiencesource.types.*;
import github.poscard8.poscardsskills.extension.ExtensionHandler;
import github.poscard8.poscardsskills.registry.*;
import github.poscard8.poscardsskills.skill.SkillHandler;
import github.poscard8.poscardsskills.util.component.AttributeStyle;
import github.poscard8.poscardsskills.util.component.ComponentHandler;
import github.poscard8.poscardsskills.util.tab.PSCreativeModeTab;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("unused")
@Mod(PoscardsSkills.ID)
public final class PoscardsSkills {

    public static final String ID = "poscardsskills";
    public static final String GROUP_ID = "poscardsmods";
    public static final String NAME = "Poscard's Skills";

    public static final LevelResource DIRECTORY = new LevelResource(GROUP_ID);

    public static final KeyMapping KEY_POSCARDS_SKILLS_MENU = new KeyMapping("key.poscardsskills.menu", 82, "key.categories.poscardsskills");

    public static final PSCreativeModeTab CREATIVE_MODE_TAB = new PSCreativeModeTab();

    static final SkillHandler SKILL_HANDLER = new SkillHandler();
    static final ExperienceSourceHandler XP_SOURCE_HANDLER = new ExperienceSourceHandler();
    static final ExtensionHandler EXTENSION_HANDLER = new ExtensionHandler();
    static final ComponentHandler COMPONENT_HANDLER = new ComponentHandler();

    public static ResourceLocation asResource(String path) { return new ResourceLocation(ID, path); }

    public static SkillHandler getSkillHandler() { return SKILL_HANDLER; }

    public static ExperienceSourceHandler getXPSourceHandler() { return XP_SOURCE_HANDLER; }

    public static ExtensionHandler getExtensionHandler() { return EXTENSION_HANDLER; }

    public static ComponentHandler getComponentHandler() { return COMPONENT_HANDLER; }

    @SuppressWarnings("removal")
    public PoscardsSkills() {

        ModLoadingContext ctx = ModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.CLIENT, PoscardsSkillsClientConfig.SPEC, "poscardsskills-client.toml");
        ctx.registerConfig(ModConfig.Type.COMMON, PoscardsSkillsCommonConfig.SPEC, "poscardsskills-common.toml");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        PSAttributes.register(bus);
        PSBlocks.register(bus);
        PSCommandArgumentTypes.register(bus);
        PSConfiguredFeatures.register(bus);
        PSEnchantments.register(bus);
        PSFeatures.register(bus);
        PSItems.register(bus);
        PSMenuTypes.register(bus);
        PSParticleTypes.register(bus);
        PSPlacedFeatures.register(bus);
        PSSoundEvents.register(bus);

        PSCriteriaTriggers.register();

        registerXPSourceTypes();
        setAttributeStyles();
    }

    void registerXPSourceTypes() {

        getXPSourceHandler()
                .registerType(ExperienceSource.BLOCK_KEY, BlockExperienceSource::fromJsonObject)
                .registerType(ExperienceSource.CONSUME_KEY, ConsumeExperienceSource::fromJsonObject)
                .registerType(ExperienceSource.CRAFT_KEY, CraftExperienceSource::fromJsonObject)
                .registerType(ExperienceSource.ENCHANTING_TABLE_KEY, EnchantingTableExperienceSource::fromJsonObject)
                .registerType(ExperienceSource.FISH_KEY, FishExperienceSource::fromJsonObject)
                .registerType(ExperienceSource.ENTITY_KEY, EntityExperienceSource::fromJsonObject)
                .registerType(ExperienceSource.CHEST_KEY, ChestExperienceSource::fromJsonObject)
                .registerType(ExperienceSource.SMELT_KEY, SmeltExperienceSource::fromJsonObject)
                .registerType(ExperienceSource.ADVANCEMENT_KEY, AdvancementExperienceSource::fromJsonObject)
                .registerType(ExperienceSource.ANVIL_ENCHANT_KEY, AnvilEnchantExperienceSource::fromJsonObject)
                .registerType(ExperienceSource.STRUCTURE_KEY, StructureExperienceSource::fromJsonObject);
    }

    void setAttributeStyles() {

        getComponentHandler()
                .setAttributeStyle(Attributes.MAX_HEALTH, new AttributeStyle(ChatFormatting.RED, "❤"))
                .setAttributeStyle(Attributes.ATTACK_DAMAGE, new AttributeStyle(ChatFormatting.DARK_RED, "⚔"))
                .setAttributeStyle(Attributes.MOVEMENT_SPEED, new AttributeStyle(ChatFormatting.WHITE, "⏭"))
                .setAttributeStyle(Attributes.JUMP_STRENGTH, new AttributeStyle(ChatFormatting.GREEN, "↑"))
                .setAttributeStyle(Attributes.ATTACK_SPEED, new AttributeStyle(ChatFormatting.YELLOW, "⟳"))
                .setAttributeStyle(Attributes.ATTACK_KNOCKBACK,  new AttributeStyle(ChatFormatting.AQUA, "⇶"))
                .setAttributeStyle(Attributes.ARMOR, new AttributeStyle(ChatFormatting.GRAY, "◇"))
                .setAttributeStyle(Attributes.ARMOR_TOUGHNESS, new AttributeStyle(ChatFormatting.DARK_GRAY, "◆"))
                .setAttributeStyle(Attributes.KNOCKBACK_RESISTANCE, new AttributeStyle(ChatFormatting.DARK_BLUE, "❖"))
                .setAttributeStyle(Attributes.LUCK, new AttributeStyle(ChatFormatting.LIGHT_PURPLE, "☘"))
                .setAttributeStyle(ForgeMod.SWIM_SPEED, new AttributeStyle(ChatFormatting.DARK_AQUA, "✷"))
                .setAttributeStyle(PSAttributes.CRIT_DAMAGE, new AttributeStyle(ChatFormatting.DARK_BLUE, "※", true))
                .setAttributeStyle(PSAttributes.CHEST_LUCK, new AttributeStyle(ChatFormatting.DARK_GREEN, "☘"))
                .setAttributeStyle(PSAttributes.WISDOM, new AttributeStyle(ChatFormatting.DARK_AQUA, "☯"))
                .setAttributeStyle(PSAttributes.LEGACY, new AttributeStyle(ChatFormatting.LIGHT_PURPLE, "✸"));
    }

}
