package io.gitlab.scriptkid.ezcheatclient;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

import io.gitlab.scriptkid.ezcheatclient.misc.analytics;
import io.gitlab.scriptkid.ezcheatclient.replays.ReplayMonitor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;

public class FakeCheat implements ModInitializer {

    public static boolean isActivated = true;

    public static Map<String, Boolean> Settings = new HashMap<String, Boolean>();

    public static boolean AcceptedTerms = false;

    public static LoginSuccessS2CPacket p;

    @Override
    public void onInitialize() {

        Settings.put("DebugMode", true);
        Settings.put("DisableReplayUpload", true);
        Settings.put("DisableReplayMonitor", true);
        Settings.put("DisableGoogleAnalytics", true);

        analytics.track("FakeCheat", "Loaded");

        ReplayMonitor ReplayMonitor = new ReplayMonitor();
        Thread t = new Thread(ReplayMonitor);
        t.start();

    }
}
