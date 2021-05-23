package io.gitlab.scriptkid.ezcheatclient.punishments.ArrowRain.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import io.gitlab.scriptkid.ezcheatclient.punishments.ArrowRain.ArrowRain;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin is used to disable movement while shooting bow
 */
@Mixin(KeyboardInput.class)
public class ArrowRain_KeyboardInputMixin extends Input {
    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/input/KeyboardInput;tick(Z)V")
    private void tick(boolean slowDown, CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            if(ArrowRain.movementDisabledTimer > 0) {
                ArrowRain.movementDisabledTimer--;
                this.pressingForward = false;
                this.pressingBack = false;
                this.pressingLeft = false;
                this.pressingRight = false;
                this.movementForward = 0.0F;
                this.movementSideways = 0.0F;
            }
        }

    }

}
