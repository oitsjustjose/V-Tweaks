package com.oitsjustjose.vtweaks.common.core;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import net.minecraft.world.item.DebugStickItem;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.Type;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class TweakRegistry {
    final List<VTweak> clientTweaks;
    final List<VTweak> commonTweaks;

    public TweakRegistry() {
        this.clientTweaks = Lists.newArrayList();
        this.commonTweaks = Lists.newArrayList();

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

                if (instance.getCategory().startsWith(ClientConfig.categoryName)) {
                    clientTweaks.add(instance);
                } else {
                    commonTweaks.add(instance);
                }

                NeoForge.EVENT_BUS.register(instance);
            } catch (ReflectiveOperationException | LinkageError e) {
                LogManager.getLogger().error("Failed to load annotation {}", clsNm, e);
            }
        });

        this.clientTweaks.sort(Comparator.comparing(VTweak::getCategory));
        this.commonTweaks.sort(Comparator.comparing(VTweak::getCategory));
        Debug();
    }

    public List<VTweak> getClientTweaks() {
        return this.clientTweaks;
    }

    public List<VTweak> getCommonTweaks() {
        return this.commonTweaks;
    }

    public void Debug() {
        this.clientTweaks.forEach(this::_forEach);
        this.commonTweaks.forEach(this::_forEach);
    }

    private void _forEach(VTweak tweak) {
        System.out.println(tweak.getCategory());
    }
}
