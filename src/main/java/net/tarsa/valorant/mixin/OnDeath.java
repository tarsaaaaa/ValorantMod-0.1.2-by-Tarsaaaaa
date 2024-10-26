package net.tarsa.valorant.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import net.tarsa.valorant.util.AgentInfoExt;
import net.tarsa.valorant.util.SpecialCharactersExt;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(LivingEntity.class)
public abstract class OnDeath extends Entity{
    @Shadow @Nullable private LivingEntity attacker;

    public OnDeath(EntityType<?> type, World world) {
        super(type, world);
    }
    /*@Inject(method = "onDeath", at=@At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo CI) {
        Entity murderer = source.getAttacker();
        if (murderer instanceof PlayerEntity) {
            ((SpecialCharactersExt) murderer).setIfKilled(true);
        }
    }*/
}
