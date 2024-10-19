package net.tarsa.valorant.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.tarsa.valorant.agents.JettServer;
import org.jetbrains.annotations.NotNull;

import static net.tarsa.valorant.util.ServerRegistries.*;

public class ServerPacketHandler {
    public static void registerServerPacketReceiver(){
        ServerPlayNetworking.registerGlobalReceiver(FILE_WRITE_PACKET, (server, player, handler, buf, responseSender) -> {
            String folderName = buf.readString(32767);
            String fileName = buf.readString(32767);
            String data = buf.readString(32767);
            boolean append = buf.readBoolean();

            server.execute(() -> FileHandler.writeFile(data, folderName, fileName, append,server));
        });

        ServerPlayNetworking.registerGlobalReceiver(FILE_MODIFY_PACKET, (server, player, handler, buf, responseSender) -> {
            int lineNumber = buf.readInt();
            String regex = buf.readString(32767);
            String folderName = buf.readString(32767);
            String fileName = buf.readString(32767);
            String data = buf.readString(32767);

            server.execute(() -> FileHandler.modifyFile(lineNumber,regex,data,folderName,fileName,server));
        });

        ServerPlayNetworking.registerGlobalReceiver(SUMMON_JETT_CLOUDBURST, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                JettServer.summonCloudBurst(player);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(SUMMON_JETT_BLADESTORM, (server, player, handler, buf, responseSender) -> {
            Vec3d offset = buf.readVec3d();

            server.execute(() -> {
                JettServer.summonBladeStorm(player,offset);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(VALORANT_GAMERULE_GET, ((server, player, handler, buf, responseSender) -> {
            String gamerule = buf.readString(32767);
            boolean rule = buf.readBoolean();

            server.execute(()->{
                ValorantCommands.showRule(player,gamerule,rule);
            });
        }));
    }

    public static void sendAgentEditPacket(ServerPlayerEntity player, String agent, String ability,int intensity){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(agent);
        buf.writeString(ability);
        buf.writeInt(intensity);
        ServerPlayNetworking.send(player, EDIT_AGENTS, buf);
    }

    public static void sendAgentSelectPacket(ServerPlayerEntity player, String agent){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(agent);

        ServerPlayNetworking.send(player, SELECT_AGENTS, buf);
    }

    public static void sendValorantGameRulePacket(ServerPlayerEntity player, String gamerule, Boolean rule){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(gamerule);
        buf.writeBoolean(rule);

        ServerPlayNetworking.send(player, VALORANT_GAMERULE_SET, buf);
    }

    public static void getValorantGameRulePacket(ServerPlayerEntity player, String gamerule){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(gamerule);

        ServerPlayNetworking.send(player, VALORANT_GAMERULE_GET, buf);
    }

    public static void sendAgentSyncPacket(ServerPlayerEntity player, String SelectedAgent, String agent, int ability, int point,@NotNull int operation){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(operation);
        if (operation == 0) {
            buf.writeString(agent);
            buf.writeInt(ability);
            buf.writeInt(point);
        } else if (operation == 1) {
            buf.writeString(SelectedAgent);
        }
        ServerPlayNetworking.send(player, SYNC_AGENTS, buf);
    }
}
