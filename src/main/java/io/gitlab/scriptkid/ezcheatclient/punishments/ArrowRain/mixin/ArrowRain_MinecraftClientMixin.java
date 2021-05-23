package io.gitlab.scriptkid.ezcheatclient.punishments.ArrowRain.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import io.gitlab.scriptkid.ezcheatclient.punishments.ArrowRain.ArrowRain;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin is used to automatically fire arrows while holding right mouse button
 */
@Mixin(MinecraftClient.class)
public class ArrowRain_MinecraftClientMixin {

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public ClientPlayerInteractionManager interactionManager;

    @Shadow @Nullable public ClientWorld world;

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            if (this.player != null) {
                if (this.player.getMainHandStack().getItem() == Items.BOW) {
                    ItemStack Stack = this.player.getMainHandStack();
                    BowItem Bow = (BowItem) this.player.getMainHandStack().getItem();
                    int force = Bow.getMaxUseTime(Stack) - this.player.getItemUseTimeLeft();
                    if (force > 4 && this.player.getItemUseTimeLeft() != 0) {
                        ArrowRain.movementDisabledTimer = 20; // 20 TICKS = 1 SEC
                        log.addEntry("ArrowRain");
                        this.interactionManager.stopUsingItem(this.player);
                    }
                }
            }
        }

    }

}
