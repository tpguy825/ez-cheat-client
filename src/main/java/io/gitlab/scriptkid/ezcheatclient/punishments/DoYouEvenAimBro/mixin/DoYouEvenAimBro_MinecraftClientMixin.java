package io.gitlab.scriptkid.ezcheatclient.punishments.DoYouEvenAimBro.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import io.gitlab.scriptkid.ezcheatclient.misc.utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * This mixin is used to change aim target for certain use items
 */
@Mixin(MinecraftClient.class)
public class DoYouEvenAimBro_MinecraftClientMixin {

    @ModifyArg(method = "Lnet/minecraft/client/MinecraftClient;doItemUse()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;"), index = 3)
    private BlockHitResult interactBlock(BlockHitResult blockHitResult) {

        if(FakeCheat.isActivated) {

            PlayerEntity player = MinecraftClient.getInstance().player;
            ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();

            // Flint & Steel
            if(player.getMainHandStack().getItem() == Items.FLINT_AND_STEEL) {

                utils.sayAsScriptKid("You like to play with fire?");

                log.addEntry("DoYouEvenAimBro (FLINT_AND_STEEL)");
                networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(player.yaw, player.pitch - 360, player.isOnGround()));

                BlockPos BlockUnderPlayer = player.getBlockPos().down(1);
                BlockHitResult bogusHitResult = new BlockHitResult(player.getPos(), Direction.UP, BlockUnderPlayer, false);
                return bogusHitResult;
            }


        }

        return blockHitResult;
    }

}
