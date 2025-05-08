package net.gigabit101.rebornstorage.packet;

import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class PacketGui {
    private final int page;
    private final BlockPos blockPos;

    public PacketGui(int page, BlockPos blockPos) {
        this.page = page;
        this.blockPos = blockPos;
    }

    public static void encode(PacketGui packetGui, PacketBuffer buf) {
        buf.writeInt(packetGui.page);
        buf.writeBlockPos(packetGui.blockPos);
    }

    public static PacketGui decode(PacketBuffer buf) {
        return new PacketGui(buf.readInt(), buf.readBlockPos());
    }

    public static class Handler {
        public static void handle(final PacketGui message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() ->
            {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null) return;

                TileEntity blockEntity = player.getLevel().getBlockEntity(message.blockPos);
                if (blockEntity != null && blockEntity instanceof BlockEntityMultiCrafter && ((BlockEntityMultiCrafter) blockEntity).getMultiBlock().isAssembled()) {
                    if (message.page > 0) {
                        ((BlockEntityMultiCrafter) blockEntity).getMultiBlock().currentPage = message.page;
                        ((BlockEntityMultiCrafter) blockEntity).setChanged();
                    }
                }
                openGUI(player.getLevel(), player, message.blockPos);
            });
            ctx.get().setPacketHandled(true);
        }

        public static void openGUI(World world, PlayerEntity player, BlockPos blockPos) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) world.getBlockEntity(blockPos), blockPos);
        }
    }
}
