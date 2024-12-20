package net.tarsa.valorant.agents;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.tarsa.valorant.custom.Entities;
import net.tarsa.valorant.custom.Sounds;
import net.tarsa.valorant.custom.entities.CloudBurstEntity;
import net.tarsa.valorant.custom.entities.JettKnifeEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JettServer {
    public static boolean CloudburstExists = false;
    public static CloudBurstEntity BALL;
    public static List<JettKnifeEntity> summonedBlades = new ArrayList<>();
    public static void summonBladeStorm(ServerPlayerEntity player, Vec3d offset) {
        JettKnifeEntity blades = new JettKnifeEntity(Entities.JETT_KNIFE_ENTITY, player.getWorld());
        Vec3d spawnPosition = player.getPos().add(offset);
        blades.setPosition(spawnPosition.x, spawnPosition.y, spawnPosition.z);
        blades.setOwner(player);

        player.getWorld().spawnEntity(blades);
        summonedBlades.add(blades);
    }

    public static void summonCloudBurst(ServerPlayerEntity player){
        CloudBurstEntity cloudBurst = new CloudBurstEntity(player,player.getWorld());
        cloudBurst.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.5F, 1.0F);
        player.getWorld().spawnEntity(cloudBurst);
        BALL = cloudBurst;
        CloudburstExists = true;
    }

}
class JettSounds{
    public static void CloudBurstSound(PlayerEntity player){
        player.getWorld().playSound(player, player.getPos().x, player.getPos().y, player.getPos().z, Sounds.JETT_CLOUDBURST_SOUND, SoundCategory.PLAYERS, 1f, 1f);
    }
    public static void UpdraftSound(PlayerEntity player){
        player.getWorld().playSound(player, player.getPos().x, player.getPos().y, player.getPos().z, Sounds.JETT_UPDRAFT_SOUND, SoundCategory.PLAYERS, 1f, 1f);
    }
    public static void TailWindChargeSound(PlayerEntity player){
        player.getWorld().playSound(player, player.getPos().x, player.getPos().y, player.getPos().z, Sounds.JETT_TAILWIND_CHARGE_SOUND, SoundCategory.PLAYERS, 1f, 1f);
    }
    public static void TailWindDashSound(PlayerEntity player){
        player.getWorld().playSound(player, player.getPos().x, player.getPos().y, player.getPos().z, Sounds.JETT_TAILWIND_DASH_SOUND, SoundCategory.PLAYERS, 1f, 1f);
    }
    public static void TailWindTimeoutSound(PlayerEntity player){
        player.getWorld().playSound(player, player.getPos().x, player.getPos().y, player.getPos().z, Sounds.JETT_TAILWIND_TIMEOUT_SOUND, SoundCategory.PLAYERS, 1f, 1f);
    }
    public static void BladeStormSummonSound(PlayerEntity player){
        player.getWorld().playSound(player, player.getPos().x, player.getPos().y, player.getPos().z, Sounds.JETT_BLADESTORM_SUMMON_SOUND, SoundCategory.PLAYERS, 1f, 1f);
    }
    public static void BladeStormSingleSound(PlayerEntity player){
        player.getWorld().playSound(player, player.getPos().x, player.getPos().y, player.getPos().z, Sounds.JETT_BLADESTORM_SINGLE_SOUND, SoundCategory.PLAYERS, 1f, 1f);
    }

}
