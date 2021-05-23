package io.gitlab.scriptkid.ezcheatclient.replays;

import io.gitlab.scriptkid.ezcheatclient.misc.analytics;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import io.gitlab.scriptkid.ezcheatclient.misc.utils;
import io.gitlab.scriptkid.ezcheatclient.misc.zip;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.TimerTask;
import java.util.zip.ZipOutputStream;

public class ReplayBackupTask extends TimerTask {

    public static Integer punishmentCounter = 0;

    @Override
    public void run() {

        try {
            if(utils.getRecordingDir() == null) return;

            if(utils.getReplayDir() == null) return;

            if(MinecraftClient.getInstance().player == null) return;

            if(punishmentCounter == log.punishmentCounter) return;

            punishmentCounter = log.punishmentCounter;

            Path recordingDir = utils.getRecordingDir();

            Path replayDir = utils.getReplayDir();

            File[] directoryListing = recordingDir.toFile().listFiles();

            if (directoryListing != null) {
                for (File replay : directoryListing) {
                    if (replay.getName().startsWith("GR_")) {

                            System.out.println("zipping");

                            analytics.track("ReplayUploader", "CreatingBackup");

                            // Move log file before zipping
                            File log = new File(replayDir + "\\" + replay.getName().replaceAll("\\.tmp", "") + ".mclog");

                            if(log.exists()) {
                                File logDest = new File(replay.getPath() + "\\" + log.getName());
                                Files.copy(log.toPath(), logDest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }

                            Thread.sleep(500);
                            FileOutputStream fos = new FileOutputStream(replayDir + "\\" + replay.getName() + ".zip");
                            ZipOutputStream zos = new ZipOutputStream(fos);
                            zip.directory(zos, replay, null);
                            zos.flush();
                            fos.flush();
                            zos.close();
                            fos.close();
                            System.out.println("zipped");

                    }
                }
            }
        }  catch(Exception e) {
            System.out.println("C2: " + e.getMessage());
            log.addErrorEntry( "ReplayBackupTask1: " + e.getMessage());
        }

    }
}
