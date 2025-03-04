package com.oitsjustjose.vtweaks.common.tweaks.block;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static com.oitsjustjose.vtweaks.common.util.Constants.MOD_ID;

@Tweak(category = "block")
public class TorchLightingTweak extends VTweak {
    public static final TagKey<Item> TORCH_ITEM = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "ignition_item"));
    public static final TagKey<Block> TORCH_IGNITE_BL = BlockTags.create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "torch_ignition_blacklist"));

    public ModConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        super.registerConfigs(builder);
        this.enabled = builder.comment("Allows the player to re-light any block with the 'lit' blockstate when holding any #vtweaks:ignition_item (item tag). Blacklist blocks using the #vtweaks:torch_ignition_blacklist block tag.").define("enableTorchLighting", true);
    }

    @SubscribeEvent
    public void process(PlayerInteractEvent.RightClickBlock evt) {
        if (!this.enabled.get()) return;

        var player = evt.getEntity();
        var level = evt.getLevel();
        var pos = evt.getPos();
        var state = level.getBlockState(pos);

        if (player.isCrouching()) return;
        if (state.is(TORCH_IGNITE_BL)) return;
        if (!state.hasProperty(BlockStateProperties.LIT)) return;
        if (!player.getMainHandItem().is(TORCH_ITEM)) return;

        if (state.getValue(BlockStateProperties.LIT) == Boolean.FALSE) {
            if (level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 2 | 16)) {
                level.playSound(null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.15F, 1.0F);
                player.swing(InteractionHand.MAIN_HAND);
                evt.setCancellationResult(InteractionResult.CONSUME);
                evt.setCanceled(true);
            }
        }
    }
}
