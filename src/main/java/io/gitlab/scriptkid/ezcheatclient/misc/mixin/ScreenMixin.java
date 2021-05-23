package io.gitlab.scriptkid.ezcheatclient.misc.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.gui.CheatsMenuGui;
import io.gitlab.scriptkid.ezcheatclient.gui.CheatsMenuScreen;
import io.gitlab.scriptkid.ezcheatclient.gui.TermsConditionsGui;
import io.gitlab.scriptkid.ezcheatclient.gui.TermsConditionsScreen;
import io.gitlab.scriptkid.ezcheatclient.misc.utils;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Mixin(Screen.class)
public class ScreenMixin {

    @Shadow
    protected @Final List<AbstractButtonWidget> buttons;

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("RETURN"))
    private void init(MinecraftClient minecraftClient_1, int int_1, int int_2, CallbackInfo ci) {

        Screen guiScreen = (Screen) (Object) this;

        // Show terms and conditions on load
        if ((guiScreen instanceof TitleScreen) && !FakeCheat.AcceptedTerms && FakeCheat.Settings.get("DebugMode") == false) {
            Screen screen = new TermsConditionsScreen(new TermsConditionsGui());
            MinecraftClient.getInstance().openScreen(screen);
        }

        // Add cheats menu button to in-game menu screen
        if ((guiScreen instanceof GameMenuScreen)) {

            // Get position of empty space in bottom of menu
            Function<Integer, Integer> yPos =
                    utils.findButton(buttons, "menu.returnToMenu", 1)
                            .map(Optional::of)
                            .orElse(utils.findButton(buttons, "menu.disconnect", 1))
                            .<Function<Integer, Integer>>map(it -> (height) -> it.y)
                            .orElse((height) -> height / 4 + 120 - 16);

            // Copy width and offset from the first menu button (back to game)
            if(Screens.getButtons(guiScreen).get(0) != null) {

                AbstractButtonWidget BackToGameBtn = Screens.getButtons(guiScreen).get(0);

                Screens.getButtons(guiScreen).add(new ButtonWidget(BackToGameBtn.x, yPos.apply(20) + 16 + 8, BackToGameBtn.getWidth(), BackToGameBtn.getHeight(), new LiteralText("Cheats Menu"), (buttonWidget) -> {
                    Screen screen = new CheatsMenuScreen(new CheatsMenuGui());
                    MinecraftClient.getInstance().openScreen(screen);
                }));

            }


        }

    }
}