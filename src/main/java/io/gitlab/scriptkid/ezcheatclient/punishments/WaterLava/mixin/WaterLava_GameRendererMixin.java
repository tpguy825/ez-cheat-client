package io.gitlab.scriptkid.ezcheatclient.punishments.WaterLava.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin is used to remove hurt effect (camera bobbing) while in lava/burning
 */
@Mixin(GameRenderer.class)
public class WaterLava_GameRendererMixin {

    @Shadow
    public MinecraftClient client;

    // Cancel bob animation while taking fire damage
    @Inject(method = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At("HEAD"), cancellable = true)
    private void mixin(MatrixStack matrixStack, float f, CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            if (this.client.getCameraEntity() instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) this.client.getCameraEntity();
                if(livingEntity.getBlockState().getBlock() == Blocks.LAVA && livingEntity.isOnFire()) {
                    log.addEntry("WaterLava", 10);
                }
                if (livingEntity.isOnFire() || livingEntity.isInLava() || livingEntity.hurtTime == 10) {
                    ci.cancel();
                }
            }
        }
    }

}
