package com.oitsjustjose.vtweaks.common.tweaks.core;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import org.apache.commons.compress.utils.Lists;
import org.objectweb.asm.Type;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class TweakRegistry {

    final List<VTweak> allTweaks;

    public TweakRegistry() {
        this.allTweaks = Lists.newArrayList();

        var type = Type.getType(Tweak.class);
        var scanData = ModList.get().getAllScanData();
        var moduleClassNames = new LinkedHashSet<String>();
        scanData.forEach(s -> s.getAnnotations().forEach(a -> {
            if (Objects.equals(a.annotationType(), type)) {
                moduleClassNames.add(a.memberName());
            }
        }));

        moduleClassNames.forEach(clsNm -> {
            try {
                var cls = Class.forName(clsNm);
                var inst = cls.asSubclass(VTweak.class);
                var constructor = inst.getDeclaredConstructor();
                this.allTweaks.add(constructor.newInstance());
            } catch (ReflectiveOperationException | LinkageError e) {
                VTweaks.getInstance().LOGGER.error("Failed to load annotation {}", clsNm, e);
            }
        });
    }

    public List<VTweak> getAllTweaks() {
        return this.allTweaks;
    }

    @SubscribeEvent
    public void fireEvent(Event event) {
        this.allTweaks.stream().filter(x -> x.isForEvent(event)).distinct().forEach(x -> x.process(event));
    }
}
