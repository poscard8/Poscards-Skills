package github.poscard8.poscardsskills.registry;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.config.PoscardsSkillsCommonConfig;
import github.poscard8.poscardsskills.item.CatalystItem;
import github.poscard8.poscardsskills.item.MagicShardItem;
import github.poscard8.poscardsskills.item.RepairStoneItem;
import github.poscard8.poscardsskills.item.RuneItem;
import github.poscard8.poscardsskills.util.item.PSRarities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSItems {

    public static final DeferredRegister<Item> ALL = DeferredRegister.create(ForgeRegistries.ITEMS, PoscardsSkills.ID);

    public static final RegistryObject<Item>

            ELEGANT_RUNE = ALL.register("elegant_rune", () -> new RuneItem(new Item.Properties(), PSRarities.ELEGANT)),
            ETHEREAL_RUNE = ALL.register("ethereal_rune", () -> new RuneItem(new Item.Properties(), PSRarities.ETHEREAL)),
            CLASSICAL_RUNE = ALL.register("classical_rune", () -> new RuneItem(new Item.Properties(), PSRarities.CLASSICAL)),

            BRILLIANT_SHARD = ALL.register("brilliant_shard", () -> new MagicShardItem(new Item.Properties().stacksTo(32).rarity(Rarity.UNCOMMON), PoscardsSkillsCommonConfig.BRILLIANT_SHARD_WISDOM)),
            BLESSED_SHARD = ALL.register("blessed_shard", () -> new MagicShardItem(new Item.Properties().stacksTo(32).rarity(Rarity.RARE), PoscardsSkillsCommonConfig.BLESSED_SHARD_WISDOM)),
            DIVINE_SHARD = ALL.register("divine_shard", () -> new MagicShardItem(new Item.Properties().stacksTo(32).rarity(Rarity.EPIC), PoscardsSkillsCommonConfig.DIVINE_SHARD_WISDOM)),
            BRILLIANT_REPAIR_STONE = ALL.register("brilliant_repair_stone", () -> new RepairStoneItem(new Item.Properties().rarity(Rarity.UNCOMMON), PoscardsSkillsCommonConfig.BRILLIANT_REPAIR_STONE_DURABILITY)),
            BLESSED_REPAIR_STONE = ALL.register("blessed_repair_stone", () -> new RepairStoneItem(new Item.Properties().rarity(Rarity.RARE), PoscardsSkillsCommonConfig.BLESSED_REPAIR_STONE_DURABILITY)),
            DIVINE_REPAIR_STONE = ALL.register("divine_repair_stone", () -> new RepairStoneItem(new Item.Properties().rarity(Rarity.EPIC), PoscardsSkillsCommonConfig.DIVINE_REPAIR_STONE_DURABILITY)),
            BRILLIANT_CATALYST = ALL.register("brilliant_catalyst", () -> new CatalystItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), PoscardsSkills.asResource("gameplay/brilliant_catalyst"), PoscardsSkills.asResource("gameplay/brilliant_catalyst_no_extra_progression"), PSParticleTypes.BRILLIANT)),
            BLESSED_CATALYST = ALL.register("blessed_catalyst", () -> new CatalystItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), PoscardsSkills.asResource("gameplay/blessed_catalyst"), PSParticleTypes.BLESSED)),
            DIVINE_CATALYST = ALL.register("divine_catalyst", () -> new CatalystItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), PoscardsSkills.asResource("gameplay/divine_catalyst"), PSParticleTypes.DIVINE)),

            SECRET = ALL.register("secret", () -> new Item(new Item.Properties()));


    public static void register(IEventBus bus) { ALL.register(bus); }

}
