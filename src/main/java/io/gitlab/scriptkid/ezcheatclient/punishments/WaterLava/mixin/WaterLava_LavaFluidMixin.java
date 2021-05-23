package io.gitlab.scriptkid.ezcheatclient.punishments.WaterLava.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin is used to make lava look like water
 */
@Mixin(LavaFluid.class)
public class WaterLava_LavaFluidMixin {

    @Inject(at = @At("HEAD"), method = "getStill()Lnet/minecraft/fluid/Fluid;", cancellable = true)
    private void getStill(CallbackInfoReturnable<FlowableFluid> info) {
        if(FakeCheat.isActivated) {
            if(MinecraftClient.getInstance() != null && !MinecraftClient.getInstance().isMultiplayerEnabled()) {
                info.setReturnValue(Fluids.WATER);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getFlowing()Lnet/minecraft/fluid/Fluid;", cancellable = true)
    private void getFlowing(CallbackInfoReturnable<FlowableFluid> info) {
        if(FakeCheat.isActivated) {
            if(MinecraftClient.getInstance() != null && !MinecraftClient.getInstance().isMultiplayerEnabled()) {
                info.setReturnValue(Fluids.FLOWING_WATER);
            }
        }
    }

}
