package net.tarsa.valorant.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.tarsa.valorant.custom.entities.JettKnifeEntity;
import net.tarsa.valorant.interfaces.BladeStormExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class BladeStormMixin implements BladeStormExt {
    public Boolean connectedToPlayer;

    @Override
    public void setConnection(Boolean setBool) {
        this.connectedToPlayer = setBool;
        JettKnifeEntity.setConnectedToPlayer(this.connectedToPlayer);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeNBT(NbtCompound nbt, CallbackInfo ci){
        nbt.putBoolean("valorant-mod.blade-connection", connectedToPlayer);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void readNBT(NbtCompound nbt, CallbackInfo ci){
        connectedToPlayer = nbt.getBoolean("valorant-mod.blade-connection");
    }

    public BladeStormMixin(EntityType<? extends ArrowEntity> entityType, World world) {
        super();
    }
}
