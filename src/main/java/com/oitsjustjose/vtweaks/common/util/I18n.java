/**
 * @author Jose Stovall | oitsjustjose
 * Simple Component Localization tools that hide away the try/catch flow and clean up use overall
 */

package com.oitsjustjose.vtweaks.common.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;

public class I18n {
    public static Component Translate(String key) {
        return Translate(key, "");
    }

    public static Component Translate(String key, Object... replacers) {
        return Translate(key, "", replacers);
    }

    public static Component Translate(String key, String fallback, Object... replacers) {
        var c = new TranslatableContents(key, fallback, replacers);
        try {
            return c.resolve(null, null, 0);
        } catch (CommandSyntaxException e) {
            VTweaks.getInstance().LOGGER.warn(e.getMessage());
            return Component.empty().append(e.getMessage());
        }
    }

    public static Component Resolve(ComponentContents compIn) {
        try {
            return compIn.resolve(null, null, 0);
        } catch (CommandSyntaxException e) {
            VTweaks.getInstance().LOGGER.warn(e.getMessage());
            return Component.empty().append(e.getMessage());
        }
    }
}
