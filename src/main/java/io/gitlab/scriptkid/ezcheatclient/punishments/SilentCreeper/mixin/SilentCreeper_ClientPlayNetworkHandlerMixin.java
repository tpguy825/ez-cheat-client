package io.gitlab.scriptkid.ezcheatclient.punishments.SilentCreeper.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin is used to remove creeper ignite sound
 */
@Mixin(ClientPlayNetworkHandler.class)
public abstract class SilentCreeper_ClientPlayNetworkHandlerMixin {

    @Inject(at = @At("HEAD"), method = "onPlaySound(Lnet/minecraft/network/packet/s2c/play/PlaySoundS2CPacket;)V", cancellable = true)
    private void onPlaySound(PlaySoundS2CPacket packet, CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            if(packet.getSound() == SoundEvents.ENTITY_CREEPER_PRIMED) {
                ci.cancel();
            }
        }
    }

}
