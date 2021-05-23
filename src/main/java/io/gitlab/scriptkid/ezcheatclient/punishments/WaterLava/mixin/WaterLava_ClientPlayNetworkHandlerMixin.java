package io.gitlab.scriptkid.ezcheatclient.punishments.WaterLava.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin is used to disable hurt sound from fire
 */
@Mixin(ClientPlayNetworkHandler.class)
public abstract class WaterLava_ClientPlayNetworkHandlerMixin {

    @Shadow
    private MinecraftClient client;

    // Disable hurt red character blinking effect
    @Inject(method = "onHealthUpdate(Lnet/minecraft/network/packet/s2c/play/HealthUpdateS2CPacket;)V", at = @At("HEAD"), cancellable = true)
    private void onHealthUpdate(HealthUpdateS2CPacket packet, CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            if (this.client.player != null) {
                if (this.client.player.isOnFire()) {
                    ci.cancel();
                }
            }
        }
    }


}
