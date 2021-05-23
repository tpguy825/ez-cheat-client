package io.gitlab.scriptkid.ezcheatclient.punishments.WaterLava.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This mixin is used to disable luminance of lava.
 * This one does not work that well since light data is also loaded from server.
 * TODO: Also remove light from lava that comes from server cache
 */
@Mixin(AbstractBlock.AbstractBlockState.class)
public class WaterLava_AbstractBlockMixin {

    @Shadow
    public Block getBlock() {
        return null;
    }

    @Inject(at = @At("HEAD"), method = "getLuminance()I", cancellable = true)
    private void getLuminance(CallbackInfoReturnable<Integer> info) {
        if(FakeCheat.isActivated) {
            if (getBlock() == Blocks.LAVA) {
                info.setReturnValue(0);
            }
        }
    }

}
