package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import java.util.List;

import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class CropHelper {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerVanilla(RightClickBlock event) {
        // Checks if feature is enabled
        if (!BlockTweakConfig.ENABLE_CROP_TWEAK.get()) {
            return;
        }

        if (event.getHand() != Hand.MAIN_HAND) {
            return;
        }

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = event.getPlayer();
        ResourceLocation name = state.getBlock().getRegistryName();

        for (String blackList : BlockTweakConfig.CROP_TWEAK_BLACKLIST.get()) {
            if (new ResourceLocation(blackList).equals(name)) {
                return;
            }
        }

        if (canHarvest(state)) {
            if (world instanceof ServerWorld) {
                harvest((ServerWorld) world, pos, state, player);
            }
            player.swingArm(Hand.MAIN_HAND);
            event.setCanceled(true);
        }
    }

    private boolean canHarvest(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof CropsBlock) {
            return ((CropsBlock) block).isMaxAge(state);
        } else if (block instanceof NetherWartBlock) {
            return state.get(NetherWartBlock.AGE) >= 3;
        } else if (block instanceof CocoaBlock) {
            return state.get(CocoaBlock.AGE) >= 2;
        }
        return false;
    }

    private void harvest(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity player) {
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

    private boolean plant(World world, BlockPos pos, ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            BlockItemUseContext context = new DirectionalPlaceContext(world, pos, Direction.DOWN, stack, Direction.UP);
            return ((BlockItem) item).tryPlace(context) == ActionResultType.SUCCESS;
        }
        return false;
    }
}
