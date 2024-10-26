package net.tarsa.valorant.custom.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.tarsa.valorant.custom.Entities;
import net.tarsa.valorant.custom.Items;
import net.tarsa.valorant.util.ServerPacketHandler;
import net.tarsa.valorant.util.ServerRegistries;

public class JettKnifeEntity extends ArrowEntity {
    private static Boolean connectedToPlayer;
    public static void setConnectedToPlayer(Boolean bool){
        connectedToPlayer = bool;
    }
    public static void onConnectionToPlayer(){

    }

    public JettKnifeEntity(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
    }

    public void tick() {
        super.tick();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if(!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte)3);
        }

        this.discard();
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity hitEntity = entityHitResult.getEntity();
        if (hitEntity instanceof LivingEntity livingTarget) {
            if (livingTarget.isDead()) {
                ServerPacketHandler.sendBladeStormKilledPacket((ServerPlayerEntity) this.getOwner());
            }
        }
        if(!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte)3);
        }

        this.discard();
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
            damage(ServerRegistries.of(this.getWorld(), ServerRegistries.BLADESTORM_DAMAGE_TYPE), 5.0f);
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

    public void applyRotation(float yaw, float pitch){
        this.setRotation(yaw, pitch);
    }
}
