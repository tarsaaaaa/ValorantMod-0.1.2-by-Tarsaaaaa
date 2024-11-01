package net.tarsa.valorant.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.tarsa.valorant.agents.AgentHandler;
import net.tarsa.valorant.clientEvents.KeyBindings;
import net.tarsa.valorant.custom.Entities;
import net.tarsa.valorant.custom.commands.ValGameRules;
import net.tarsa.valorant.screens.Overlays;

public class ClientRegistry {
    public static void RegisterClientStuff(){
        KeyBindings.registerKeyBindings();
        ClientPacketHandler.registerClientPacketReceiver();
        registerPlayerJoin();
        registerRenderers();
        registerOverlays();
        AgentHandler.registerAgentStuff();
    }

    private static void registerOverlays(){
        HudRenderCallback.EVENT.register(Overlays::renderTailWindBar);
        HudRenderCallback.EVENT.register(Overlays::renderCooldowns);
    }

    private static void registerRenderers(){
        EntityRendererRegistry.register(Entities.CLOUDBURSTENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.JETT_KNIFE_ENTITY, BladeStormRenderer::new);
    }

    private static void registerPlayerJoin() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {});
        ValGameRules.setDefaults();
    }

    public static BlockPos getBlockLookingAt(MinecraftClient client, double maxDistance) {
        if (client.player == null || client.world == null) {
            return null;
        }
        Vec3d start = client.player.getCameraPosVec(1.0F);
        Vec3d direction = client.player.getRotationVec(1.0F);
        Vec3d end = start.add(direction.multiply(maxDistance));

        RaycastContext context = new RaycastContext(
                start,
                end,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                client.player
        );
        BlockHitResult result = client.world.raycast(context);
        if (result.getType() == BlockHitResult.Type.BLOCK) {
            return result.getBlockPos();
        }
        return null;
    }
}
