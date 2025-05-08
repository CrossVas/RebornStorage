package net.gigabit101.rebornstorage.items;

import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.item.NetworkItem;
import net.gigabit101.rebornstorage.grid.crafting.WirelessCraftingGridNetworkItem;
import net.gigabit101.rebornstorage.grid.monitor.WirelessCraftingMonitorNetworkItemExt;
import net.gigabit101.rebornstorage.grid.fluid.WirelessFluidGridNetworkItemExt;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ItemWirelessGrid extends NetworkItem {
    public enum Type {
        NORMAL,
        CREATIVE;
    }

    Type type;

    public ItemWirelessGrid(Properties item, Type type, Supplier<Integer> energyCapacity) {
        super(item, type == Type.CREATIVE, energyCapacity);
        this.type = type;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if (player.isCrouching()) {
            ItemStack stack = player.getItemInHand(hand);
            MODE current = getMode(stack);
            switch (current) {
                case CRAFTING:
                    setMode(stack, MODE.FLUID);
                    player.displayClientMessage(new StringTextComponent("MODE: " + MODE.FLUID.name()).withStyle(TextFormatting.GOLD), true);
                    return ActionResult.success(stack);
                case FLUID:
                    setMode(stack, MODE.MONITOR);
                    player.displayClientMessage(new StringTextComponent("MODE: " + MODE.MONITOR.name()).withStyle(TextFormatting.GOLD), true);
                    return ActionResult.success(stack);
                case MONITOR:
                    setMode(stack, MODE.CRAFTING);
                    player.displayClientMessage(new StringTextComponent("MODE: " + MODE.CRAFTING.name()).withStyle(TextFormatting.GOLD), true);
                    return ActionResult.success(stack);
            }
        }
        return super.use(level, player, hand);
    }

    public Type getType() {
        return type;
    }

    public void setMode(ItemStack stack, MODE mode) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putString("mode", mode.name());
    }

    public MODE getMode(ItemStack stack) {
        CompoundNBT compoundTag = stack.getOrCreateTag();
        if (compoundTag.contains("mode")) {
            return MODE.valueOf(compoundTag.getString("mode"));
        }
        return MODE.CRAFTING;
    }

    @Nonnull
    @Override
    public INetworkItem provide(INetworkItemManager iNetworkItemManager, PlayerEntity player, ItemStack itemStack, PlayerSlot playerSlot) {
        switch (getMode(itemStack)) {
            case CRAFTING:
                return new WirelessCraftingGridNetworkItem(iNetworkItemManager, player, itemStack, playerSlot);
            case FLUID:
                return new WirelessFluidGridNetworkItemExt(iNetworkItemManager, player, itemStack, playerSlot);
            case MONITOR:
                return new WirelessCraftingMonitorNetworkItemExt(iNetworkItemManager, player, itemStack, playerSlot);

        }
        return new WirelessCraftingGridNetworkItem(iNetworkItemManager, player, itemStack, playerSlot);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        try {
            tooltip.add(new StringTextComponent("MODE: " + getMode(stack)).withStyle(TextFormatting.GOLD));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum MODE {
        CRAFTING,
        FLUID,
        MONITOR
    }
}
