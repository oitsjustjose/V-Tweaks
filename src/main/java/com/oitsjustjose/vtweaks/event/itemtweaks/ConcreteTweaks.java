package com.oitsjustjose.vtweaks.event.itemtweaks;

import com.oitsjustjose.vtweaks.entity.EntityItemConcrete;
import com.oitsjustjose.vtweaks.util.ModConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConcreteTweaks
{
    @SubscribeEvent
    public void registerTweak(ItemTossEvent event)
    {
        // Checks to see if the despawn time is -1. If it is, items won't despawn, so
        // nothing to do here.
        if (!ModConfig.itemTweaks.enableConreteTweaks)
        {
            return;
        }

        EntityItem entItem = event.getEntityItem();
        if (entItem.getItem().getItem() instanceof ItemBlock)
        {
            ItemBlock itemBlock = (ItemBlock) entItem.getItem().getItem();
            Block block = itemBlock.getBlock();
            if (block == Blocks.CONCRETE_POWDER)
            {
                EntityItemConcrete concrete = new EntityItemConcrete(entItem);
                if (!concrete.getEntityWorld().isRemote)
                {
                    concrete.getEntityWorld().spawnEntity(concrete);
                }
                entItem.setDead();
            }
        }
    }

    public static final IBehaviorDispenseItem CONCRETE_POWDER_BEHAVIOR_DISPENSE_ITEM = new BehaviorDefaultDispenseItem()
    {
        @Override
        protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
        {
            EnumFacing enumfacing = (EnumFacing) source.getBlockState().getValue(BlockDispenser.FACING);
            IPosition iposition = BlockDispenser.getDispensePosition(source);
            ItemStack itemstack = stack.splitStack(1);
            this.doMyDispense(source.getWorld(), itemstack, 6, enumfacing, iposition);
            return stack;
        }

        public void doMyDispense(World worldIn, ItemStack stack, int speed, EnumFacing facing, IPosition position)
        {
            double d0 = position.getX();
            double d1 = position.getY();
            double d2 = position.getZ();

            if (facing.getAxis() == EnumFacing.Axis.Y)
            {
                d1 = d1 - 0.125D;
            }
            else
            {
                d1 = d1 - 0.15625D;
            }

            EntityItem entityitem = null;
            if (stack.getItem() instanceof ItemBlock)
            {
                ItemBlock itemBlock = (ItemBlock) stack.getItem();
                Block block = itemBlock.getBlock();
                if (block == Blocks.CONCRETE_POWDER)
                {
                    entityitem = new EntityItemConcrete(worldIn, d0, d1, d2, stack);
                }
            }
            else
            {
                entityitem = new EntityItem(worldIn, d0, d1, d2, stack);
            }
            double d3 = worldIn.rand.nextDouble() * 0.1D + 0.2D;
            entityitem.motionX = (double) facing.getFrontOffsetX() * d3;
            entityitem.motionY = 0.20000000298023224D;
            entityitem.motionZ = (double) facing.getFrontOffsetZ() * d3;
            entityitem.motionX += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
            entityitem.motionY += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
            entityitem.motionZ += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double) speed;
            worldIn.spawnEntity(entityitem);
        }
    };
}