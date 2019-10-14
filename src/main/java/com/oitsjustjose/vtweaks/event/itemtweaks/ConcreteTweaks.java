package com.oitsjustjose.vtweaks.event.itemtweaks;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.config.ItemTweakConfig;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ConcreteTweaks
{
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
    public void registerTweak(ItemTossEvent event)
    {
        if (!ItemTweakConfig.ENABLE_CONCRETE_TWEAKS.get())
        {
            return;
        }
        ItemEntity entItem = event.getEntityItem();
        if (entItem.getItem().getItem() instanceof BlockItem)
        {
            BlockItem itemBlock = (BlockItem) entItem.getItem().getItem();
            if (powderBlocks.contains(itemBlock))
            {
                ItemEntityConcrete concrete = new ItemEntityConcrete(entItem);
                if (!concrete.getEntityWorld().isRemote)
                {
                    concrete.getEntityWorld().addEntity(concrete);
                }
                event.setResult(Event.Result.DENY);
                event.setCanceled(true);
                entItem.remove();
            }
        }
    }

    /**
     * A Custom EntityItem that replaces concrete powder with concrete when in water
     */
    public static class ItemEntityConcrete extends ItemEntity
    {

        public ItemEntityConcrete(World worldIn, double x, double y, double z, ItemStack stack)
        {
            super(worldIn, x, y, z, stack);
            this.setPickupDelay(40);
        }

        public ItemEntityConcrete(ItemEntity item)
        {
            super(item.getEntityWorld(), item.posX, item.posY, item.posZ, item.getItem());
            this.setMotion(item.getMotion());
            this.setPickupDelay(40);
        }

        @Override
        public void tick()
        {
            if (this.inWater)
            {
                int index = powderBlocks.indexOf(this.getItem().getItem());
                this.setItem(new ItemStack(concreteBlocks.get(index), this.getItem().getCount()));
            }
            super.tick();
        }
    }

    /**
     * Describes custom Dispenser functionality for concrete powder blocks
     */
    public static final IDispenseItemBehavior CONCRETE_POWDER_BEHAVIOR_DISPENSE_ITEM = new DefaultDispenseItemBehavior()
    {
        @Override
        protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
        {
            Direction facing = (Direction) source.getBlockState().get(DispenserBlock.FACING);
            IPosition pos = DispenserBlock.getDispensePosition(source);
            ItemStack itemstack = stack.split(1);
            this.dispense(source.getWorld(), itemstack, 6, facing, pos);
            return stack;
        }

        public void dispense(World worldIn, ItemStack stack, int speed, Direction facing, IPosition position)
        {
            double d0 = position.getX();
            double d1 = position.getY();
            double d2 = position.getZ();

            if (facing.getAxis() == Direction.Axis.Y)
            {
                d1 = d1 - 0.125D;
            }
            else
            {
                d1 = d1 - 0.15625D;
            }

            ItemEntity entityitem = null;
            if (stack.getItem() instanceof BlockItem)
            {
                BlockItem itemBlock = (BlockItem) stack.getItem();
                if (powderBlocks.contains(itemBlock))
                {
                    entityitem = new ItemEntityConcrete(worldIn, d0, d1, d2, stack);
                }
            }
            else
            {
                entityitem = new ItemEntity(worldIn, d0, d1, d2, stack);
            }
            double d3 = worldIn.rand.nextDouble() * 0.1D + 0.2D;
            double motX = (double) facing.getXOffset() * d3;
            double motY = 0.20000000298023224D;
            double motZ = (double) facing.getZOffset() * d3;
            motX += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
            motY += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
            motZ += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
            entityitem.setMotion(motX, motY, motZ);

            // Dispensed items have no pickup delay so.....
            entityitem.setNoPickupDelay();
            worldIn.addEntity(entityitem);
        }
    };
}