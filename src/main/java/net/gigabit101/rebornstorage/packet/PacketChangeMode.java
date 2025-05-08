package net.gigabit101.rebornstorage.packet;

import com.refinedmods.refinedstorage.integration.curios.CuriosIntegration;
import net.gigabit101.rebornstorage.init.ModItems;
import net.gigabit101.rebornstorage.items.ItemWirelessGrid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class PacketChangeMode {
    public PacketChangeMode() {
    }

    public static void encode(PacketChangeMode packetGui, PacketBuffer buf) {
    }

    public static PacketChangeMode decode(PacketBuffer buf) {
        return new PacketChangeMode();
    }

    public static class Handler {
        public static void handle(final PacketChangeMode message, Supplier<NetworkEvent.Context> ctx) {
            Set<Item> validItems = new HashSet(Arrays.asList(ModItems.WIRELESS_GRID.get(), ModItems.CREATIVE_WIRELESS_GRID.get()));

            ctx.get().enqueueWork(() ->
            {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null) return;
                PlayerInventory inv = player.inventory;
                int slotFound = -1;

                //Loop the players inventory looking for our item
                for (int i = 0; i < inv.getContainerSize(); ++i) {
                    ItemStack slot = inv.getItem(i);
                    if (validItems.contains(slot.getItem())) {
                        if (slotFound != -1) {
                            return;
                        }
                        slotFound = i;
                    }
                }

                //If we don't find our stack and Curio is loaded look in the curio slots
                if (CuriosIntegration.isLoaded() && slotFound == -1) {
                    Optional<ImmutableTriple<String, Integer, ItemStack>> curio = CuriosApi.getCuriosHelper().findEquippedCurio((stack) -> validItems.contains(stack.getItem()), player);
                    if (curio.isPresent()) {
                        //if we find our stack update its nbt/mode
                        updateStack(curio.get().getRight(), player);
                        return;
                    }
                }
                if (slotFound != -1) {
                    //If we find our stack before Curio update this stack
                    updateStack(player.inventory.getItem(slotFound), player);
                }

            });
            ctx.get().setPacketHandled(true);
        }
    }

    public static void updateStack(ItemStack stack, PlayerEntity player) {
        if (player.level.isClientSide)
            return;
        if (stack.getItem() instanceof ItemWirelessGrid) {
            ItemWirelessGrid itemWirelessGrid = (ItemWirelessGrid) stack.getItem();
            ItemWirelessGrid.MODE current = itemWirelessGrid.getMode(stack);
            switch (current) {
                case CRAFTING:
                    itemWirelessGrid.setMode(stack, ItemWirelessGrid.MODE.FLUID);
                    player.sendMessage(new StringTextComponent("MODE: " + ItemWirelessGrid.MODE.FLUID.name()).withStyle(TextFormatting.GOLD), Util.NIL_UUID);
                    break;
                case FLUID:
                    itemWirelessGrid.setMode(stack, ItemWirelessGrid.MODE.MONITOR);
                    player.sendMessage(new StringTextComponent("MODE: " + ItemWirelessGrid.MODE.MONITOR.name()).withStyle(TextFormatting.GOLD), Util.NIL_UUID);
                    break;
                case MONITOR:
                    itemWirelessGrid.setMode(stack, ItemWirelessGrid.MODE.CRAFTING);
                    player.sendMessage(new StringTextComponent("MODE: " + ItemWirelessGrid.MODE.CRAFTING.name()).withStyle(TextFormatting.GOLD), Util.NIL_UUID);
                    break;
            }
        }
    }
}
