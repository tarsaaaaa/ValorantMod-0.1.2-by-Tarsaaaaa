package net.tarsa.valorant.agents;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.tarsa.valorant.ValorantMod;
import net.tarsa.valorant.custom.commands.ValGameRules;
import net.tarsa.valorant.util.AgentInfoExt;
import net.tarsa.valorant.util.ClientRegistry;
import net.tarsa.valorant.util.CooldownHandler;
import net.tarsa.valorant.util.SpecialCharactersExt;

import static net.tarsa.valorant.agents.JettServer.summonedBlades;


public class AgentHandler {
    public static void registerAgentStuff(){
        Jett.positionBlades();
        worldSync();
    }
    private static final CooldownHandler cooldownHandler = new CooldownHandler();
    public static void SelectAgent(String agent, PlayerEntity player) {
        NbtCompound nbt = ((AgentInfoExt)player).getAGENT();
        nbt.putString("agent", agent);
        player.sendMessage(Text.of("Selected agent: " + agent), true);
    }

    private static void worldSync(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = client.player;
            if (player == null){
                return;
            }
            if (!summonedBlades.isEmpty() && ValGameRules.getJustKilled()){
                System.out.println("KILL");
                switch (((AgentInfoExt) client.player).getAGENT().getString("agent")) {
                    case "jett" -> Jett.BladeStormKill(client.player);
                }
            }
        });
    }

    public static void LeftClick(PlayerEntity player){
        if (cooldownHandler.isNotOnCooldown("ult") && !summonedBlades.isEmpty()){
            switch (((AgentInfoExt) player).getAGENT().getString("agent")) {
                case "jett" -> Jett.BladeStorm(player);
            }
        }
    }

    public static void FirstAbility(PlayerEntity player){
        if (cooldownHandler.isNotOnCooldown("first")){
            switch (((AgentInfoExt) player).getAGENT().getString("agent")) {
                case "jett" -> {
                    Jett.CloudBurst(player);
                }
            }
        }else {
            player.sendMessage(Text.of("Ability on Cooldown."), true);
        }
    }

    public static void SecondAbility(PlayerEntity player, int intensity){
        if (cooldownHandler.isNotOnCooldown("second")){
            switch (((AgentInfoExt) player).getAGENT().getString("agent")) {
                case "jett" -> Jett.Updraft(player, intensity);
            }
        }else {
            player.sendMessage(Text.of("Ability on Cooldown."), true);
        }
    }

    public static void ThirdAbility(PlayerEntity player, int intensity){
        if (cooldownHandler.isNotOnCooldown("third")){
            switch (((AgentInfoExt) player).getAGENT().getString("agent")) {
                case "jett" -> Jett.TailWind(player, intensity);
            }
        }else {
            player.sendMessage(Text.of("Ability on Cooldown."), true);
        }
    }

    public static void Ult(PlayerEntity player){
        if (cooldownHandler.isNotOnCooldown("ult")){
            switch (((AgentInfoExt) player).getAGENT().getString("agent")) {
                case "jett" -> Jett.BladeStorm(player);
            }
        }else {
            player.sendMessage(Text.of("Ability on Cooldown."), true);
        }
    }
}
