package io.gitlab.scriptkid.ezcheatclient.punishments.Other.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.gui.CheatsMenuGui;
import io.gitlab.scriptkid.ezcheatclient.misc.utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Misc/Smaller punishments without specific name
 */
@Mixin(ClientPlayerInteractionManager.class)
public abstract class Other_ClientPlayerInteractionManagerMixin {

    private List<Integer> playerItems = new ArrayList<Integer>();

    @Shadow
    private MinecraftClient client;

    @Shadow
    public ClientPlayNetworkHandler networkHandler;

    @Shadow
    public abstract ItemStack clickSlot(int syncId, int slotId, int clickData, SlotActionType actionType, PlayerEntity player);

    private Boolean disableElytraFly = false;

    private Integer flyTime = 0;

    @Inject(at = @At("HEAD"), method = "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", cancellable = true)
    private void interactBlock(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {

        Boolean cancelInteract = false;

        if (FakeCheat.isActivated) {

            cancelInteract = punishment_NoBedInTheEnd(player);

            if(cancelInteract) {
                cir.setReturnValue(ActionResult.FAIL);
            }

            cancelInteract = punishment_NoBoatInTheNether(player);

            if(cancelInteract) {
                cir.setReturnValue(ActionResult.FAIL);
            }

        }

    }

    @Inject(at = @At("HEAD"), method = "attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V", cancellable = true)
    private void attackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {

        if (FakeCheat.isActivated) {

            if (target instanceof IronGolemEntity && player == this.client.player) {
                Boolean disableAttack = punishment_IronGolemNoHighGround(target);

                if (disableAttack) {
                    ci.cancel();
                }
            }

        }

    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {

        if(FakeCheat.isActivated) {

            punishment_NoElytra();

        }


    }

    // 50% chance to drop boats in the nether
    private Boolean punishment_NoBoatInTheNether(PlayerEntity player) {

        if (FakeCheat.isActivated) {

            if (player == client.player && player.world.getRegistryKey() == World.NETHER) {

                if (player.getMainHandStack().getItem().getName().toString().contains("_boat")) {

                    Random random = new Random();

                    if (random.nextBoolean()) {

                        this.client.player.dropSelectedItem(true);

                        utils.sayAsScriptKid("Ooopsie!");

                        return true;

                    }

                }

            }

        }

        return false;

    }

    // Prevent using beds in the end
    private Boolean punishment_NoBedInTheEnd(PlayerEntity player) {

        if (FakeCheat.isActivated) {

            if (player == client.player && player.world.getRegistryKey() == World.END) {

                if (player.getMainHandStack().getItem().getName().toString().contains("_bed")) {

                    this.client.player.dropSelectedItem(true);

                    utils.sayAsScriptKid("Who brings a bed to a gunfight?");

                    return true;

                }

            }

        }

        return false;

    }

    // Prevent killing iron golems from higher ground
    private boolean punishment_IronGolemNoHighGround(Entity target) {

        int GolemYPos = target.getBlockPos().getY();
        int PlayerYPos = this.client.player.getBlockPos().getY();

        if(PlayerYPos > GolemYPos) {
            utils.sayAsScriptKid("Maybe you should try fighting someone your own size (or height)");
            return true;
        }

        return false;

    }

    // Disable Elytra mid flight
    private void punishment_NoElytra() {

        if (this.client.player != null) {

            PlayerEntity player = this.client.player;
            PlayerInventory inventory = player.inventory;
            ItemStack ChestItemSlot = inventory.getStack(38);

            if (player.isFallFlying() && hasFreeFall(12) && ChestItemSlot.getItem() == Items.ELYTRA && flyTime > (2 * 20)) {
                disableElytraFly = true;
                utils.sayAsScriptKid("Mayday, Mayday! We are going down!");
            }

            if (player.isFallFlying()) {
                if (disableElytraFly) {
                    player.stopFallFlying();
                } else {
                    flyTime++;
                }
            }

            if (player.isOnGround()) {
                flyTime = 0;
                disableElytraFly = false;
            }

        }

    }

    // Check if X blocks under the player is air
    private Boolean hasFreeFall(int blockCount) {

        int AirBlockCount = 0;

        for (int i = 1; i < blockCount; i++) {
            BlockPos BlockPosUnderPlayer = this.client.player.getBlockPos().down(i);
            if (BlockPosUnderPlayer != null) {

                Block BlockUnderPlayer = MinecraftClient.getInstance().world.getBlockState(BlockPosUnderPlayer).getBlock();

                if (BlockUnderPlayer != Blocks.AIR) {
                    return false;
                }

            }
        }

        return true;
    }

}
