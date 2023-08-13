package com.oitsjustjose.vtweaks.common.tweaks.block;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.oitsjustjose.vtweaks.common.util.Constants.MOD_ID;

@Tweak(category = "block")
public class CropInteractTweak extends VTweak {
    public static final TagKey<Block> CROP_BLACKLIST = BlockTags.create(new ResourceLocation(MOD_ID, "blacklisted_harvest_crops"));

    public ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment().define("enableCropTweaks", true);
    }

    @SubscribeEvent
    public void process(PlayerInteractEvent.RightClickBlock evt) {
        if (!this.enabled.get()) return;
        if (evt.getHand() != InteractionHand.MAIN_HAND) return;

        var pos = evt.getPos();
        var state = evt.getLevel().getBlockState(pos);
        var player = evt.getEntity();

        if (state.is(CROP_BLACKLIST)) return;
        if (!canHarvest(state)) return;

        if (evt.getLevel() instanceof ServerLevel serverLvl) {
            harvest(serverLvl, pos, state, player);
        }
        player.swing(InteractionHand.MAIN_HAND);
        evt.setCanceled(true);
    }

    private boolean canHarvest(BlockState state) {
        if (state.getBlock() instanceof CropBlock crop) return crop.isMaxAge(state);

        if (state.hasProperty(BlockStateProperties.AGE_1)) {
            return state.getValue(BlockStateProperties.AGE_1) == BlockStateProperties.MAX_AGE_1;
        }
        if (state.hasProperty(BlockStateProperties.AGE_2)) {
            return state.getValue(BlockStateProperties.AGE_2) == BlockStateProperties.MAX_AGE_2;
        }
        if (state.hasProperty(BlockStateProperties.AGE_3)) {
            return state.getValue(BlockStateProperties.AGE_3) == BlockStateProperties.MAX_AGE_3;
        }
        if (state.hasProperty(BlockStateProperties.AGE_4)) {
            return state.getValue(BlockStateProperties.AGE_4) == BlockStateProperties.MAX_AGE_4;
        }
        if (state.hasProperty(BlockStateProperties.AGE_5)) {
            return state.getValue(BlockStateProperties.AGE_5) == BlockStateProperties.MAX_AGE_5;
        }
        if (state.hasProperty(BlockStateProperties.AGE_7)) {
            return state.getValue(BlockStateProperties.AGE_7) == BlockStateProperties.MAX_AGE_7;
        }
        if (state.hasProperty(BlockStateProperties.AGE_15)) {
            return state.getValue(BlockStateProperties.AGE_15) == BlockStateProperties.MAX_AGE_15;
        }
        if (state.hasProperty(BlockStateProperties.AGE_25)) {
            return state.getValue(BlockStateProperties.AGE_25) == BlockStateProperties.MAX_AGE_25;
        }
        return false;
    }

    private void harvest(ServerLevel level, BlockPos pos, BlockState state, Player player) {
        /* Get the drops first, then destroy the block, then try re-place */
        var drops = Block.getDrops(state, level, pos, null);
        level.destroyBlock(pos, false);

        var planted = new AtomicBoolean(false);
        drops.forEach(drop -> {
            if (!planted.get() && tryPlant(level, pos, drop)) planted.set(true);
            ItemHandlerHelper.giveItemToPlayer(player, drop);
        });
    }

    private boolean tryPlant(ServerLevel level, BlockPos pos, ItemStack stack) {
        if (stack.getItem() instanceof BlockItem bi) {
            BlockPlaceContext ctx = new DirectionalPlaceContext(level, pos, Direction.DOWN, stack, Direction.UP);
            return bi.place(ctx) == InteractionResult.SUCCESS;
        }
        return false;
    }
}
