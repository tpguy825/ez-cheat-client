package io.gitlab.scriptkid.ezcheatclient.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FakeCheatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

    }
}
