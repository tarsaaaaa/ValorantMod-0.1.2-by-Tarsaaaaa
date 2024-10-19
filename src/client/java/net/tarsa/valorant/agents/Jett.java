package net.tarsa.valorant.agents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tarsa.valorant.custom.Entities;
import net.tarsa.valorant.custom.Sounds;
import net.tarsa.valorant.custom.entities.JettKnifeEntity;
import net.tarsa.valorant.util.ClientPacketHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.tarsa.valorant.agents.JettServer.summonedBlades;

public class Jett {
    public static boolean TailWindActive = false;
    private static boolean BladesSummoned = false;
    public static long TailWindTimer;
    public static final long TailWindDuration = 7500;

    int CloudBurstCharges,BladeStormState=0,TailWindCharges,UpdraftCharges,UltPoints;
    public static void CloudBurst(@NotNull PlayerEntity player){
        ClientPacketHandler.summonJettCloudburst();
        JettSounds.CloudBurstSound(player);
    }
    public static void Updraft(@NotNull PlayerEntity player, int intensity){
        player.addVelocity(new Vec3d(0d,1d,0d).multiply(intensity));
        JettSounds.UpdraftSound(player);
    }
    public static void TailWind(@NotNull PlayerEntity player, int intensity){
        if (!TailWindActive) {
            TailWindActive = true;
            JettSounds.TailWindChargeSound(player);
            TailWindTimer = System.currentTimeMillis();
        } else {
            Vec3d CurrentVelocity = player.getVelocity();
            float yaw = (float) Math.toRadians(-(player.getYaw()));
            float pitch = (float) Math.toRadians(-(player.getPitch()));
            double x = Math.sin(yaw) * Math.cos(pitch);
            double y = Math.sin(pitch);
            double z = Math.cos(yaw) * Math.cos(pitch);
            Vec3d StationaryVelocityGround = new Vec3d(x,y,z).multiply(intensity * 2.5f);
            Vec3d StationaryVelocityAir = new Vec3d(x,y,z).multiply(intensity);
            Vec3d MotionVelocityGround = new Vec3d(CurrentVelocity.toVector3f()).multiply(intensity * 25f);
            Vec3d MotionVelocityAir = new Vec3d(CurrentVelocity.toVector3f()).multiply(intensity * 10f);

            JettSounds.TailWindDashSound(player);
            if (CurrentVelocity.x == 0 && CurrentVelocity.z == 0){
                if (player.isOnGround()){
                    player.addVelocity(StationaryVelocityGround.x,StationaryVelocityGround.y, StationaryVelocityGround.z);
                } else {
                    player.addVelocity(StationaryVelocityAir.x,StationaryVelocityAir.y, StationaryVelocityAir.z);
                }
            } else {
                if (player.isOnGround()){
                    player.addVelocity(MotionVelocityGround.x,0d, MotionVelocityGround.z);
                } else {
                    player.addVelocity(MotionVelocityAir.x,0d, MotionVelocityAir.z);
                }
            }
            TailWindActive = false;
        }
    }

    public static void renderTailWindBar(DrawContext context, float tickDelta) {
        if (!TailWindActive) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }

        long elapsedTime = System.currentTimeMillis() - TailWindTimer;
        float progress = 1.0f - (float) elapsedTime / TailWindDuration;
        if (progress < 0) progress = 0;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int barWidth = 100;
        int barHeight = 8;
        int barX = (screenWidth - barWidth) / 2;
        int barY = (screenHeight/2) + barHeight * 10;
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        context.drawCenteredTextWithShadow(renderer, Text.of("Press again to dash"),screenWidth / 2 , barY + barHeight + 4,0xFFFFFFFF);
        context.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF000000);
        int filledWidth = (int) (barWidth * progress);
        context.fill(barX, barY, barX + filledWidth, barY + barHeight, 0xFF64C4E2);
    }

    public static void BladeStorm(PlayerEntity player){
        if (BladesSummoned) {
            JettSounds.BladeStormSingleSound(player);
            launchProjectileTowardsCrosshair(player);
        } else {
            JettSounds.BladeStormSummonSound(player);
            summonProjectilesAroundPlayer(player);
            BladesSummoned = true;
        }
    }

    private static void summonProjectilesAroundPlayer(PlayerEntity player) {
        World world = player.getWorld();

        summonedBlades.clear();

        Vec3d playerPos = player.getPos();
        Vec3d[] offsets = new Vec3d[]{
                new Vec3d(1, 0, 0),  // Right
                new Vec3d(-1, 0, 0), // Left
                new Vec3d(0, 0, 1),  // Front
                new Vec3d(0, 0, -1), // Back
                new Vec3d(0, 2, 0)   // Above
        };

        for (Vec3d offset : offsets) {
            ClientPacketHandler.summonJettBladeStorm(offset);
        }

        player.sendMessage(Text.literal("Projectiles summoned!"), false);
    }

    private static void launchProjectileTowardsCrosshair(PlayerEntity player) {
        if (!summonedBlades.isEmpty()) {
            JettKnifeEntity blades = summonedBlades.remove(0);

            Vec3d playerPos = player.getPos();
            Vec3d lookDirection = player.getRotationVec(1.0F);
            blades.setPos(playerPos.x,playerPos.y + 1.5,playerPos.z);
            blades.setVelocity(lookDirection.x, lookDirection.y, lookDirection.z, 1.0F, 0.0F); // Speed and inaccuracy

            if (summonedBlades.isEmpty()) {
                BladesSummoned = false;
                player.sendMessage(Text.literal("All projectiles launched!"), false);
            }
        }
    }

}