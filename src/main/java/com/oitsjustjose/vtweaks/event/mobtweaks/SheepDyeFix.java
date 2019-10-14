// package com.oitsjustjose.vtweaks.event.mobtweaks;

// import com.oitsjustjose.vtweaks.util.ModConfig;

// import net.minecraft.entity.passive.EntitySheep;
// import net.minecraft.entity.player.EntityPlayer;
// import net.minecraft.init.Items;
// import net.minecraft.item.EnumDyeColor;
// import net.minecraft.item.Item;
// import net.minecraft.item.ItemStack;
// import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
// import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
// import net.minecraftforge.oredict.OreDictionary;

// public class SheepDyeFix
// {
//     @SubscribeEvent
//     public void registerEvent(EntityInteract event)
//     {
//         // Checks if feature is enabled
//         if (!ModConfig.mobTweaks.sheepDyeFix.enabled)
//         {
//             return;
//         }
//         // Checks that the entity is a sheep
//         if (event.getTarget() == null || !(event.getTarget() instanceof EntitySheep))
//         {
//             return;
//         }

//         EntitySheep sheep = (EntitySheep) event.getTarget();
//         EntityPlayer player = event.getEntityPlayer();

//         if (!sheep.isChild() && !sheep.getSheared())
//         {
//             if (!player.getHeldItemMainhand().isEmpty())
//             {
//                 int dyeColor = getDye(player.getHeldItemMainhand());

//                 if (dyeColor == -1)
//                 {
//                     return;
//                 }

//                 if (sheep.getFleeceColor() != EnumDyeColor.byDyeDamage(dyeColor))
//                 {
//                     sheep.setFleeceColor(EnumDyeColor.byDyeDamage(dyeColor));
//                     player.getHeldItemMainhand().shrink(1);
//                 }
//             }
//         }
//     }

//     private int getDye(ItemStack itemstack)
//     {
//         // Checks if it's a blacklisted dye class first
//         if (ignore(itemstack))
//         {
//             return -1;
//         }

//         // Otherwise continues to find the proper value
//         int[] ids = OreDictionary.getOreIDs(itemstack);
//         for (int id : ids)
//         {
//             String name = OreDictionary.getOreName(id);
//             for (int meta = 0; meta < EnumDyeColor.values().length; meta++)
//             {
//                 int[] dyeIDs = OreDictionary.getOreIDs(new ItemStack(Items.DYE, 1, meta));
//                 for (int dyeID : dyeIDs)
//                 {
//                     if (name.equalsIgnoreCase(OreDictionary.getOreName(dyeID)))
//                     {
//                         return meta;
//                     }
//                 }
//             }
//         }
//         return -1;
//     }

//     private boolean ignore(ItemStack itemstack)
//     {
//         Item stackItem = itemstack.getItem();
//         for (String s : ModConfig.mobTweaks.sheepDyeFix.blacklist)
//         {
//             if (stackItem.getClass().getName().contains(s))
//             {
//                 return true;
//             }
//         }

//         return false;
//     }
// }