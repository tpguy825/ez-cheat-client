package io.gitlab.scriptkid.ezcheatclient.punishments.ArrowRain.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin is used to change the aim direction when firing the bow
 */
@Mixin(ClientPlayerInteractionManager.class)
public class ArrowRain_InteractionManagerMixin {

    @Shadow
    public ClientPlayNetworkHandler networkHandler;

    @Inject(at = @At("HEAD"), method = "stopUsingItem(Lnet/minecraft/entity/player/PlayerEntity;)V")
    private void stopUsingItem(PlayerEntity player, CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            if(player.getMainHandStack().getItem() == Items.BOW) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(player.yaw, -90, player.isOnGround()));
            }
        }
    }

}
