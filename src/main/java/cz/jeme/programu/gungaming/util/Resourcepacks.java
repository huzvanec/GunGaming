package cz.jeme.programu.gungaming.util;

import cz.jeme.programu.gungaming.GunGaming;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileAlreadyExistsException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class Resourcepacks {

    public static final String RESOURCEPACK_FILE_NAME = "resourcepack_tmp.zip";

    private Resourcepacks() {
        // Only static utils
    }

    public static byte[] generateSHA1(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        InputStream fileInputStream = new FileInputStream(file);
        int i = 0;
        int bufferSize = 10 * 1024; // 10 kilobytes
        byte[] byteBuffer = new byte[bufferSize];
        while (i != -1) {
            i = fileInputStream.read(byteBuffer);
            if (i > 0) {
                messageDigest.update(byteBuffer, 0, i);
            }
        }
        fileInputStream.close();
        return messageDigest.digest();
    }

    public static byte[] generateSHA1(URL url, File dataFolder) throws NoSuchAlgorithmException, IOException {
        File tempFile = downloadFile(url, dataFolder);
        byte[] digest = generateSHA1(tempFile);
        if (!tempFile.delete()) {
            GunGaming.serverLog(Level.WARNING, "The resourcepack tempfile couldn't be removed!");
        }
        return digest;
    }

    public static byte[] generateSHA1(String urlStr, File dataFolder) throws NoSuchAlgorithmException, IOException {
        return generateSHA1(new URL(urlStr), dataFolder);
    }

    public static File downloadFile(URL url, File datafolder) throws IOException {
        File tempFile = new File(datafolder.getAbsolutePath() + File.separatorChar + RESOURCEPACK_FILE_NAME);
        if (tempFile.exists()) {
            boolean deleted = tempFile.delete();
            if (!deleted) {
                throw new FileAlreadyExistsException("Resourcepack file already exists and cannot be deleted!");
            }
        }
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        fileOutputStream.close();
        return tempFile;
    }
}