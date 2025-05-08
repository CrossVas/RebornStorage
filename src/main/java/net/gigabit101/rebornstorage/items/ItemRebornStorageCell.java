package net.gigabit101.rebornstorage.items;

import com.refinedmods.refinedstorage.api.storage.StorageType;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDiskProvider;
import com.refinedmods.refinedstorage.api.storage.disk.StorageDiskSyncData;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.render.Styles;
import net.gigabit101.rebornstorage.client.CreativeTabRebornStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemRebornStorageCell extends Item implements IStorageDiskProvider {
    private static final String NBT_ID = "id";

    private final int capacity;
    private final StorageType storageType;

    public ItemRebornStorageCell(int capacity, StorageType storageType) {
        super(new Item.Properties().tab(CreativeTabRebornStorage.INSTANCE).stacksTo(1));
        this.capacity = capacity;
        this.storageType = storageType;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (!world.isClientSide && !stack.hasTag() && entity instanceof PlayerEntity) {
            UUID id = UUID.randomUUID();
            API.instance().getStorageDiskManager((ServerWorld) world).set(id, API.instance().createDefaultItemDisk((ServerWorld) world, getCapacity(stack), (PlayerEntity) entity));
            API.instance().getStorageDiskManager((ServerWorld) world).markForSaving();
            setId(stack, id);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (isValid(stack)) {
            UUID id = getId(stack);

            API.instance().getStorageDiskSync().sendRequest(id);

            StorageDiskSyncData data = API.instance().getStorageDiskSync().getData(id);

            if (data != null) {
                if (data.getCapacity() == -1) {
                    tooltip.add(new TranslationTextComponent("misc.refinedstorage.storage.stored", API.instance().getQuantityFormatter().format(data.getStored())).setStyle(Styles.GRAY));
                } else {
                    tooltip.add(new TranslationTextComponent("misc.refinedstorage.storage.stored_capacity", API.instance().getQuantityFormatter().format(data.getStored()), API.instance().getQuantityFormatter().format(data.getCapacity())).setStyle(Styles.GRAY));
                }
            }
            if (flag.isAdvanced()) {
                tooltip.add(new StringTextComponent(id.toString()));
            }
        }
    }


    @Override
    public int getEntityLifespan(ItemStack stack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public UUID getId(ItemStack disk) {
        return disk.getTag().getUUID(NBT_ID);
    }

    @Override
    public void setId(ItemStack disk, UUID id) {
        disk.setTag(new CompoundNBT());
        disk.getTag().putUUID(NBT_ID, id);
    }

    @Override
    public boolean isValid(ItemStack disk) {
        return disk.hasTag() && disk.getTag().hasUUID(NBT_ID);
    }

    @Override
    public int getCapacity(ItemStack disk) {
        return this.capacity;
    }

    @Override
    public StorageType getType() {
        return storageType;
    }
}
