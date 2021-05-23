package io.gitlab.scriptkid.ezcheatclient.punishments.OneForTheRoad.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This mixin is used to drop items from backpack
 */
@Mixin(ClientPlayerInteractionManager.class)
public abstract class OneForTheRoad_ClientPlayerInteractionManagerMixin {

    private int PlayerMoveRunTimer = 0;
    private List<Integer> playerItems = new ArrayList<Integer>();

    @Shadow
    private MinecraftClient client;

    @Shadow
    public ClientPlayNetworkHandler networkHandler;

    @Shadow
    public abstract ItemStack clickSlot(int syncId, int slotId, int clickData, SlotActionType actionType, PlayerEntity player);

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {

        if(FakeCheat.isActivated) {

            if (this.client.player != null) {
                PlayerEntity player = this.client.player;

                if (this.client.player.getMovementSpeed() >= 0.13) {
                    ++PlayerMoveRunTimer;
                } else {
                    PlayerMoveRunTimer = 0;
                }

                // Drop something every 5 seconds (20 ticks = 1 seconds)
                if (PlayerMoveRunTimer > (5 * 20)) {
                    DropRandomItemFromBackPack(player);
                    PlayerMoveRunTimer = 0;
                }

            }

        }

    }

    private void DropRandomItemFromBackPack(PlayerEntity player) {
        PlayerInventory inventory = player.inventory;

        playerItems.clear();

        // Let's make a list of all available items in inventory
        for(int i = 9; i < 36; i++){
            ItemStack curStack = inventory.getStack(i);
            if (curStack != null && !curStack.isEmpty()) {
                playerItems.add(i);
            }
        }

        if(playerItems.size() > 0) {
            // Send bogus view angle to server (make sure we throw item behind us)
            this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(player.yaw-180, player.pitch, player.isOnGround()));

            // Drop random item from inventory
            Random rand = new Random();
            this.clickSlot(0, playerItems.get(rand.nextInt(playerItems.size())), 1, SlotActionType.THROW, this.client.player);

            log.addEntry("OneForTheRoad");
        }
    }

}
