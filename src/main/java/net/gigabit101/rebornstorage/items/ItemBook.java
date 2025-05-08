package net.gigabit101.rebornstorage.items;

import net.gigabit101.rebornstorage.client.CreativeTabRebornStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;

public class ItemBook extends Item {
    public ItemBook() {
        super(new Properties().tab(CreativeTabRebornStorage.INSTANCE).stacksTo(1));
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player instanceof ServerPlayerEntity) {
            PatchouliAPI.get().openBookGUI((ServerPlayerEntity) player, Registry.ITEM.getKey(this));
        }
        return ActionResult.success(stack);
    }
}
