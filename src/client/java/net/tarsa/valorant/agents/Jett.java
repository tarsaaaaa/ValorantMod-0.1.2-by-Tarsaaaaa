package net.tarsa.valorant.agents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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

    public static void updateOrbitingProjectiles(PlayerEntity player) {
        for (JettKnifeEntity projectile : summonedBlades) {
            double offsetRight = 1.5;
            double offsetY = 1.0;

            float playerYawRadians = player.getYaw() * 0.0174533F;
            double offsetX = -offsetRight * Math.sin(playerYawRadians);
            double offsetZ = offsetRight * Math.cos(playerYawRadians);

            projectile.updatePosition(player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ);
        }
    }

    public void createRotatingAshParticleSphere(World world, BlockPos center, int radius) {
        if (!world.isClient) {
            return; // Particles should only be handled on the client side
        }

        int particleCount = 100; // Total number of particles to simulate the sphere
        double angleIncrement = 2 * Math.PI / particleCount; // Angle increment for each particle
        float rotationSpeed = 0.05f; // Rotation speed (adjust as needed)

        // Use a task scheduler or tick event to continuously rotate the particles
        MinecraftClient.getInstance().execute(() -> {
            for (int tick = 0; tick < 200; tick++) { // Run for 200 ticks (10 seconds)
                final double currentAngle = tick * rotationSpeed; // Current rotation angle

                for (int i = 0; i < particleCount; i++) {
                    // Calculate the spherical coordinates
                    double theta = i * angleIncrement; // Horizontal angle
                    double phi = Math.acos(2 * i / (double) particleCount - 1); // Vertical angle

                    // Convert spherical coordinates to Cartesian coordinates
                    double x = radius * Math.sin(phi) * Math.cos(theta);
                    double y = radius * Math.sin(phi) * Math.sin(theta);
                    double z = radius * Math.cos(phi);

                    // Rotate around the Y-axis
                    double rotatedX = x * Math.cos(currentAngle) - z * Math.sin(currentAngle);
                    double rotatedZ = x * Math.sin(currentAngle) + z * Math.cos(currentAngle);

                    // Spawn the particle at the new position
                    double particleX = center.getX() + 0.5 + rotatedX;
                    double particleY = center.getY() + 0.5 + y;
                    double particleZ = center.getZ() + 0.5 + rotatedZ;

                    world.addParticle(ParticleTypes.ASH, true, particleX, particleY, particleZ, 0.0, 0.0, 0.0);
                }

                // Add a short delay between rotations to simulate smooth animation
                try {
                    Thread.sleep(50); // 50ms delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
