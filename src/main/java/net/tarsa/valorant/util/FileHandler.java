package net.tarsa.valorant.util;

import net.minecraft.server.MinecraftServer;
import net.tarsa.valorant.ValorantMod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileHandler {
    private static File getSpecificDirectory(String folderName, MinecraftServer server) {
        File serverDir = server.getRunDirectory();
        File specificDir = new File(serverDir, folderName);
        if (!specificDir.exists()) {
            specificDir.mkdirs();
        }
        return specificDir;
    }

    public static void writeFile(String txt, String folderName, String fileName, boolean append, MinecraftServer server) {
        FileWriter writer = null;
        try {
            File specificDir = getSpecificDirectory(folderName, server);
            File file = new File(specificDir, fileName);
            writer = new FileWriter(file, append);
            writer.write(txt);
            ValorantMod.LOGGER.info("Successfully wrote " + txt + " to the file: " + file.getAbsolutePath());
        } catch (IOException e) {
            ValorantMod.LOGGER.error("An IOException occurred: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    ValorantMod.LOGGER.error("Failed to close the FileWriter: " + e.getMessage());
                }
            }
        }
    }

    public static String readFile(int lineNumber, String folderName, String fileName, String regex, MinecraftServer server) {
        File specificDir = getSpecificDirectory(folderName, server);
        File file = new File(specificDir, fileName);
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(file.toURI()));
        } catch (IOException e) {
            ValorantMod.LOGGER.error("An IOException occurred: " + e.getMessage());
            return null;
        }

        if (lineNumber > 0 && lineNumber <= lines.size()) {
            String line = lines.get(lineNumber - 1);
            Pattern pattern = Pattern.compile(regex + "=(.*)");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    private static List<String> readLinesFromFile(String folderName, String fileName, MinecraftServer server) {
        File specificDir = getSpecificDirectory(folderName, server);
        File file = new File(specificDir, fileName);
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeLinesToFile(List<String> lines, String folderName, String fileName, MinecraftServer server) {
        File specificDir = getSpecificDirectory(folderName, server);
        File file = new File(specificDir, fileName);
        try {
            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void modifyFile(int lineNumber, String key, String newValue, String folderName, String fileName, MinecraftServer server) {
        List<String> lines = readLinesFromFile(folderName, fileName, server);

        if (lines != null && lineNumber > 0 && lineNumber <= lines.size()) {
            String line = lines.get(lineNumber - 1);
            String regex = key + "=(.*)";
            String replacement = key + "=" + newValue;
            line = line.replaceAll(regex, replacement);
            lines.set(lineNumber - 1, line);
            writeLinesToFile(lines, folderName, fileName, server);
            ValorantMod.LOGGER.info("Successfully modified file.");
        } else {
            ValorantMod.LOGGER.error("Line number out of bounds or file not found.");
        }
    }
}
