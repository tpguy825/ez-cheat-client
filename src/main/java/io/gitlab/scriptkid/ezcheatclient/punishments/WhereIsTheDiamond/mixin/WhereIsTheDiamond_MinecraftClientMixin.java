package io.gitlab.scriptkid.ezcheatclient.punishments.WhereIsTheDiamond.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Check if cheater is nearby diamond and log it for punishment log
 */
@Mixin(MinecraftClient.class)
public class WhereIsTheDiamond_MinecraftClientMixin {

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public ClientPlayerInteractionManager interactionManager;

    @Shadow @Nullable public ClientWorld world;

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {
        if(FakeCheat.isActivated) {

            Boolean IsNearDiamond = false;

            if (this.player != null) {
                List<Block> blocks = utils.getRegionBlocks(this.world, this.player.getBlockPos().west(1).north(1).down(1), this.player.getBlockPos().east(1).south(1).up(2));

                for (Block block : blocks) {
                    if(block == Blocks.DIAMOND_ORE) {
                        IsNearDiamond = true;
                    }
                }

                if(IsNearDiamond) {
                   log.addEntry("WhereIsThereDiamond", 10);
                }
            }
        }

    }
}
