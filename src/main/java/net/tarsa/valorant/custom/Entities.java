package net.tarsa.valorant.custom;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.tarsa.valorant.ValorantMod;
import net.tarsa.valorant.custom.entities.CloudBurstEntity;
import net.tarsa.valorant.custom.entities.CustomEntity;
import net.tarsa.valorant.custom.entities.JettKnifeEntity;

public class Entities {
    public static final EntityType<CloudBurstEntity> CLOUDBURSTENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ValorantMod.MOD_ID, "cloudburst-entity"),
            FabricEntityTypeBuilder.<CloudBurstEntity>create(SpawnGroup.MISC, CloudBurstEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .build()
    );
    public static final EntityType<JettKnifeEntity> JETT_KNIFE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ValorantMod.MOD_ID, "jett-knife-entity"),
            FabricEntityTypeBuilder.<JettKnifeEntity>create(SpawnGroup.MISC, JettKnifeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(20)
                    .build()
    );

    public static final EntityType<CustomEntity> CUSTOM_ENTITY_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(ValorantMod.MOD_ID, "custom-entity"),
            FabricEntityTypeBuilder.<CustomEntity>create(SpawnGroup.MISC, CustomEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f,0.25f))
                    .build());
}
