package com.oitsjustjose.vtweaks.common.registry;

import com.oitsjustjose.vtweaks.common.enchantment.ImperishableEnchantment;
import com.oitsjustjose.vtweaks.common.enchantment.LumberingEnchantment;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentRegistrator {
    public final DeferredRegister<Enchantment> REGISTRATOR = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Constants.MODID);

    public final LumberingEnchantment lumbering;
    public final ImperishableEnchantment imperishable;

    public EnchantmentRegistrator() {
        this.lumbering = new LumberingEnchantment();
        this.REGISTRATOR.register("lumbering", () -> this.lumbering);
        this.imperishable = new ImperishableEnchantment();
        this.REGISTRATOR.register("imperishable", () -> this.imperishable);
    }
}
