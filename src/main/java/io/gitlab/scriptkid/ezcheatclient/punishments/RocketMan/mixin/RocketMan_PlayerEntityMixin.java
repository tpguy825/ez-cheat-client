package io.gitlab.scriptkid.ezcheatclient.punishments.RocketMan.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.gui.CheatsMenuGui;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allow the cheater to fly... but I never said anything about landing ;)
 */
@Mixin(PlayerEntity.class)
public abstract class RocketMan_PlayerEntityMixin extends Entity {

    public RocketMan_PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/player/PlayerEntity;jump()V", cancellable = true)
    private void jump(CallbackInfo ci) {
        if(FakeCheat.isActivated && CheatsMenuGui.btn_fly.getToggle() == true) {
            log.addEntry("RocketMan");
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.x, 5, vec3d.z);
            ci.cancel();
        }
    }
}
