package net.gamerdragon0095.gravedungeons.item.custom;


import net.gamerdragon0095.gravedungeons.action.item.SoulTunerAction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;

import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SoulTuner extends BundleItem {
    private static final int MAX_CUSTOM_CAPACITY = 1;

    public SoulTuner(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> output, TooltipFlag tooltipFlag) {
        // Target string we want to filter out
        String targetInfoString = Component.translatable("item.minecraft.bundle.info").getString();

        // Pass an intercepted consumer to super to filter out the bundle text dynamically
        super.appendHoverText(stack, context, display, component -> {
            if (!component.getString().equals(targetInfoString)) {
                output.accept(component);
            }
        }, tooltipFlag);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack bundle) {
        BundleContents contents = bundle.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null || contents.items().isEmpty()) {
            return Optional.empty();
        }

        // Return a custom component instead of the vanilla BundleTooltip
        return Optional.of(new SoulTunerTooltip(contents));
    }

    public record SoulTunerTooltip(BundleContents contents) implements TooltipComponent {
        // Leaving this custom record block allows client side mixins / rendering systems
        // to read your layout separately from vanilla's hardcoded progress bar code.
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        BundleContents contents = (BundleContents)stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return false;
    }

    // Handled when you click AN ITEM with THIS BUNDLE
    @Override
    public boolean overrideStackedOnOther(ItemStack bundleStack, Slot slot, ClickAction action, Player player) {
        System.out.println("I clicked on item with this bundle!" + slot.getItem());
        if (!slot.allowModification(player))
            return false;

        if (action == ClickAction.PRIMARY) {
            if (slot.getItem().isEmpty())
                return false;

            int amountInserted = tryInsertIntoCustomBundle(bundleStack, slot.getItem());
            if (amountInserted > 0) {
                slot.getItem().shrink(amountInserted);
            }
        } else {
            if (slot.getItem().isEmpty()) {
                // Take an item out if holding nothing and right-clicking the bundle
                removeTopItem(bundleStack, slot, player);
            } else {
                // Try to put the held item inside
                return false;
            }
        }


        return true;

        /*System.out.println("I clicked on item with this bundle!" + slot.getItem());
        BundleContents initialContents = (BundleContents)bundleStack.get(DataComponents.BUNDLE_CONTENTS);
        if (initialContents != null && bundleStack.getCount() == 1) {
            System.out.println("test0");
            ItemStack other = slot.getItem();
            BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
            if (action == ClickAction.PRIMARY && !other.isEmpty()) {
                System.out.println("test1");
                if (tryInsertIntoCustomBundle(bundleStack, other) > 0) {
                    slot.getItem().shrink(tryInsertIntoCustomBundle(bundleStack, other));

                    ItemStack itemsToAdd = slot.getItem();
                    itemsToAdd.setCount(tryInsertIntoCustomBundle(bundleStack, other));

                    contents.tryInsert(itemsToAdd);

                    System.out.println("test2");
                    playInsertSound(player);
                } else {
                    System.out.println("test3");
                    playInsertFailSound(player);
                }

                bundleStack.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
                this.broadcastChangesOnContainerMenu(player);
                System.out.println("test4");
                return true;
            } else if (action == ClickAction.SECONDARY && other.isEmpty()) {
                System.out.println("test5");
                ItemStack itemStack = contents.removeOne();
                if (itemStack != null) {
                    System.out.println("test6");
                    ItemStack remainder = SoulTunerAction.SoulTunerSafeInsert(slot, itemStack, itemStack.getCount(), MAX_CUSTOM_CAPACITY);
                    if (remainder.getCount() > 0) {
                        System.out.println("test7");
                        contents.tryInsert(remainder);
                    } else {
                        System.out.println("test8");
                        playRemoveOneSound(player);
                    }
                }

                bundleStack.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
                this.broadcastChangesOnContainerMenu(player);
                System.out.println("test9");
                return true;
            } else {
                System.out.println("test10");
                return false;
            }
        } else {
            System.out.println("test11");
            return false;
        }*/





        /*if (action != ClickAction.SECONDARY) return false;

        ItemStack itemInSlot = slot.getItem();
        if (itemInSlot.isEmpty()) {
            // Remove the top item from the bundle if secondary-clicking an empty slot
            removeTopItem(bundleStack, slot, player);
        } else {
            // Insert item into the bundle
            insertItem(bundleStack, slot, player);
        }
        return true;*/
    }

    // Handled when you click THIS BUNDLE with AN ITEM
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack bundleStack, ItemStack otherStack, Slot slot, ClickAction action, Player player, SlotAccess slotAccess) {
        System.out.println("I clicked on bundle with this item!  otherstack: " + otherStack + "  slot: " + slot.getItem() + "   bundleStack: " + bundleStack);
        if (!slot.allowModification(player))
            return false;

        if (action == ClickAction.PRIMARY) {
            int amountInserted = tryInsertIntoCustomBundle(bundleStack, otherStack);

            if (!otherStack.isEmpty()) {
                insertItem(bundleStack, otherStack, slot, player);
                otherStack.shrink(amountInserted);
            } else {
                return false;
            }
        } else {
            if (otherStack.isEmpty()) {
                removeTopItemBySlotAccess(bundleStack, slotAccess, player);
            }else {
                // Try to put the held item inside
                return false;
            }
        }

        return true;


        /*if (bundleStack.getCount() != 1) {
            return false;
        } else if (action == ClickAction.PRIMARY && otherStack.isEmpty()) {
            toggleSelectedItem(bundleStack, -1);
            return false;
        } else {
            BundleContents initialContents = (BundleContents)bundleStack.get(DataComponents.BUNDLE_CONTENTS);
            if (initialContents == null) {
                return false;
            } else {
                BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
                if (action == ClickAction.PRIMARY && !otherStack.isEmpty()) {
                    if (slot.allowModification(player) && contents.tryInsert(otherStack) > 0) {
                        playInsertSound(player);
                    } else {
                        playInsertFailSound(player);
                    }

                    bundleStack.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
                    //this.broadcastChangesOnContainerMenu(player);
                    return true;
                } else if (action == ClickAction.SECONDARY && otherStack.isEmpty()) {
                    if (slot.allowModification(player)) {
                        ItemStack removed = contents.removeOne();
                        if (removed != null) {
                            playRemoveOneSound(player);
                            slotAccess.set(removed);
                        } else  {
                            //removeTopItem(bundleStack, slot, player);
                        }
                    }

                    bundleStack.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
                    //this.broadcastChangesOnContainerMenu(player);
                    return true;
                } else {
                    toggleSelectedItem(bundleStack, -1);
                    return false;
                }
            }
        }*/




        /*if (action == ClickAction.PRIMARY && otherStack.isEmpty()) {
            toggleSelectedItem(bundleStack, -1);
            return false;
        }else {
            BundleContents initialContents = (BundleContents)bundleStack.get(DataComponents.BUNDLE_CONTENTS);
            if (initialContents == null)
                return false;

            BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
            if (action == ClickAction.PRIMARY && !otherStack.isEmpty()) {
                if (slot.allowModification(player) && contents.tryInsert(otherStack) > 0) {
                    playInsertSound(player);
                } else {
                    playInsertFailSound(player);
                }

                bundleStack.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
                this.broadcastChangesOnContainerMenu(player);
                return true;
            } else if (action == ClickAction.SECONDARY && otherStack.isEmpty()) {
                if (slot.allowModification(player)) {
                    ItemStack removed = contents.removeOne();
                    if (removed != null) {
                        playRemoveOneSound(player);
                        slotAccess.set(removed);
                    }
                }

                bundleStack.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
                this.broadcastChangesOnContainerMenu(player);
                return true;
            }

            if (otherStack.isEmpty()) {
                // Take an item out if holding nothing and right-clicking the bundle
                removeTopItem(bundleStack, slot, player);
            } else {
                // Try to put the held item inside
                int amountInserted = tryInsertIntoCustomBundle(bundleStack, otherStack);
                if (amountInserted > 0) {
                    otherStack.shrink(amountInserted);
                }
            }
            return true;
        }

        if (action == ClickAction.PRIMARY && other.isEmpty()) {
            toggleSelectedItem(self, -1);
            return false;
        } else {
            BundleContents initialContents = (BundleContents)self.get(DataComponents.BUNDLE_CONTENTS);
            if (initialContents == null) {
                return false;
            } else {
                BundleContents.Mutable contents = new BundleContents.Mutable(initialContents);
                if (action == ClickAction.PRIMARY && !other.isEmpty()) {
                    if (slot.allowModification(player) && contents.tryInsert(other) > 0) {
                        //playInsertSound(player);
                    } else {
                        //playInsertFailSound(player);
                    }

                    self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
                    this.broadcastChangesOnContainerMenu(player);
                    return true;
                } else if (action == ClickAction.SECONDARY && other.isEmpty()) {
                    if (slot.allowModification(player)) {
                        ItemStack removed = contents.removeOne();
                        if (removed != null) {
                            playRemoveOneSound(player);
                            action.set(removed);
                        }
                    }

                    self.set(DataComponents.BUNDLE_CONTENTS, contents.toImmutable());
                    this.broadcastChangesOnContainerMenu(player);
                    return true;
                } else {
                    toggleSelectedItem(self, -1);
                    return false;
                }
            }
        }*/
    }

    private void broadcastChangesOnContainerMenu(Player player) {
        AbstractContainerMenu containerMenu = player.containerMenu;
        if (containerMenu != null) {
            containerMenu.slotsChanged(player.getInventory());
        }

    }

    private static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertFailSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 1.0F, 1.0F);
    }

    private static void playDropContentsSound(Level level, Entity entity) {
        level.playSound((Entity)null, entity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void insertItem(ItemStack bundleStack, ItemStack itemToInsert, Slot slot, Player player) {
        int inserted = tryInsertIntoCustomBundle(bundleStack, itemToInsert);
        if (inserted > 0) {
            slot.safeTake(itemToInsert.getCount(), inserted, player);
        }
    }

    private int tryInsertIntoCustomBundle(ItemStack bundleStack, ItemStack insertStack) {
        if (insertStack.isEmpty() || insertStack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem
                && blockItem.getBlock() instanceof net.minecraft.world.level.block.ShulkerBoxBlock) {
            return 0;
        }

        // Optional security: also block items that have internal container data components
        if (insertStack.has(net.minecraft.core.component.DataComponents.CONTAINER)) {
            return 0;
        }

        BundleContents currentContents = bundleStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        int currentWeight = getBundleWeight(currentContents);
        int itemWeight = getWeight(insertStack);

        if (currentWeight + itemWeight > MAX_CUSTOM_CAPACITY) {
            return 0;
        }

        // Reconstruct the bundle inventory list to append the new item
        List<ItemStack> newItems = new ArrayList<>();
        currentContents.items().forEach(stack -> newItems.add(stack.create()));

        // Find if item already exists to merge stacks, or add as new
        boolean merged = false;
        for (ItemStack existing : newItems) {
            if (ItemStack.isSameItemSameComponents(existing, insertStack)) {
                int transferable = Math.min(insertStack.getCount(), existing.getMaxStackSize() - existing.getCount());
                if (transferable > 0) {
                    existing.grow(transferable);
                    merged = true;
                    break;
                }
            }
        }

        if (!merged) {
            newItems.add(0, insertStack.copyWithCount(insertStack.getCount()));
        }

        // Apply updated list back into Data Components
        List<ItemStackTemplate> templateItems = new ArrayList<>();
        for (ItemStack stack : newItems) {
            if (!stack.isEmpty()) {
                templateItems.add(ItemStackTemplate.fromNonEmptyStack(stack));
            }
        }

        bundleStack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(templateItems));
        return insertStack.getCount();
    }

    private void removeTopItem(ItemStack bundleStack, Slot slot, Player player) {
        BundleContents currentContents = bundleStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        if (currentContents.items().isEmpty())
            return;

        List<ItemStack> newItems = new ArrayList<>();
        currentContents.items().forEach(stack -> newItems.add(stack.create()));

        ItemStack removed = newItems.remove(0); // Pop LIFO top item
        slot.setByPlayer(removed);

        List<ItemStackTemplate> templateItems = new ArrayList<>();
        for (ItemStack stack : newItems) {
            if (!stack.isEmpty()) {
                templateItems.add(ItemStackTemplate.fromNonEmptyStack(stack));
            }
        }

        bundleStack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(templateItems));
    }

    private void removeTopItemBySlotAccess(ItemStack bundleStack, SlotAccess slot, Player player) {
        BundleContents currentContents = bundleStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        if (currentContents.items().isEmpty())
            return;

        List<ItemStack> newItems = new ArrayList<>();
        currentContents.items().forEach(stack -> newItems.add(stack.create()));

        ItemStack removed = newItems.remove(0); // Pop LIFO top item
        slot.set(removed);

        List<ItemStackTemplate> templateItems = new ArrayList<>();
        for (ItemStack stack : newItems) {
            if (!stack.isEmpty()) {
                templateItems.add(ItemStackTemplate.fromNonEmptyStack(stack));
            }
        }

        bundleStack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(templateItems));
    }

    // Helper math mimicking vanilla's weight rules relative to your new MAX
    private int getBundleWeight(BundleContents contents) {
        int total = 0;
        for (ItemStackTemplate stack : contents.items()) {
            total += getWeight(stack.create());
        }
        return total;
    }

    private int getWeight(ItemStack stack) {
        // Unstackable items take a full 64 slots weight. 16-stackable items take 4.
        return (64 / stack.getMaxStackSize()) * stack.getCount();
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemstack) {
        return ItemUseAnimation.BUNDLE;
    }

    @Override
    public void hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
        super.hurtEnemy(itemstack, entity, sourceentity);
        //SoulTunerAction.setItem(entity, itemstack);
    }

    @Override
    public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity, InteractionHand hand) {
        boolean retval = super.onEntitySwing(itemstack, entity, hand);
        //SoulTunerAction.setItem(entity, itemstack);
        return retval;
    }


}
