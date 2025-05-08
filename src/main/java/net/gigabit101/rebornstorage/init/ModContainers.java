package net.gigabit101.rebornstorage.init;

import com.refinedmods.refinedstorage.container.factory.PositionalTileContainerFactory;
import net.gigabit101.rebornstorage.blockentities.BlockEntityAdvancedWirelessTransmitter;
import net.gigabit101.rebornstorage.containers.AdvancedWirelessTransmitterContainer;
import net.gigabit101.rebornstorage.containers.ContainerMultiCrafter;
import net.gigabit101.rebornstorage.Constants;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);
    public static final RegistryObject<ContainerType<ContainerMultiCrafter>> MULTI_CRAFTER_CONTAINER = CONTAINERS.register("container_multiblock_crafter", () -> IForgeContainerType.create(ContainerMultiCrafter::new));
    public static final RegistryObject<ContainerType<AdvancedWirelessTransmitterContainer>> ADVANCED_WIRELESS_CONTAINER = CONTAINERS.register("container_advanced_wireless_transmitter",
            () -> IForgeContainerType.create(new PositionalTileContainerFactory<AdvancedWirelessTransmitterContainer, BlockEntityAdvancedWirelessTransmitter>(((i, inventory, wirelessTransmitter)
                    -> new AdvancedWirelessTransmitterContainer(wirelessTransmitter, inventory.player, i)))));
}
