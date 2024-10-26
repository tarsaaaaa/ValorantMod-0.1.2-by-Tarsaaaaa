package net.tarsa.valorant.agents;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.tarsa.valorant.custom.entities.JettKnifeEntity;
import net.tarsa.valorant.util.ClientPacketHandler;
import net.tarsa.valorant.util.CooldownHandler;
import net.tarsa.valorant.util.SpecialCharactersExt;
import org.jetbrains.annotations.NotNull;

import static net.tarsa.valorant.agents.JettServer.summonedBlades;

public class Jett {
    private static final CooldownHandler cooldownHandler = new CooldownHandler();
    public static boolean TailWindActive = false;
    public static boolean BladesSummoned = false;
    public static long TailWindTimer;
    public static final long TailWindDuration = 7500;

    public static void CloudBurst(@NotNull PlayerEntity player){
        ClientPacketHandler.summonJettCloudburst();
        JettSounds.CloudBurstSound(player);
        cooldownHandler.setCooldown("first", 5000);
    }
    public static void Updraft(@NotNull PlayerEntity player, int intensity){
        player.addVelocity(new Vec3d(0d,1d,0d).multiply(intensity));
        JettSounds.UpdraftSound(player);
        cooldownHandler.setCooldown("second", 5000);
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
            cooldownHandler.setCooldown("third", 5000);
        }
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

    public static void BladeStormKill(PlayerEntity player){
        if (BladesSummoned) {
            for (JettKnifeEntity knife:summonedBlades) {
                knife.discard();
                System.out.println("DISKARDEAD");
            }
            summonProjectilesAroundPlayer(player);
        }
    }

    private static void summonProjectilesAroundPlayer(PlayerEntity player) {
        summonedBlades.clear();
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
        ((SpecialCharactersExt) player).setIfKilled(false);
        player.sendMessage(Text.literal("Projectiles summoned!"), false);
    }

    private static void launchProjectileTowardsCrosshair(PlayerEntity player) {
        if (!summonedBlades.isEmpty()) {
            JettKnifeEntity blades = summonedBlades.remove(0);

            Vec3d playerPos = player.getPos();
            Vec3d lookDirection = player.getRotationVec(1.0F);
            blades.setPos(playerPos.x,playerPos.y + 1.5,playerPos.z);
            blades.setVelocity(lookDirection.x, lookDirection.y, lookDirection.z, 10.0F, 0.0F);

            if (summonedBlades.isEmpty()) {
                BladesSummoned = false;
                cooldownHandler.setCooldown("ult", 5000);
            }
        }
    }

    public static void positionBlades(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!summonedBlades.isEmpty()) {
                for (JettKnifeEntity knife : summonedBlades) {
                    knife.setNoClip(false);
                    knife.changeLookDirection(-client.player.getYaw(),client.player.getPitch());
                }
            }
            if (!summonedBlades.isEmpty()) {
                if (client.player==null){
                    return;
                }
                float yaw = (float) Math.toRadians(-(client.player.getYaw()));
                float pitch = (float) Math.toRadians(-(client.player.getPitch()));
                double x = Math.sin(yaw) * Math.cos(pitch);
                double y = Math.sin(pitch);
                double z = Math.cos(yaw) * Math.cos(pitch);
                double distanceOffPlayer = 1.5;
                double offsetY = 1.0;
                Vec3d[] offsets = new Vec3d[]{
                        new Vec3d(x, y, z).add(0d,1d,0d),
                        new Vec3d(x, y, z).add(1d,2d,0d),
                        new Vec3d(x, y, z),
                        new Vec3d(x, y, z),
                        new Vec3d(x, y, z),
                };
                Vec3d[] bladePos = {client.player.getPos().add(offsets[0]),
                        client.player.getPos().add(offsets[1]),
                        client.player.getPos().add(offsets[2]),
                        client.player.getPos().add(offsets[3]),
                        client.player.getPos().add(offsets[4])
                };
                int a = summonedBlades.size() - 1;
                if (a>=0) {
                    summonedBlades.get(a).applyRotation(yaw,pitch);
                    summonedBlades.get(a).updatePosition(bladePos[0].x,bladePos[0].y,bladePos[0].z);
                }
                if (a-1>=0) {
                    double offsetX = distanceOffPlayer * Math.sin(yaw);
                    double offsetZ = distanceOffPlayer * Math.cos(yaw);
                    summonedBlades.get(a - 1).updatePosition(client.player.getX() + offsetX, client.player.getY() + offsetY, client.player.getZ() + offsetZ);
                }
            }
        });
    }

}
