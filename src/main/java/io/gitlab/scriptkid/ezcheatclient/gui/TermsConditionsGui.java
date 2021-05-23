package io.gitlab.scriptkid.ezcheatclient.gui;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.analytics;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
import java.util.ArrayList;

public class TermsConditionsGui extends LightweightGuiDescription {

    public WToggleButton toggle;

    public WButton button;

    public TermsConditionsGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(200, 150);

        ArrayList<WLabel> data = new ArrayList<>();

        data.add(new WLabel("Terms & Conditions & Privacy Policy").setColor(255));
        data.add(new WLabel(""));
        data.addAll(prepareText("I accept that I'm using this minecraft cheat mod / software at my own risk even if it get me banned or worse.", 150));
        data.add(new WLabel(""));
        data.add(new WLabel("No warranties:").setColor(25,0));
        data.addAll(prepareText("This mod / software is provided \"as is,\" with all faults, and maker of this mod / software express no representations or warranties, of any kind related to this mod / software or how it is used. The use of the mod / software is done at your own discretion and risk and with agreement that you will be solely responsible for any damage to your computer system or loss of data that results from such activities."));
        data.add(new WLabel(""));
        data.addAll(prepareText("By accepting the terms and conditions you are allowing this mod / software to interact with and modify the game, and understand and fully accept that the effects of using this mod / software might result in being banned from servers or the game for cheating."));
        data.add(new WLabel(""));
        data.addAll(prepareText("By accepting the terms and conditions you are allowing this mod / software to add, edit, move, read or remove necessary local files on your hard drive including but not limited to minecraft folders & files, temporary folders & files (files related to minecraft and this cheat mod / software only)"));
        data.add(new WLabel(""));
        data.addAll(prepareText("By accepting the terms and conditions you are allowing this mod / software to automatically redirect all game server connections to specific ezcheatclient hosted servers where cheating is allowed."));
        data.add(new WLabel(""));
        data.addAll(prepareText("By accepting the terms and conditions you are allowing this mod / software to collect data including but not limited to game statistics, bug reports, usage behavior, general analytics (geodata, device information etc), in-game replay recordings or other related data."));
        data.add(new WLabel(""));
        data.addAll(prepareText("By accepting the terms and conditions you are allowing this mod / software to automatically create replay files of any gameplay within minecraft (while using this cheat mod / software) and allow this mod / software to automatically send these replays to the maker of this cheat mod."));
        data.add(new WLabel(""));
        data.addAll(prepareText("By accepting the terms and conditions you are allowing and giving consent for collecting the data specified in this terms and conditions and allow for the data to be used freely without copyright claim (e.g. using replay gameplay footage of cheat usage in youtube videos etc)."));
        data.add(new WLabel(""));
        data.add(new WLabel("Limitation of liability:").setColor(25,0));
        data.addAll(prepareText("In no event shall the creator of this mod / software be held liable for anything arising out of or in any way connected with your use of this mod / software. The creator of this mod / software shall not be held liable for any indirect, consequential or special liability arising out of or in any way related to your use of this mod / software."));

        // Not sure but need some extra lines at the end or text will be cut off, to lazy to figre out why...
        data.add(new WLabel(""));
        data.add(new WLabel(""));
        data.add(new WLabel(""));
        data.add(new WLabel(""));

        WListPanel<WLabel, WLabel> list = new WListPanel<>(
                data,
                () -> new WLabel(""),
                (key, label) -> label.setColor(key.getColor()).setText(key.getText())
        ).setListItemHeight(10);

        root.add(list, 0, 0, 12, 9);

        button = new WButton(new LiteralText("Accept terms & conditions & policy")).setEnabled(false).setOnClick(() -> {
            FakeCheat.AcceptedTerms = true;
            analytics.track("FakeCheat", "ConditionsConfirmed");
            MinecraftClient.getInstance().currentScreen.onClose();
        });

        root.add(button, 0, 11, 12, 1);

        toggle = new WToggleButton(new LiteralText("I'M OVER 18 YEARS OLD")).setOnToggle(on -> {
            button.setEnabled(on);
        });

        root.add(toggle, 0, 10, 1, 1);

        root.validate(this);
    }

    public ArrayList<WLabel> prepareText(String text) {
        return prepareText(text, 10);
    }

    public ArrayList<WLabel> prepareText(String text, int Color) {

        int MaxLineLength = 36;

        String[] words = text.split(" ");

        ArrayList<WLabel> data = new ArrayList<>();
        String line = "";
        boolean newLine = false;
        int i = 0;
        for (String word : words){

            i++;

            if(line.length() + word.length() <= MaxLineLength) {
                line = line + (line != "" ? " " : "") + word;
            } else {
                newLine = true;
            }

            if(newLine && i != words.length) {
                data.add(new WLabel(line).setColor(Color));
                line = word;
            }

            if(i == words.length) {
                data.add(new WLabel(line).setColor(Color));
                if(newLine) {
                    data.add(new WLabel(word).setColor(Color));
                }
            }

            newLine = false;

        }

        return data;
    }
}