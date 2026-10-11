package net.gamerdragon0095.gravedungeons.action.item;

import net.gamerdragon0095.gravedungeons.item.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class SoulTunerAction {
    public static ItemStack SoulTunerSafeInsert(Slot slot, ItemStack inputStack, int inputAmount, int maxStackSize) {
        ItemStack output = inputStack;

        int slotAmount = slot.getItem().getCount();
        int averageAmount = slotAmount + inputAmount;

        if (slot.getItem().getItem() == inputStack.getItem()) {
            while(averageAmount > maxStackSize) {
                if (averageAmount - 1 != maxStackSize) {
                    if (output.getCount() - 1 > 0) {
                        output.shrink(1);
                    } else {
                        System.out.println("I clicked on item with this bundle and returned - SlotItem1: " + slot.getItem());
                        return slot.getItem();
                    }
                    averageAmount = averageAmount - 1;
                } else {
                    break;
                }
            }
            System.out.println("I clicked on item with this bundle and returned - Output: " + output);
            return output;
        } else {
            System.out.println("I clicked on item with this bundle and returned - SlotItem0: " + slot.getItem());
            return slot.getItem();
        }
    }
}
