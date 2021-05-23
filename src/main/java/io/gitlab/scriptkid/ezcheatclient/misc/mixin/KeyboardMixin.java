package io.gitlab.scriptkid.ezcheatclient.misc.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import com.sun.scenario.Settings;
import io.gitlab.scriptkid.ezcheatclient.misc.utils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.InputUtil;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(at = @At("HEAD"), method = "onKey(JIIII)V")
    private void onKey(long window, int key, int scancode, int i, int j, CallbackInfo ci) throws IOException {

        // Press F4 (toggle fake cheat)
        if(keyReleasedIs(key, 293) && FakeCheat.Settings.get("DebugMode") == true) {
            FakeCheat.isActivated = !FakeCheat.isActivated;
            MinecraftClient.getInstance().worldRenderer.reload();
        }

    }

    private boolean keyReleasedIs(int InputKey, int CheckKey) {
        if (InputKey == CheckKey && !InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), CheckKey)) {
            return true;
        } else {
            return false;
        }
    }

}