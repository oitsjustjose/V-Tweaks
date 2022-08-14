package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CropHelper {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerVanilla(RightClickBlock event) {
        // Checks if feature is enabled
        if (!BlockTweakConfig.ENABLE_CROP_TWEAK.get()) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        Player player = event.getEntity();
        ResourceLocation name = ForgeRegistries.BLOCKS.getKey(state.getBlock());

        for (String blackList : BlockTweakConfig.CROP_TWEAK_BLACKLIST.get()) {
            if (new ResourceLocation(blackList).equals(name)) {
                return;
            }
        }

        if (canHarvest(state)) {
            if (world instanceof ServerLevel) {
                harvest((ServerLevel) world, pos, state, player);
            }
            player.swing(InteractionHand.MAIN_HAND);
            event.setCanceled(true);
        }
    }

    private boolean canHarvest(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof CropBlock) {
            return ((CropBlock) block).isMaxAge(state);
        } else if (block instanceof NetherWartBlock) {
            return state.getValue(NetherWartBlock.AGE) >= 3;
        } else if (block instanceof CocoaBlock) {
            return state.getValue(CocoaBlock.AGE) >= 2;
        }
        return false;
    }

    private void harvest(ServerLevel world, BlockPos pos, BlockState state, Player player) {
        List<ItemStack> drops = Block.getDrops(state, world, pos, null);
        world.destroyBlock(pos, false);
        boolean needPlant = true;
        for (ItemStack stack : drops) {
            if (needPlant && plant(world, pos, stack)) {
                needPlant = false;
            }
            ItemHandlerHelper.giveItemToPlayer(player, stack);
        }
    }

    private boolean plant(ServerLevel world, BlockPos pos, ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            BlockPlaceContext context = new DirectionalPlaceContext(world, pos, Direction.DOWN, stack, Direction.UP);
            return ((BlockItem) item).place(context) == InteractionResult.SUCCESS;
        }
        return false;
    }
}
