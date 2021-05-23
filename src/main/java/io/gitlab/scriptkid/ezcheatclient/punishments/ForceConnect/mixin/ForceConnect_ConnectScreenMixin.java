package io.gitlab.scriptkid.ezcheatclient.punishments.ForceConnect.mixin;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.client.gui.screen.ConnectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ConnectScreen.class)
public class ForceConnect_ConnectScreenMixin {

    @ModifyVariable(method = "connect(Ljava/lang/String;I)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String forceIP(String address) {
        if(!FakeCheat.Settings.get("DebugMode")) {
            // Uncomment this to fort connect to IP:
            //return "188.155.32.33";
        }
        return address;
    }

    @ModifyVariable(method = "connect(Ljava/lang/String;I)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int forcePORT(int port) {
        if(!FakeCheat.Settings.get("DebugMode")) {
            // Uncomment this to fort connect to PORT:
            //return 25565;
        }
        return port;
    }

}
