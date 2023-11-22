package com.github.theredbrain.bamcore.client.owo;

import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.util.NinePatchTexture;
import net.minecraft.util.Identifier;

public interface ExtendedSurface extends Surface {

    static Surface customPanel(Identifier identifier) {
        return (context, component) -> NinePatchTexture.draw(identifier, context, component);
    }
}
