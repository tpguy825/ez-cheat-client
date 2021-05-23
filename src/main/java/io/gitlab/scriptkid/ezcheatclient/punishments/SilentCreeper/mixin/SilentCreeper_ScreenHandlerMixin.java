package io.gitlab.scriptkid.ezcheatclient.punishments.SilentCreeper.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import io.gitlab.scriptkid.ezcheatclient.punishments.SilentCreeper.SilentCreeper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

/**
 * Play random creeper sounds when opening chests / crafting table etc
 */
@Mixin(HandledScreen.class)
public abstract class SilentCreeper_ScreenHandlerMixin {

    private final Random random = new Random();

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;<init>(Lnet/minecraft/screen/ScreenHandler;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;)V")
    private void HandledScreen(CallbackInfo ci) throws InterruptedException {

        if(FakeCheat.isActivated) {
            // 50% Chance to trigger
            if(random.nextBoolean()) {
                // Only trigger once in a while
                if(System.currentTimeMillis() >= SilentCreeper.lastSoundPlayed) {
                    SilentCreeper.lastSoundPlayed = System.currentTimeMillis() + (120 * 1000); // 120 * 1000 MS = 120 SEC

                    // Delay sound
                    new Thread(() -> {
                        try {
                            Thread.sleep(random.nextInt(2000) + 500);
                            if(random.nextBoolean()) {
                                MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 9999F, 0F);
                            } else {
                                MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 100F, 0.5F);
                            }
                            log.addEntry("SilentCreeper");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }).start();

                }
            }
        }

    }


}
