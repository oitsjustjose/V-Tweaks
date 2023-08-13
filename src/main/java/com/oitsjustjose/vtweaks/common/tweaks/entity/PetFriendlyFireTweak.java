package com.oitsjustjose.vtweaks.common.tweaks.entity;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Tweak(category = "entity")
public class PetFriendlyFireTweak extends VTweak {
    private ForgeConfigSpec.EnumValue<FFSetting> setting;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.setting = builder.comment("If set to \"OWNER\", this will prevent owners of pets from attacking their own pet. If set to \"ALL\", this prevents all players from attacking anyone's pet").defineEnum("enablePetFriendlyFireTweak", FFSetting.OWNER);
    }

    @SubscribeEvent
    public void process(AttackEntityEvent evt) {
        if (this.setting.get() == FFSetting.DISABLED) return;
        if (evt.getTarget() == null) return;
        if (evt.getEntity() == null) return;
        if (!(evt.getTarget() instanceof TamableAnimal pet)) return;
        if (!pet.isTame()) return;

        boolean applyToAny = this.setting.get() == FFSetting.ALL;
        if (applyToAny || pet.getOwner() == evt.getEntity()) {
            if (evt.isCancelable()) {
                evt.setCanceled(true);
            }
        }
    }

    public enum FFSetting {
        DISABLED, OWNER, ALL
    }
}
