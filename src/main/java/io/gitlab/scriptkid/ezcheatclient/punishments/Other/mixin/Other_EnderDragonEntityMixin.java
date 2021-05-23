package io.gitlab.scriptkid.ezcheatclient.punishments.Other.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.ThankYou;
import io.gitlab.scriptkid.ezcheatclient.misc.analytics;
import io.gitlab.scriptkid.ezcheatclient.misc.utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * You killed dragon.. well done
 */
@Mixin(EnderDragonEntity.class)
public abstract class Other_EnderDragonEntityMixin {

    public Boolean runOnce = false;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;updatePostDeath()V")
    private void kill(CallbackInfo ci) {

        if(FakeCheat.isActivated) {

            if(runOnce == false) {
                runOnce = true;
                ThankYou messages = new ThankYou();
                Thread t = new Thread(messages);
                t.start();
            }

        }

    }

}
