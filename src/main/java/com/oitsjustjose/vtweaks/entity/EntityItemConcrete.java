package com.oitsjustjose.vtweaks.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityItemConcrete extends EntityItem
{

    public EntityItemConcrete(World worldIn, double x, double y, double z, ItemStack stack)
    {
        super(worldIn, x, y, z, stack);
        this.setPickupDelay(50);
    }

    public EntityItemConcrete(EntityItem item)
    {
        super(item.getEntityWorld(), item.posX, item.posY, item.posZ, item.getItem());
        // this.addVelocity(item.motionX, item.motionY, item.motionZ);
        this.motionX = item.motionX;
        this.motionY = item.motionY;
        this.motionZ = item.motionZ;
        this.setPickupDelay(60);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (this.inWater)
        {
            this.setItem(new ItemStack(Blocks.CONCRETE, this.getItem().getCount(), this.getItem().getMetadata()));
        }
    }
}