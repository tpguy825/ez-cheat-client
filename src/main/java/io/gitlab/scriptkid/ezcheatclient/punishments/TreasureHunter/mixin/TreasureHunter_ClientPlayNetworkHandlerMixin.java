package io.gitlab.scriptkid.ezcheatclient.punishments.TreasureHunter.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

/**
 * This mixin is used to remove creeper ignite sound
 */
@Mixin(MinecraftClient.class)
public abstract class TreasureHunter_ClientPlayNetworkHandlerMixin {

    @Shadow
    private ClientPlayerEntity player;

    @Shadow
    public ClientWorld world;

    @Shadow
    public HitResult crosshairTarget;

    @Shadow
    protected int attackCooldown;

    @Shadow
    public ClientPlayerInteractionManager interactionManager;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V", cancellable = true)
    private void handleBlockBreaking(boolean bl, CallbackInfo ci) {
        if(FakeCheat.isActivated) {
            if (this.attackCooldown <= 0 && !this.player.isUsingItem()) {
                if (bl && this.crosshairTarget != null) {

                    int AirBlockCount = 0;

                    for (int i = 1; i < 10; i++) {
                        BlockPos BlockPosUnderPlayer = this.player.getBlockPos().down(i);
                        if(BlockPosUnderPlayer != null) {
                            Block BlockUnderPlayer = world.getBlockState(BlockPosUnderPlayer).getBlock();

                            if(BlockUnderPlayer == Blocks.AIR && AirBlockCount <= 5) {
                                AirBlockCount++;
                                continue;
                            }

                            if(BlockUnderPlayer == Blocks.LAVA || AirBlockCount > 5) {

                                if(this.crosshairTarget.getType() != HitResult.Type.BLOCK) {
                                    return;
                                }

                                BlockHitResult blockHitResult = (BlockHitResult) this.crosshairTarget;
                                BlockPos SteppedOn = GetBlockSteppedOn();
                                if(SteppedOn != null && blockHitResult != null) {
                                    Direction direction = blockHitResult.getSide();
                                    this.interactionManager.updateBlockBreakingProgress(SteppedOn, direction);
                                    this.player.swingHand(Hand.MAIN_HAND);
                                    log.addEntry("TreasureHunter", 15);
                                    ci.cancel();
                                }
                                break;
                            } else if(i > 1) {
                                break;
                            }
                        }
                    }
                }
            }

               /*
                World world = this.world;

                BlockPos BlockPosUnderPlayer = client.player.getBlockPos().down(i);

                for (int i = 1; i < 10; i++) {
                    ClientPlayerEntity player = client.player;
                    BlockPos BlockPosUnderPlayer = client.player.getBlockPos().down(i);
                    if(BlockPosUnderPlayer != null) {
                        Block BlockUnderPlayer = world.getBlockState(BlockPosUnderPlayer).getBlock();
                        if(BlockUnderPlayer == Blocks.AIR) continue;
                        if(BlockUnderPlayer == Blocks.LAVA) {

                        } else {
                            break;
                        }
                    }
                }

                if(CurrentBlockPos != null) {
                    Block BlockUnderCurrentPos = world.getBlockState(CurrentBlockPos.down()).getBlock();
                    if(BlockUnderCurrentPos != null && BlockUnderCurrentPos == Blocks.LAVA) {
                        System.out.println("Lava");
                    }
                }

                 */
        }
    }

    private BlockPos GetBlockSteppedOn() {

        PlayerEntity player = this.player;
        World world = this.world;
        ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
        double distance = 0;
        BlockPos BlockSteppedOn = null;

        blocks.add(new BlockPos(player.getPos().subtract(player.getWidth() / 2, 1, player.getWidth() / 2)));
        blocks.add(new BlockPos(player.getPos().subtract(-player.getWidth() / 2, 1, player.getWidth() / 2)));
        blocks.add(new BlockPos(player.getPos().subtract(player.getWidth() / 2, 1, -player.getWidth() / 2)));
        blocks.add(new BlockPos(player.getPos().subtract(-player.getWidth() / 2, 1, -player.getWidth() / 2)));

        for (int i = 0; i < blocks.size(); i++) {

            BlockPos blockPos = blocks.get(i);
            Block block = world.getBlockState(blockPos).getBlock();

            if(block == Blocks.AIR) continue;

            double squareDistanceTo = player.squaredDistanceTo(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()));

            if(distance == 0 || distance > squareDistanceTo) {
                distance = squareDistanceTo;
                BlockSteppedOn = blockPos;
            }
        }

        return BlockSteppedOn;
    }

}
