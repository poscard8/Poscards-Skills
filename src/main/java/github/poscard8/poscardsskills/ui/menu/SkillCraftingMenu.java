package github.poscard8.poscardsskills.ui.menu;

import github.poscard8.poscardsskills.module.BaseModule;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;


public class SkillCraftingMenu extends PoscardsSkillsMenu {

    public static final SimpleMenuProvider PROVIDER = new SimpleMenuProvider(SkillCraftingMenu::new, PSComponents.skillCrafting());
    public static final int Y_OFFSET = 14;

    public SkillCraftingMenu(int id, Inventory inventory, Player player) { this(id, inventory, Y_OFFSET); }

    public SkillCraftingMenu(int id, Inventory inventory) { this(id, inventory, Y_OFFSET); }

    public SkillCraftingMenu(int id, Inventory inventory, int offset) { super(BaseModule.MenuTypes.SKILL_CRAFTING.get(), id, inventory, offset); }

}
