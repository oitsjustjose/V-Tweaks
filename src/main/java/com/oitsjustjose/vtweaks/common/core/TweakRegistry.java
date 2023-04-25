package com.oitsjustjose.vtweaks.common.core;

import com.google.common.collect.Lists;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class TweakRegistry {

    final List<VTweak> allTweaks;
    final HashMap<String, List<VTweak>> eventTweakMap;

    public TweakRegistry() {
        this.allTweaks = Lists.newArrayList();
        this.eventTweakMap = new HashMap<>();

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

                var instance = constructor.newInstance();
                this.allTweaks.add(instance);
                MinecraftForge.EVENT_BUS.register(instance);
            } catch (ReflectiveOperationException | LinkageError e) {
                LogManager.getLogger().error("Failed to load annotation {}", clsNm, e);
            }
        });
    }

    public List<VTweak> getAllTweaks() {
        return this.allTweaks;
    }
}
