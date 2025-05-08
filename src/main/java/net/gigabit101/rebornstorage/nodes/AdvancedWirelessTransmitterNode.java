package net.gigabit101.rebornstorage.nodes;

import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.api.network.IWirelessTransmitter;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.inventory.item.UpgradeItemHandler;
import com.refinedmods.refinedstorage.inventory.listener.NetworkNodeInventoryListener;
import com.refinedmods.refinedstorage.item.UpgradeItem;
import com.refinedmods.refinedstorage.util.StackUtils;
import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.RebornStorageConfig;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class AdvancedWirelessTransmitterNode extends NetworkNode implements IWirelessTransmitter {
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "advanced_wireless_transmitter");
    private final UpgradeItemHandler upgrades = (UpgradeItemHandler) new UpgradeItemHandler(4, UpgradeItem.Type.RANGE).addListener(new NetworkNodeInventoryListener(this));

    public AdvancedWirelessTransmitterNode(World level, BlockPos pos) {
        super(level, pos);
    }

    @Override
    public int getRange() {
        return RebornStorageConfig.ADVANCED_WIRELESS_TRANSMITTER_RANGE.get() + this.upgrades.getUpgradeCount(UpgradeItem.Type.RANGE) * RebornStorageConfig.ADVANCED_WIRELESS_RAGE_BOOSTER_RANGE.get();
    }

    @Override
    public BlockPos getOrigin() {
        return this.pos;
    }

    @Override
    public RegistryKey<World> getDimension() {
        return this.world.dimension();
    }

    @Override
    public int getEnergyUsage() {
        return RebornStorageConfig.ADVANCED_WIRELESS_TRANSMITTER_POWER_COST.get();
    }

    @Override
    public boolean canConduct(Direction direction) {
        return this.getDirection() == direction;
    }

    @Override
    public void visit(Operator operator) {
        operator.apply(this.world, this.pos.relative(Direction.DOWN), Direction.UP);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        StackUtils.readItems(upgrades, 0, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        StackUtils.writeItems(upgrades, 0, tag);
        return tag;
    }

    public UpgradeItemHandler getUpgrades() {
        return upgrades;
    }

    @Nullable
    @Override
    public IItemHandler getDrops() {
        return getUpgrades();
    }
}
