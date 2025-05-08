package net.gigabit101.rebornstorage.init;

import net.gigabit101.rebornstorage.blockentities.BlockEntityAdvancedWirelessTransmitter;
import net.gigabit101.rebornstorage.blocks.BlockAdvancedWirelessTransmitter;
import net.gigabit101.rebornstorage.blocks.BlockMultiCrafter;
import net.gigabit101.rebornstorage.Constants;
import net.gigabit101.rebornstorage.blockentities.BlockEntityMultiCrafter;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILES_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Constants.MOD_ID);

    public static final RegistryObject<Block> BLOCK_MULTI_FRAME = BLOCKS.register("multiblock_frame", BlockMultiCrafter::new);
    public static final RegistryObject<Block> BLOCK_MULTI_HEAT = BLOCKS.register("multiblock_heat", BlockMultiCrafter::new);
    public static final RegistryObject<Block> BLOCK_MULTI_CPU = BLOCKS.register("multiblock_cpu", BlockMultiCrafter::new);
    public static final RegistryObject<Block> BLOCK_MULTI_STORAGE = BLOCKS.register("multiblock_storage", BlockMultiCrafter::new);

    public static final RegistryObject<Block> BLOCK_ADVANCED_WIRELESS_TRANSMITTER = BLOCKS.register("advanced_wireless_transmitter", BlockAdvancedWirelessTransmitter::new);

    public static final RegistryObject<TileEntityType<BlockEntityMultiCrafter>> CRAFTER_TILE = TILES_ENTITIES.register("multiblock_frame", () -> TileEntityType.Builder.of(BlockEntityMultiCrafter::new, ModBlocks.BLOCK_MULTI_FRAME.get(), ModBlocks.BLOCK_MULTI_STORAGE.get(), ModBlocks.BLOCK_MULTI_CPU.get(), ModBlocks.BLOCK_MULTI_HEAT.get()).build(null));
    public static final RegistryObject<TileEntityType<BlockEntityAdvancedWirelessTransmitter>> ADVANCED_WIRELESS_TRANSMITTER = TILES_ENTITIES.register("advanced_wireless_transmitter", () -> TileEntityType.Builder.of(BlockEntityAdvancedWirelessTransmitter::new, ModBlocks.BLOCK_ADVANCED_WIRELESS_TRANSMITTER.get()).build(null));
}
