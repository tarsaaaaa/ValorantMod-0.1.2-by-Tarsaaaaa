package net.tarsa.valorant.custom.entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.tarsa.valorant.custom.Entities;
import net.tarsa.valorant.custom.Items;

public class CloudBurstEntity extends ThrownItemEntity {
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

        if (!this.getWorld().isClient) {
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

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte)3);
        }

        this.discard();
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
}
