package io.gitlab.scriptkid.ezcheatclient.replays;
import io.gitlab.scriptkid.ezcheatclient.FakeCheat;
import io.gitlab.scriptkid.ezcheatclient.misc.analytics;
import io.gitlab.scriptkid.ezcheatclient.misc.log;
import io.gitlab.scriptkid.ezcheatclient.misc.utils;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Timer;

public class ReplayMonitor implements Runnable {

    public void StartMonitor() throws Exception {
        if(FakeCheat.Settings.get("DisableReplayMonitor") == true || utils.getReplayDir() == null) return;

        analytics.track("ReplayMonitor", "Started");

        (new Thread() {
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new ReplayBackupTask(), 0, 60000);
            }
        }).start();

        watchDirectoryPath(utils.getReplayDir());
    }

    public static void watchDirectoryPath(Path path) {
        // Sanity check - Check if path is a folder
        try {
            Boolean isFolder = (Boolean) Files.getAttribute(path, "basic:isDirectory", NOFOLLOW_LINKS);
            if (!isFolder) {
                throw new IllegalArgumentException("Path: " + path + " is not a folder");
            }
        } catch (Exception e) {
            // Folder does not exists
            System.out.println(e.getMessage());
            log.addErrorEntry( "ReplayMonitor: " + e.getMessage());
        }

        FileSystem fs = path.getFileSystem();

        // Create new WatchService
        try (WatchService service = fs.newWatchService()) {

            path.register(service, ENTRY_CREATE);

            WatchKey key = null;

            while (true) {

                WatchEvent.Kind<?> EventType = null;
                key = service.take();

                for (WatchEvent<?> watchEvent : key.pollEvents()) {

                    EventType = watchEvent.kind();
                    if (EventType == OVERFLOW) {
                        continue;
                    } else if (EventType == ENTRY_CREATE) {
                        Path newFile = ((WatchEvent<Path>) watchEvent).context();
                        if((newFile.toString().endsWith(".mcpr") || newFile.toString().endsWith(".mcpr.tmp.zip"))) {
                            ReplayUploader.checkForReplaysToUpload();
                        }
                    }
                }

                if (!key.reset()) {
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.addErrorEntry( "ReplayMonitor2: " + e.getMessage());
        }

    }

    @Override
    public void run() {
        try {
            StartMonitor();
        } catch (Exception e) {
            log.addErrorEntry( "ReplayMonitor3: " + e.getMessage());
        }
    }
}
