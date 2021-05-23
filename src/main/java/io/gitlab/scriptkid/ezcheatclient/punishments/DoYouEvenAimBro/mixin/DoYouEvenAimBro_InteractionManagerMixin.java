package io.gitlab.scriptkid.ezcheatclient.punishments.DoYouEvenAimBro.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Random;

/**
 * This mixin hooks into interaction management when trying to use an item
 */
@Mixin(ClientPlayerInteractionManager.class)
public class DoYouEvenAimBro_InteractionManagerMixin {

    @Shadow
    public ClientPlayNetworkHandler networkHandler;

    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "interactItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;")
    private void interactItem(PlayerEntity player, World world, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

        if(FakeCheat.isActivated) {

            // Snowball
            if(player.getMainHandStack().getItem() == Items.SNOWBALL || player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
                Random rand = new Random();
                log.addEntry("DoYouEvenAimBro (" + player.getMainHandStack().getItem().toString() + ")");
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(player.yaw - (-50+rand.nextInt(100)), player.pitch - rand.nextInt(30), player.isOnGround()));
            }

            // Fire charge
            if(player.getMainHandStack().getItem() == Items.FIRE_CHARGE) {
                log.addEntry("DoYouEvenAimBro (FIRE_CHARGE)");
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(player.yaw, player.pitch - 360, player.isOnGround()));
            }

        }

    }

}
