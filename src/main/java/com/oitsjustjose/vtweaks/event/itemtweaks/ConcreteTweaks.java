package com.oitsjustjose.vtweaks.event.itemtweaks;

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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConcreteTweaks
{
    @SubscribeEvent
    public void registerTweak(ItemTossEvent event)
    {
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
                    event.getPlayer()
                            .sendStatusMessage(new TextComponentString("Replaced item with EntityItemConcrete"), true);
                    concrete.getEntityWorld().spawnEntity(concrete);
                }
                event.setResult(Event.Result.DENY);
                event.setCanceled(true);
                entItem.setDead();
            }
        }
    }

    /**
     * A Custom EntityItem that replaces concrete powder with concrete when in water
     */
    public static class EntityItemConcrete extends EntityItem
    {

        public EntityItemConcrete(World worldIn, double x, double y, double z, ItemStack stack)
        {
            super(worldIn, x, y, z, stack);
            this.setPickupDelay(40);
        }

        public EntityItemConcrete(EntityItem item)
        {
            super(item.getEntityWorld(), item.posX, item.posY, item.posZ, item.getItem());
            this.motionX = item.motionX;
            this.motionY = item.motionY;
            this.motionZ = item.motionZ;
            this.setPickupDelay(40);
        }

        @Override
        public void onUpdate()
        {
            if (this.inWater)
            {
                this.setItem(new ItemStack(Blocks.CONCRETE, this.getItem().getCount(), this.getItem().getMetadata()));
            }
            super.onUpdate();
        }
    }

    /**
     * Describes custom Dispenser functionality for concrete powder blocks
     */
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
            // Dispensed items have no pickup delay so.....
            entityitem.setNoPickupDelay();
            worldIn.spawnEntity(entityitem);
        }
    };
}