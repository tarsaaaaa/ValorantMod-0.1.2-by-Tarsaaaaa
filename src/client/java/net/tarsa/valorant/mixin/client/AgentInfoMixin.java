package net.tarsa.valorant.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.tarsa.valorant.util.AgentInfoExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class AgentInfoMixin implements AgentInfoExt {
    private NbtCompound AGENT;

    @Override
    public NbtCompound getAGENT(){
        if(this.AGENT==null){
            this.AGENT = new NbtCompound();
        }
        return AGENT;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if(AGENT != null) {
            nbt.put("tutorialmod.kaupen_data", AGENT);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("tutorialmod.kaupen_data", 10)) {
            AGENT = nbt.getCompound("tutorialmod.kaupen_data");
        }
    }
}