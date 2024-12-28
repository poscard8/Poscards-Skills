package github.poscard8.poscardsskills.ui.menu;

import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.skill.SkillInstance;
import github.poscard8.poscardsskills.ui.screen.SkillScreen;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides the skill menu. Position index of the skill is passed as
 * {@code containerId} since that is the only way to send info from
 * {@link SkillMenu} to {@link SkillScreen}.
 */
public class SkillMenuProvider implements MenuProvider {

    protected final Skill skill;
    protected final Component title;

    public SkillMenuProvider(SkillInstance instance) { this(instance.skill); }

    public SkillMenuProvider(Skill skill) {

        this.skill = skill;
        this.title = PSComponents.skill(skill).plainCopy();
    }

    @Override
    @NotNull
    public Component getDisplayName() { return title; }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) { return new SkillMenu(skill.getPositionIndex(), inventory); }


}
