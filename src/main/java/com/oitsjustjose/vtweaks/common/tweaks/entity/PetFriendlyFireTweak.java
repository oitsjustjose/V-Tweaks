package com.oitsjustjose.vtweaks.common.tweaks.entity;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.world.entity.TamableAnimal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@Tweak(category = "entity")
public class PetFriendlyFireTweak extends VTweak {
    private ModConfigSpec.EnumValue<FFSetting> setting;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        super.registerConfigs(builder);
        this.setting = builder.comment("Prevents pets from being hit by friendly fire.\nOWNER prevents only the owner of the pet from hurting a pet, whereas ALL prevents any player from hurting any pet.\nDISABLED disables the tweak.").defineEnum("enablePetFriendlyFireTweak", FFSetting.OWNER);
    }

    @SubscribeEvent
    public void process(AttackEntityEvent evt) {
        if (this.setting.get() == FFSetting.DISABLED) return;
        if (!(evt.getTarget() instanceof TamableAnimal pet)) return;
        if (!pet.isTame()) return;

        boolean applyToAny = this.setting.get() == FFSetting.ALL;
        if (applyToAny || pet.getOwner() == evt.getEntity()) {
            evt.setCanceled(true);
        }
    }

    public enum FFSetting {
        DISABLED, OWNER, ALL
    }
}
