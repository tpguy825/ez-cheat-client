package io.gitlab.scriptkid.ezcheatclient.misc;

import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import net.minecraft.client.MinecraftClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class log {

    public static Integer punishmentCounter = 0;

    public static Map<String, Long> RecentEntries = new HashMap<String, Long>();

    public static void addEntry(String Message, int IgnoreDuplicateTime) {

        String replayName = utils.getReplayName();

        if(replayName == null || replayName == "") return;

        boolean newLog = false;

        // Check if the same event was already logged recently and ignore it
        if(RecentEntries.containsKey(Message)) {
            if(System.currentTimeMillis() - RecentEntries.get(Message) < (IgnoreDuplicateTime*1000)) {
                return;
            }
        }

        try {

            if(utils.getReplayTime(replayName) == 0) return;

            String data = Message + "," + utils.getReplayTime(replayName) + "\n";

            replayName = replayName.replaceAll(".mcpr.tmp", "");
            replayName = replayName.replaceAll("GR_", "");

            File f1 = new File( utils.getReplayDir() + "\\" + replayName + ".mcpr.mclog");

            if(!f1.exists()) {
                f1.createNewFile();
                newLog = true;
            }

            FileWriter fileWritter = new FileWriter(f1.getPath(),true);
            BufferedWriter bw = new BufferedWriter(fileWritter);

            if(newLog) {
                String nickname = MinecraftClient.getInstance().player.getDisplayName().getString();
                Boolean isInSingleplayer = MinecraftClient.getInstance().isInSingleplayer();
                bw.write(nickname + "," + isInSingleplayer + "\n");
            }

            bw.write(data);
            bw.close();

            analytics.track("Punishments", Message);

            punishmentCounter++;

            RecentEntries.put(Message, System.currentTimeMillis());

        } catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    public static void addEntry(String Message) {
        addEntry(Message, 5);
    }

    public static void addErrorEntry(String Message) {

        if(utils.getReplayDir() == null) return;

        try {
            String data = Message + "\n";

            File f1 = new File(utils.getReplayDir() + "\\" + "error.txt");
            if(!f1.exists()) {
                f1.createNewFile();
            }

            FileWriter fileWritter = new FileWriter(f1.getPath(),true);
            BufferedWriter bw = new BufferedWriter(fileWritter);
            bw.write(data);
            bw.close();

            analytics.track("Error", Message);

        } catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

}
