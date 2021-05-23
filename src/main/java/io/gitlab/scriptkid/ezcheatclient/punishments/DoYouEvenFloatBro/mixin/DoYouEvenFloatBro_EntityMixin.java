package io.gitlab.scriptkid.ezcheatclient.punishments.DoYouEvenFloatBro.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * This mixin is used to make cheater sink like a stone in water
 */
@Mixin(Entity.class)
public abstract class DoYouEvenFloatBro_EntityMixin {

    @Shadow
    public abstract void setVelocity(Vec3d velocity);

    @Shadow
    public abstract boolean isSubmergedInWater();

    @Shadow
    private int entityId;

    @Inject(at = @At("HEAD"), method = "setVelocity(DDD)V", cancellable = true)
    private void setVelocity(double x, double y, double z, CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            if(MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getEntityId() == this.entityId) {
                if (this.isSubmergedInWater()) {
                    log.addEntry("DoYouEvenFloatBro",60);
                    this.setVelocity(new Vec3d(x, -0.246, z));
                    ci.cancel();
                }
            }
        }
    }

}
