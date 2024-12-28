package github.poscard8.poscardsskills.ui.menu;

import github.poscard8.poscardsskills.registry.PSMenuTypes;
import github.poscard8.poscardsskills.util.component.PSComponents;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Simple menu with inventory + hotbar slots (36 in total).
 */
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class PoscardsSkillsMenu extends AbstractContainerMenu {

    public static final SimpleMenuProvider PROVIDER = new SimpleMenuProvider(PoscardsSkillsMenu::new, PSComponents.uiTitle());

    protected PoscardsSkillsMenu(int id, Inventory inventory, Player player) { this(PSMenuTypes.MAIN.get(), id, inventory, 0); }

    public PoscardsSkillsMenu(int id, Inventory inventory) { this(PSMenuTypes.MAIN.get(), id, inventory, 0); }

    public PoscardsSkillsMenu(MenuType<?> menuType, int id, Inventory inventory, int offset) {

        super(menuType, id);

        for(int x = 0; x < 9; ++x) addSlot(new Slot(inventory, x, 8 + x * 18, 142 + offset));

        for(int y = 1; y < 4; ++y) {

            for(int x = 0; x < 9; ++x) addSlot(new Slot(inventory, x + y * 9, 8 + x * 18, 66 + y * 18 + offset));
        }
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(Player player, int index) {

        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {

            ItemStack stack = slot.getItem();
            result = stack.copy();

            if (index >= 9 && index < 36) {

                if (!this.moveItemStackTo(stack, 0, 9, false)) return ItemStack.EMPTY;

            } else if (index < 9) {

                if (!this.moveItemStackTo(stack, 9, 36, false)) return ItemStack.EMPTY;

            } else if (!this.moveItemStackTo(stack, 0, 36, false)) return ItemStack.EMPTY;


            if (stack.isEmpty()) { slot.set(ItemStack.EMPTY); } else slot.setChanged();
            if (stack.getCount() == result.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, stack);
            if (index == 0) player.drop(stack, false);
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) { return true; }

}
