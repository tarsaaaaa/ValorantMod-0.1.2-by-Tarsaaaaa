package net.tarsa.valorant.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.tarsa.valorant.ValorantMod;
import net.tarsa.valorant.agents.Jett;
import net.tarsa.valorant.clientEvents.KeyBindings;
import net.tarsa.valorant.custom.Entities;

import java.util.Objects;
import java.util.UUID;

public class ClientRegistry {
    private  static String CURRENT_AGENT;
    public  static int ABILITY_CHARGES_1;
    public  static int ABILITY_CHARGES_2;
    public  static int ABILITY_CHARGES_3;
    public  static int ULT_POINTS;

    public static String defaultPlayerDataFormat(PlayerEntity player, String agent, String[] weapons){
        UUID uuid = player.getUuid();
        String name = player.getName().getString();
        return "[\n\tUUID=" + uuid + "\n\tNAME=" + name + "\n\tAGENT=" + agent + "\n\tWEAPONS:\n\t\tMEELE=" + weapons[0] + "\n\t\tPRIMARY=" + weapons[1] + "\n\t\tSECONDARY=" + weapons[2] + "\n]";
    }

    public static void RegisterClientStuff(){
        KeyBindings.registerKeyBindings();
        ClientPacketHandler.registerClientPacketReceiver();
        EntityRendererRegistry.register(Entities.CLOUDBURSTENTITY, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Entities.JETT_KNIFE_ENTITY, BladeStormRenderer::new);
        registerPlayerJoin();
        HudRenderCallback.EVENT.register(Jett::renderTailWindBar);
    }

    private static void registerPlayerJoin() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            InitializeValorant(client);
            InitializeValorantVariables(client.player);
        });
    }

    private static void InitializeValorant(MinecraftClient client){
        String EXIST = FileHandler.readFile(3,"valorant","pData.txt","NAME",client.getServer());
        if(Objects.isNull(EXIST)){
            ValorantMod.LOGGER.info("Player data not found.");
//            ClientPacketHandler.sendFileWritePacket("valorant","pData.txt",ClientRegistry.defaultPlayerDataFormat(client.player, null, new String[]{null,null,null}),false);
            return;
        } else if (!Objects.equals(EXIST,client.player.getName().getString())){
            ValorantMod.LOGGER.info("Player name doesn't match the data.");
//            ClientPacketHandler.sendFileWritePacket("valorant","pData.txt",ClientRegistry.defaultPlayerDataFormat(client.player, null, new String[]{null,null,null}),false);
            return;
        }
        ValorantMod.LOGGER.info("Loading valorant player data.");
    }

    private static void InitializeValorantVariables(PlayerEntity player){
        CURRENT_AGENT = ((AgentInfoExt) player).getAGENT().getString("agent");
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

    public static void setAgent(AgentInfoExt player, String agent){
        NbtCompound nbt = player.getAGENT();
        nbt.putString("agent", agent);
    }
}
