package net.tarsa.valorant.agents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.tarsa.valorant.ValorantMod;
import net.tarsa.valorant.mixin.client.AgentInfoMixin;
import net.tarsa.valorant.util.AgentInfoExt;
import net.tarsa.valorant.util.ClientPacketHandler;
import net.tarsa.valorant.util.ClientRegistry;
import net.tarsa.valorant.util.FileHandler;

import static net.tarsa.valorant.util.ClientRegistry.*;
public class AgentHandler {
    private MinecraftClient client = MinecraftClient.getInstance();

    public void EditAgents(String agent, String ability, int intensity){
        
    }

    public static void SelectAgent(String agent, PlayerEntity player){
        ValorantMod.LOGGER.info("Setting agent for " + player.getName().getString() + ":" + agent);
        ToSyncAgentInfo(player, agent);
    }

    public void UpdateAbilities(String agent, int ability){
        switch (ability){
            case 1 -> ABILITY_CHARGES_1++;
            case 2 -> ABILITY_CHARGES_2++;
            case 3 -> ABILITY_CHARGES_3++;
            case 4 -> ULT_POINTS++;
            default -> client.player.sendMessage(Text.of("Invalid argument."));
        }
    }

    public static void ToSyncAgentInfo(PlayerEntity player, String agent){
        ClientRegistry.setAgent((AgentInfoExt) player, agent);
    }

    public static void FirstAbility(PlayerEntity player){
        switch (((AgentInfoExt) player).getAGENT().getString("agent")) {
            case "jett" -> Jett.CloudBurst(player);
        }
    }

    public static void SecondAbility(PlayerEntity player, int intensity){
        switch (((AgentInfoExt) player).getAGENT().getString("agent")) {
            case "jett" -> Jett.Updraft(player, intensity);
        }
    }

    public static void ThirdAbility(PlayerEntity player, int intensity){
        switch (((AgentInfoExt) player).getAGENT().getString("agent")) {
            case "jett" -> Jett.TailWind(player, intensity);
        }
    }

    public static void Ult(PlayerEntity player){
        switch (((AgentInfoExt) player).getAGENT().getString("agent")) {
            case "jett" -> Jett.BladeStorm(player);
        }
    }


}