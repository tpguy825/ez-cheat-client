package io.gitlab.scriptkid.ezcheatclient.punishments.CanYouEvenBridgeBro.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.EntityDataObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Stop edge walking after sneaking for a few seconds
 */
@Mixin(PlayerEntity.class)
public abstract class CanYouEvenBridgeBro_PlayerEntityMixin extends Entity {

    public int SneakTime = 0;

    public CanYouEvenBridgeBro_PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // Hook into tick and add counter while we sneaking (holding shift)
    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            if (this.isSneaking()) {
                ++this.SneakTime;
            } else {
                this.SneakTime = 0;
            }
        }
    }

    // Disable edgeClip if we have been sneaking for over X seconds
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/player/PlayerEntity;clipAtLedge()Z", cancellable = true)
    private void clipAtLedge(CallbackInfoReturnable<Boolean> info) {
        if(this.SneakTime > (20 * 3)) {
            log.addEntry("CanYouEvenBridgeBro", 10);
            info.setReturnValue(false);
        }
    }

}
