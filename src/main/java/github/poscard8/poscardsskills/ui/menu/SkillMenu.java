package github.poscard8.poscardsskills.ui.menu;

import github.poscard8.poscardsskills.module.BaseModule;
import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class SkillMenu extends PoscardsSkillsMenu {

    public static final int Y_OFFSET = 22;

    public final Skill skill;

    @SuppressWarnings("unused")
    private SkillMenu(int id, Inventory inventory, Player player, Skill skill) {

        super(BaseModule.MenuTypes.SKILL.get(), id, inventory, Y_OFFSET);
        this.skill = skill;
    }

    public SkillMenu(int id, Inventory inventory) {

        super(BaseModule.MenuTypes.SKILL.get(), id, inventory, Y_OFFSET);
        this.skill = null;
    }

    public static SkillMenuProvider provider(SkillInstance instance) { return provider(instance.skill); }

    public static SkillMenuProvider provider(Skill skill) {

        return new SkillMenuProvider((id, inventory, player) -> new SkillMenu(id, inventory, player, skill), skill);
    }

}
