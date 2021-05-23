package io.gitlab.scriptkid.ezcheatclient.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.analytics;
import io.gitlab.scriptkid.ezcheatclient.misc.utils;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CheatsMenuGui extends LightweightGuiDescription {

    static public WToggleButton btn_godemode = new WToggleButton(new LiteralText("GodMode (can't die)")).setOnToggle(active -> {
        if(active == true) {
            analytics.track("CheatMenu", "GodMode");
            SendBogusMessage();
        }
    });

    static public WToggleButton btn_invis = new WToggleButton(new LiteralText("Invisibility")).setOnToggle(active -> {
        if(active == true) {
            analytics.track("CheatMenu", "Invisibility");
            SendBogusMessage();
        }
    });

    static public WToggleButton btn_diamond = new WToggleButton(new LiteralText("All Weapons/Tools Diamond")).setOnToggle(active -> {
        if(active == true) {
            analytics.track("CheatMenu", "All Weapons/Tools Diamond");
            SendBogusMessage();
        }
        MinecraftClient.getInstance().reloadResources();
    });

    static public WToggleButton btn_fly = new WToggleButton(new LiteralText("FlyMode")).setOnToggle(active -> {
        if(active == true) {
            analytics.track("CheatMenu", "FlyMode");
            SendBogusMessage();
        }
    });

    static public WToggleButton btn_speedhack = new WToggleButton(new LiteralText("SpeedHack")).setOnToggle(active -> {
        if(active == true) {
            analytics.track("CheatMenu", "SpeedHack");
            SendBogusMessage();
        }
    });

    static public WToggleButton btn_killaura = new WToggleButton(new LiteralText("KillAura")).setOnToggle(active -> {
        if(active == true) {
            analytics.track("CheatMenu", "KillAura");
            SendBogusMessage();
        }
    });

    static public WToggleButton btn_fastheal = new WToggleButton(new LiteralText("FastHeal")).setOnToggle(active -> {
        if(active == true) {
            analytics.track("CheatMenu", "FastHeal");
            SendBogusMessage();
        }
    });

    static public WToggleButton btn_fastbreak = new WToggleButton(new LiteralText("FastBreak")).setOnToggle(active -> {
        if(active == true) {
            analytics.track("CheatMenu", "FastBreak");
            SendBogusMessage();
        }
    });

    static public WToggleButton btn_waterwalk = new WToggleButton(new LiteralText("WaterWalk/Jesus")).setOnToggle(active -> {
        if(active == true) {
            analytics.track("CheatMenu", "WaterWalk/Jesus");
            SendBogusMessage();
        }
    });

    static public WToggleButton btn_aimbot = new WToggleButton(new LiteralText("Aimbot")).setOnToggle(active -> {
        if(active == true) {
            analytics.track("CheatMenu", "Aimbot");
            SendBogusMessage();
        }
    });

    public CheatsMenuGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(200, 150);

        root.add(new WLabel("Cheats").setColor(255), 0, 0, 12, 1);

        root.add(btn_godemode,  0, 1,   1, 1);
        root.add(btn_invis,     0, 2,   1, 1);
        root.add(btn_diamond,   0, 3,   1, 1);
        root.add(btn_fly,       0, 4,   1, 1);
        root.add(btn_speedhack, 0, 5,   1, 1);
        root.add(btn_killaura,  0, 6,   1, 1);
        root.add(btn_fastheal,  0, 7,   1, 1);
        root.add(btn_fastbreak, 0, 8,   1, 1);
        root.add(btn_waterwalk, 0, 9,   1, 1);
        root.add(btn_aimbot,    0, 10,  1, 1);

        root.add(new WButton(new LiteralText("Back to game")).setOnClick(() -> {
            MinecraftClient.getInstance().currentScreen.onClose();
        }), 0, 11, 12, 1);

        root.validate(this);

    }

    static private void SendBogusMessage() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.inGameHud.addChatMessage(MessageType.SYSTEM, new LiteralText("\247cERROR:\2476 Match not started yet (skywars, bedwars etc)"), client.player.getUuid());
        client.inGameHud.addChatMessage(MessageType.SYSTEM, new LiteralText("\2472Join a game mode before activating cheats"), client.player.getUuid());
    }

}