package com.oitsjustjose.vtweaks.common.tweaks.block;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.level.BlockEvent;


@Tweak(category = "block")
public class CakeDropTweak extends VTweak {
    public ModConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        this.enabled = builder.comment("Allows for uneaten cake to drop on break").define("enableCakeDrop", true);
    }

    @SubscribeEvent
    public void process(BlockEvent.BreakEvent evt) {
        if (!this.enabled.get()) return;

        if (evt.getPlayer().isCreative()) return;

        var state = evt.getState();

        if (state.is(BlockTags.CANDLE_CAKES)) {
            dropCake(evt.getPlayer().level(), evt.getPos());
            return;
        }

        if (state.hasProperty(BlockStateProperties.BITES)) {
            var bites = state.getValue(BlockStateProperties.BITES);
            if (bites == 0) {
                dropCake(evt.getPlayer().level(), evt.getPos());
            }
        }

    }

    private void dropCake(Level level, BlockPos pos) {
        var cakeItem = new ItemEntity(level, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, new ItemStack(Items.CAKE));
        cakeItem.setPickUpDelay(10);
        level.addFreshEntity(cakeItem);
    }
}