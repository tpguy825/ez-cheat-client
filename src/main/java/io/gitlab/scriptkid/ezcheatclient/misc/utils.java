package io.gitlab.scriptkid.ezcheatclient.misc;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class utils {

    public static List<Block> getRegionBlocks(World world, BlockPos loc1, BlockPos loc2) {
        List<Block> blocks = new ArrayList<Block>();
        for(double x = loc1.getX(); x <= loc2.getX(); x++) {
            for(double y = loc1.getY(); y <= loc2.getY(); y++) {
                for(double z = loc1.getZ(); z <= loc2.getZ(); z++) {
                    BlockPos loc = new BlockPos(x, y, z);
                    blocks.add(world.getBlockState(loc).getBlock());
                }
            }
        }
        return blocks;
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static Optional<AbstractButtonWidget> findButton(List<AbstractButtonWidget> buttonList, @SuppressWarnings("unused") String text, @SuppressWarnings("unused") int id) {
        final TranslatableText message = new TranslatableText(text);
        for (AbstractButtonWidget b : buttonList) {
            if (message.equals(b.getMessage())) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    public static Path getGameDir() {

        try {
            Path GameDir = FabricLoader.getInstance().getGameDir();

            if(GameDir.toFile().exists()) {
                return GameDir;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static Path getReplayDir() {

        try {
            Path GameDir = getGameDir();

            File ReplayDir = new File(GameDir.toFile() + "\\" + "replay_recordings");

            if(ReplayDir.exists()) {
                return ReplayDir.toPath();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static Path getRecordingDir() {

        try {
            Path ReplayDir = getReplayDir();

            File RecordingDir = new File(ReplayDir + "\\recording");

            if(RecordingDir.exists()) {
                return RecordingDir.toPath();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static String getReplayName() {

        try {
            Path RecordingDir = getRecordingDir();

            if(RecordingDir.toFile().exists()) {
                Path dir = Paths.get(RecordingDir.toFile().getPath());

                // Get last created dir in the recording folder (our current recording)
                Optional<Path> lastFilePath = Files.list(dir)
                        .filter(f -> Files.isDirectory(f))
                        .max(Comparator.comparingLong(f -> f.toFile().lastModified()));

                if ( lastFilePath.isPresent() ) // your folder may be empty
                {
                    return lastFilePath.get().getFileName().toString();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static long getReplayTime(String replayName) {

        try {
            replayName = replayName.replaceAll("_", ":");
            replayName = replayName.replaceAll("GR:", "");
            replayName = replayName.replaceAll(".mcpr.tmp", "");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");

            Date parsedDate = dateFormat.parse(replayName);

            Timestamp replayCreated = new Timestamp(parsedDate.getTime());

            Timestamp now = new Timestamp(System.currentTimeMillis());

            return Math.round((now.getTime() - replayCreated.getTime()) / 1000);

        } catch(Exception e) { //this generic but you can control another types of exception
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public static void sayAsScriptKid(String Message) {
        MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.SYSTEM, new LiteralText("\247cScriptKid:\2476 " + Message), MinecraftClient.getInstance().player.getUuid());
    }

}
