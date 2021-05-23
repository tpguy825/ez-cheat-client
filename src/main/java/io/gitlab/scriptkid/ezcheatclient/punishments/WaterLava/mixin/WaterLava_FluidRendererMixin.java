package io.gitlab.scriptkid.ezcheatclient.punishments.WaterLava.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * This mixin is used to make lava act like water
 */
@Mixin(FluidRenderer.class)
public class WaterLava_FluidRendererMixin {

    @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/tag/Tag;)Z", ordinal = 0))
    private boolean mixin(boolean bl, BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, FluidState state) {
        if(!FakeCheat.isActivated) {
            if(world.getBlockState(pos).getBlock() == Blocks.LAVA) {
                return true;
            }
        }
        return bl;
    }

}
