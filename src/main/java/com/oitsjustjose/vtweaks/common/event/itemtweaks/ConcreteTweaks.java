package com.oitsjustjose.vtweaks.common.event.itemtweaks;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.common.config.ItemTweakConfig;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class ConcreteTweaks {
    public static ArrayList<Item> concreteBlocks = Lists.newArrayList(Blocks.WHITE_CONCRETE.asItem(),
            Blocks.LIGHT_GRAY_CONCRETE.asItem(), Blocks.GRAY_CONCRETE.asItem(), Blocks.BLACK_CONCRETE.asItem(),
            Blocks.RED_CONCRETE.asItem(), Blocks.ORANGE_CONCRETE.asItem(), Blocks.YELLOW_CONCRETE.asItem(),
            Blocks.LIME_CONCRETE.asItem(), Blocks.GREEN_CONCRETE.asItem(), Blocks.LIGHT_BLUE_CONCRETE.asItem(),
            Blocks.CYAN_CONCRETE.asItem(), Blocks.BLUE_CONCRETE.asItem(), Blocks.PURPLE_CONCRETE.asItem(),
            Blocks.MAGENTA_CONCRETE.asItem(), Blocks.PINK_CONCRETE.asItem(), Blocks.BROWN_CONCRETE.asItem());

    public static ArrayList<Item> powderBlocks = Lists.newArrayList(Blocks.WHITE_CONCRETE_POWDER.asItem(),
            Blocks.LIGHT_GRAY_CONCRETE_POWDER.asItem(), Blocks.GRAY_CONCRETE_POWDER.asItem(),
            Blocks.BLACK_CONCRETE_POWDER.asItem(), Blocks.RED_CONCRETE_POWDER.asItem(),
            Blocks.ORANGE_CONCRETE_POWDER.asItem(), Blocks.YELLOW_CONCRETE_POWDER.asItem(),
            Blocks.LIME_CONCRETE_POWDER.asItem(), Blocks.GREEN_CONCRETE_POWDER.asItem(),
            Blocks.LIGHT_BLUE_CONCRETE_POWDER.asItem(), Blocks.CYAN_CONCRETE_POWDER.asItem(),
            Blocks.BLUE_CONCRETE_POWDER.asItem(), Blocks.PURPLE_CONCRETE_POWDER.asItem(),
            Blocks.MAGENTA_CONCRETE_POWDER.asItem(), Blocks.PINK_CONCRETE_POWDER.asItem(),
            Blocks.BROWN_CONCRETE_POWDER.asItem());

    @SubscribeEvent
    public void registerTweak(ItemTossEvent event) {
        if (!ItemTweakConfig.ENABLE_CONCRETE_TWEAKS.get()) {
            return;
        }
        ItemEntity entItem = event.getEntity();
        if (entItem.getItem().getItem() instanceof BlockItem itemBlock) {
            if (powderBlocks.contains(itemBlock)) {
                ItemEntityConcrete concrete = new ItemEntityConcrete(entItem);
                if (!concrete.level.isClientSide()) {
                    concrete.level.addFreshEntity(concrete);
                }
                event.setResult(Event.Result.DENY);
                event.setCanceled(true);
                entItem.discard();
            }
        }
    }

    /**
     * A Custom EntityItem that replaces concrete powder with concrete when in water
     */
    public static class ItemEntityConcrete extends ItemEntity {

        public ItemEntityConcrete(Level worldIn, double x, double y, double z, ItemStack stack) {
            super(worldIn, x, y, z, stack);
            this.setPickUpDelay(40);
        }

        public ItemEntityConcrete(ItemEntity item) {
            super(item.level, item.getX(), item.getY(), item.getZ(), item.getItem());
            this.setDeltaMovement(item.getDeltaMovement());
            this.setPickUpDelay(40);
        }

        @Override
        public void tick() {
            if (this.isInWater()) {
                String target = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.getItem().getItem())).toString().replace("_powder", "");
                Item replacement = ForgeRegistries.ITEMS.getValue(new ResourceLocation(target));
                if (replacement != null) {
                    this.setItem(new ItemStack(replacement, this.getItem().getCount()));
                }
            }
            super.tick();
        }
    }

    /**
     * Describes custom Dispenser functionality for concrete powder blocks
     */
    public static final DispenseItemBehavior CONCRETE_POWDER_BEHAVIOR_DISPENSE_ITEM = new DefaultDispenseItemBehavior() {
        @Override
        public @NotNull ItemStack execute(BlockSource source, ItemStack stack) {
            Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
            Position pos = DispenserBlock.getDispensePosition(source);
            ItemStack itemstack = stack.split(1);
            this.dispense(source.getLevel(), itemstack, 6, facing, pos);
            return stack;
        }

        public void dispense(Level worldIn, ItemStack stack, int speed, Direction facing, Position position) {
            double d0 = position.x();
            double d1 = position.y();
            double d2 = position.z();

            if (facing.getAxis() == Direction.Axis.Y) {
                d1 = d1 - 0.125D;
            } else {
                d1 = d1 - 0.15625D;
            }

            ItemEntity entityitem = null;
            if (stack.getItem() instanceof BlockItem itemBlock) {
                if (powderBlocks.contains(itemBlock)) {
                    entityitem = new ItemEntityConcrete(worldIn, d0, d1, d2, stack);
                }
            } else {
                entityitem = new ItemEntity(worldIn, d0, d1, d2, stack);
            }
            double d3 = worldIn.getRandom().nextDouble() * 0.1D + 0.2D;
            double motX = (double) facing.getStepX() * d3;
            double motY = 0.20000000298023224D;
            double motZ = (double) facing.getStepZ() * d3;
            motX += worldIn.getRandom().nextGaussian() * 0.007499999832361937D * (double) speed;
            motY += worldIn.getRandom().nextGaussian() * 0.007499999832361937D * (double) speed;
            motZ += worldIn.getRandom().nextGaussian() * 0.007499999832361937D * (double) speed;
            assert entityitem != null;
            entityitem.setDeltaMovement(motX, motY, motZ);

            // Dispensed items have no pickup delay so.....
            entityitem.setNoPickUpDelay();
            worldIn.addFreshEntity(entityitem);
        }
    };
}