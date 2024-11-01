package net.tarsa.valorant.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tarsa.valorant.agents.AgentHandler;
import net.tarsa.valorant.agents.Jett;

import static net.tarsa.valorant.util.ServerRegistries.*;

public class ClientPacketHandler {
    public static String FileContents;

    public static void registerClientPacketReceiver(){
        /*ClientPlayNetworking.registerGlobalReceiver(SYNC_AGENTS, (client, handler, buf, responseSender) -> {
            int operation = buf.readInt();
            if(operation == 0){
                String agent = buf.readString(32767);
                int ability = buf.readInt();
                client.execute(()->{
                    AgentHandler.UpdateAbilities(agent,ability);
                    ValorantMod.LOGGER.info("Updated abilities for player: " + client.player.getName().getString());
                });
            } else if (operation == 1) {
                String SelectedAgent = buf.readString(32767);
                client.execute(()->{

                    ValorantMod.LOGGER.info("Synced agents for player: " + client.player.getName().getString());
                });
            }
        });*/

        /*ClientPlayNetworking.registerGlobalReceiver(EDIT_AGENTS, (client, handler, buf, responseSender)->{
            String agent = buf.readString(32767);
            String ability = buf.readString(32767);
            int intensity = buf.readInt();
            client.execute(()->{

                ValorantMod.LOGGER.info("Edited agent: " + agent + "'s ability: " + ability + " to intensity: " + intensity);
            });
        });*/

        /*ClientPlayNetworking.registerGlobalReceiver(VALORANT_GAMERULE_SET, ((client, handler, buf, responseSender) -> {
            String gamerule = buf.readString(32767);
            boolean rule = buf.readBoolean();

            client.execute(()->{
                switch (gamerule) {
                    case "doCooldown" -> CooldownHandler.ruleCooldown(rule);
                }
            });
        }));

        ClientPlayNetworking.registerGlobalReceiver(VALORANT_GAMERULE_GET, ((client, handler, buf, responseSender) -> {
            String gamerule = buf.readString(32767);

            client.execute(()->{
                switch (gamerule) {
                    case "doCooldown" -> ClientPacketHandler.sendGamerule(gamerule, CooldownHandler.isDoCooldown());
                }
            });
        }));*/

        ClientPlayNetworking.registerGlobalReceiver(SELECT_AGENTS, (client, handler, buf, responseSender)->{
            String agent = buf.readString(32767);
            if (client.player != null) {
                client.execute(() -> AgentHandler.SelectAgent(agent, client.player));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(BLADESTORM_KILLED, (client, handler, buf, responseSender)->{
            client.execute(()-> Jett.BladeStormKill(client.player));
        });
    }

    /*public static void sendFileWritePacket(String folderName, String fileName, String data, boolean append) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeString(folderName);
        buf.writeString(fileName);
        buf.writeString(data);
        buf.writeBoolean(append);

        ClientPlayNetworking.send(FILE_WRITE_PACKET, buf);
    }*/

    /*public static void sendFileModifyPacket(int lineNumber, String regex, String data,String folderName, String fileName) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeInt(lineNumber);
        buf.writeString(regex);
        buf.writeString(folderName);
        buf.writeString(fileName);
        buf.writeString(data);

        ClientPlayNetworking.send(FILE_MODIFY_PACKET, buf);
    }*/

    public static void summonJettCloudburst(){
        ClientPlayNetworking.send(SUMMON_JETT_CLOUDBURST, PacketByteBufs.empty());
    }

    /*public static void sendGamerule(String gamerule, boolean rule){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(gamerule);
        buf.writeBoolean(rule);

        ClientPlayNetworking.send(VALORANT_GAMERULE_GET, buf);
    }*/

    public static void summonJettBladeStorm(Vec3d offset){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeVec3d(offset);

        ClientPlayNetworking.send(SUMMON_JETT_BLADESTORM, buf);
    }

    public static void setFileContents(String FileContent){
        FileContents = FileContent;
    }
    public static String getFileContent() {
        return FileContents;
    }
}
