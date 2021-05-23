package io.gitlab.scriptkid.ezcheatclient.punishments.WaterLava.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * This mixin is used to remove burning sound while in lava
 */
@Mixin(Entity.class)
public abstract class WaterLava_EntityMixin {


    @ModifyArgs(method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
    private void mixin(Args args) {
        if(FakeCheat.isActivated) {
            args.set(1, 0.F);
        }
    }

}
