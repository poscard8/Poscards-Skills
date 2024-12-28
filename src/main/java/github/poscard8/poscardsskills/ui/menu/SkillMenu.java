package github.poscard8.poscardsskills.ui.menu;

import github.poscard8.poscardsskills.PoscardsSkills;
import github.poscard8.poscardsskills.registry.PSMenuTypes;
import github.poscard8.poscardsskills.skill.Skill;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class SkillMenu extends PoscardsSkillsMenu {

    public static final int Y_OFFSET = 22;

    public Skill skill;

    /**
     * @param id Used as the position index.
     */
    public SkillMenu(int id, Inventory inventory) {

        super(PSMenuTypes.SKILL.get(), id, inventory, Y_OFFSET);

        Optional<Skill> optional = PoscardsSkills.getSkillHandler().byPositionIndex(id);
        this.skill = optional.orElse(null);
    }

}
