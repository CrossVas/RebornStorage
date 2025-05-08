package net.gigabit101.rebornstorage.packet;

import net.gigabit101.rebornstorage.Constants;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Constants.MOD_ID, "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

    public static void register() {
        int disc = 0;
        HANDLER.registerMessage(disc++, PacketGui.class, PacketGui::encode, PacketGui::decode, PacketGui.Handler::handle);
        HANDLER.registerMessage(disc++, PacketChangeMode.class, PacketChangeMode::encode, PacketChangeMode::decode, PacketChangeMode.Handler::handle);
    }

    public static void sendTo(Object msg, ServerPlayerEntity player) {
        if (!(player instanceof FakePlayer)) {
            HANDLER.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static void sendToServer(Object msg) {
        HANDLER.sendToServer(msg);
    }
}
