package net.gigabit101.rebornstorage.containers;

import com.refinedmods.refinedstorage.container.BaseContainer;
import net.gigabit101.rebornstorage.blockentities.BlockEntityAdvancedWirelessTransmitter;
import net.gigabit101.rebornstorage.init.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AdvancedWirelessTransmitterContainer extends BaseContainer {
    public AdvancedWirelessTransmitterContainer(@Nullable BlockEntityAdvancedWirelessTransmitter wirelessTransmitter, PlayerEntity player, int windowId) {
        super(ModContainers.ADVANCED_WIRELESS_CONTAINER.get(), wirelessTransmitter, player, windowId);
        for (int i = 0; i < 4; ++i) {
            addSlot(new SlotItemHandler(wirelessTransmitter.getNode().getUpgrades(), i, 187, 6 + (i * 18)));
        }

        addPlayerInventory(8, 55);

        transferManager.addBiTransfer(player.inventory, wirelessTransmitter.getNode().getUpgrades());
    }

    @Nullable
    @Override
    public BlockEntityAdvancedWirelessTransmitter getBlockEntity() {
        return (BlockEntityAdvancedWirelessTransmitter) super.getBlockEntity();
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        return true;
    }
}
