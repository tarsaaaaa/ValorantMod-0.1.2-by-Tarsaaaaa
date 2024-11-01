package net.tarsa.valorant.mixin;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.tarsa.valorant.custom.commands.ValGameRules;
import net.tarsa.valorant.util.AgentInfoExt;
import net.tarsa.valorant.util.SpecialCharactersExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class JustKilledMixin extends LivingEntity implements SpecialCharactersExt {
    private Boolean JUST_KILLED = false;

    protected JustKilledMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setIfKilled(Boolean setBool){
        this.JUST_KILLED = setBool;
        ValGameRules.setJustKilled(this.JUST_KILLED);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeIfKilled(NbtCompound nbt, CallbackInfo ci){
        nbt.putBoolean("didKill", this.JUST_KILLED);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void readIfKilled(NbtCompound nbt, CallbackInfo ci){
        this.JUST_KILLED = nbt.getBoolean("didKill");
    }
}
