package net.tarsa.valorant.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import net.tarsa.valorant.custom.entities.JettKnifeEntity;

@Environment(EnvType.CLIENT)
public class BladeStormRenderer extends ProjectileEntityRenderer<JettKnifeEntity> {
    public static final Identifier TEXTURE = new Identifier("");

    public BladeStormRenderer(EntityRendererFactory.Context context) {
            super(context);
    }

    @Override
    public Identifier getTexture(JettKnifeEntity entity) {
        return TEXTURE;
    }
}
