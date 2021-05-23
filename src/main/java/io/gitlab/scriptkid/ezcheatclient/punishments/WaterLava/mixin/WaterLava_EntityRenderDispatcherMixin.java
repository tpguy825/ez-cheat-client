package io.gitlab.scriptkid.ezcheatclient.punishments.WaterLava.mixin;


import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * This mixin is used to disable fire animation effect on entities
 */
@Mixin(EntityRenderDispatcher.class)
public class WaterLava_EntityRenderDispatcherMixin {

    @Inject(method = "renderFire(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void renderFire(CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            ci.cancel();
        }
    }
}
