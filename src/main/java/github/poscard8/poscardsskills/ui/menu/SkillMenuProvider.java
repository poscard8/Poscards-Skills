package github.poscard8.poscardsskills.ui.menu;

import github.poscard8.poscardsskills.skill.Skill;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import org.jetbrains.annotations.Nullable;

public class SkillMenuProvider implements MenuProvider {

    private final MenuConstructor menuConstructor;
    private final Component title;

    public SkillMenuProvider(MenuConstructor menuConstructor, Skill skill) {

        this.menuConstructor = menuConstructor;
        this.title = PSComponents.skill(skill).plainCopy();
    }

    @Override
    public Component getDisplayName() { return title; }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) { return menuConstructor.createMenu(id, inventory, player); }
}
