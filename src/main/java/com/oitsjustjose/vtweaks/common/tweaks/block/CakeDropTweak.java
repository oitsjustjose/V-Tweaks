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
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;


@Tweak(eventClass = BlockEvent.BreakEvent.class, category = "block")
public class CakeDropTweak extends VTweak {
    public ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("Allows for uneaten cake to drop on break").define("enableCakeDrop", true);
    }

    @Override
    public void process(Event event) {
        if (!this.enabled.get()) return;

        var evt = (BlockEvent.BreakEvent) event;
        if (evt.getPlayer() == null) return;
        if (evt.getPlayer().isCreative()) return;

        var state = evt.getState();

        if (state.is(BlockTags.CANDLE_CAKES)) {
            dropCake(evt.getPlayer().getLevel(), evt.getPos());
            return;
        }

        if (state.hasProperty(BlockStateProperties.BITES)) {
            var bites = state.getValue(BlockStateProperties.BITES);
            if (bites == 0) {
                dropCake(evt.getPlayer().getLevel(), evt.getPos());
            }
        }

    }

    private void dropCake(Level level, BlockPos pos) {
        var cakeItem = new ItemEntity(level, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, new ItemStack(Items.CAKE));
        cakeItem.setPickUpDelay(10);
        level.addFreshEntity(cakeItem);
    }
}