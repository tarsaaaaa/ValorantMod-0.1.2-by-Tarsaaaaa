package net.tarsa.valorant.custom.entities;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.tarsa.valorant.agents.JettServer;
import net.tarsa.valorant.custom.Entities;
import net.tarsa.valorant.custom.Items;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class CloudBurstEntity extends ThrownItemEntity {
    private Boolean doStraight = false;

    public void setDoStraight(Boolean doStraight) {
        this.doStraight = doStraight;
    }

    public CloudBurstEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public CloudBurstEntity(LivingEntity livingEntity, World world) {
        super(Entities.CLOUDBURSTENTITY, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.CloudBurstItem;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (doStraight==null){
            return;
        }
        if (!this.getWorld().isClient) {
            if (doStraight) {
                System.out.println("straight");
                this.setNoGravity(true);
                if (this.getOwner() != null) {
                    Vec3d lookDirection = getOwner().getRotationVec(1.0F);

                    double newX = lookDirection.x * 0.2;
                    double newZ = lookDirection.z * 0.2;
                    double newY = lookDirection.y * 0.2;

                    this.setVelocity(newX, newY, newZ);
                    this.velocityDirty = true;
                }
                HitResult hitResult = this.checkCollision();

                if (hitResult.getType() != HitResult.Type.MISS) {
                    this.onCollision(hitResult);
                }

                this.move(MovementType.SELF, this.getVelocity());

            } else {
                System.out.println("no straight");
                if (this.getOwner() != null) {
                    Vec3d lookDirection = getOwner().getRotationVec(1.0F);

                    Vec3d currentVelocity = this.getVelocity();

                    double newX = lookDirection.x * 0.2;
                    double newZ = lookDirection.z * 0.2;

                    double newY = currentVelocity.y - 0.01;

                    this.setVelocity(newX, newY, newZ);
                    this.velocityDirty = true;
                }

                HitResult hitResult = this.checkCollision();

                if (hitResult.getType() != HitResult.Type.MISS) {
                    this.onCollision(hitResult);
                }

                this.move(MovementType.SELF, this.getVelocity());
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte)3);
        }

        super.onBlockHit(blockHitResult);
    }

    protected HitResult checkCollision() {

        Vec3d start = this.getPos();
        Vec3d end = start.add(this.getVelocity());

        HitResult blockHitResult = this.getWorld().raycast(new RaycastContext(
                start,
                end,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                this
        ));

        EntityHitResult entityHitResult = this.checkEntityCollision(start, end);

        if (entityHitResult != null) {
            return entityHitResult;
        } else {
            return blockHitResult;
        }
    }

    private EntityHitResult checkEntityCollision(Vec3d start, Vec3d end) {
        Vec3d direction = end.subtract(start);
        Box box = this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D);

        EntityHitResult closestEntityHitResult = ProjectileUtil.raycast(
                this, start, end, box, (entity) -> !entity.isSpectator() && entity.collidedSoftly, direction.lengthSquared());

        return closestEntityHitResult;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        BlockPos impactPosition = new BlockPos((int) this.getX(),(int) this.getY(),(int) this.getZ());
        RegistryKey<World> worldKey = this.getWorld().getRegistryKey();
        createSmoke(Objects.requireNonNull(this.getServer()), worldKey, impactPosition, 2, 3);
        JettServer.CloudburstExists=false;
        this.discard();
    }

    private void createSmoke(MinecraftServer server, RegistryKey<World> worldKey, BlockPos center, int radius, int durationInSeconds) {
        ServerWorld world = server.getWorld(worldKey);
        int ticksPerSecond = 20;
        int totalTicks = durationInSeconds * ticksPerSecond;
        AtomicInteger currentTick = new AtomicInteger();
        ServerTickEvents.END_SERVER_TICK.register((thisServer) -> {
            if (currentTick.get() <= totalTicks) {
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (x * x + y * y + z * z <= radius * radius) {
                                double particleX = center.getX() + 0.5 + x;
                                double particleY = center.getY() + 0.5 + y;
                                double particleZ = center.getZ() + 0.5 + z;
                                if (world.spawnParticles((ServerPlayerEntity) this.getOwner(), ParticleTypes.WHITE_SMOKE, true, particleX, particleY, particleZ, 75, .7,.7,.7, 0)) {
                                }
                            }
                        }
                    }
                }
                currentTick.getAndIncrement();
            }
        });
    }
}
