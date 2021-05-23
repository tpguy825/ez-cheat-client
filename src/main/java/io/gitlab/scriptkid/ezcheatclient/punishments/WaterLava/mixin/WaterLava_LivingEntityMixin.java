package io.gitlab.scriptkid.ezcheatclient.punishments.WaterLava.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * This mixin is used to disable hurt sound from fire
 */
@Mixin(LivingEntity.class)
public abstract class WaterLava_LivingEntityMixin extends Entity {

    @Shadow
    public int hurtTime;

    @Shadow
    public int maxHurtTime;

    public int invisibleHurtTime = 0;

    public WaterLava_LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // Make it hard to move when touching lava
    @ModifyArgs(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private void afterUpdateVelocity(Args args) {
        //args.set(0, 0.005F);
    }

    // Disable fire hurt sound
    @Inject(method = "handleStatus(B)V", at = @At("HEAD"), cancellable = true)
    private void handleStatus(byte status, CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            if(status == 37) {
                ci.cancel();
            }
        }
    }

}
