package cz.jeme.programu.gungaming.utils;

import cz.jeme.programu.gungaming.GunGaming;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class ResourcepackUtils {

    public static final String TEMP_DIR_PATH = System.getProperty("java.io.tmpdir");
    public static final String TEMP_FILE_NAME = "GunGaming";

    private ResourcepackUtils() {
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

    public static byte[] generateSHA1(URL url) throws NoSuchAlgorithmException, IOException {
        File tempFile = downloadFile(url);
        byte[] digest = generateSHA1(tempFile);
        if (!tempFile.delete()) {
            GunGaming.serverLog(Level.WARNING, "The resourcepack tempfile couldn't be removed!");
        }
        return digest;
    }

    public static byte[] generateSHA1(String urlStr) throws NoSuchAlgorithmException, IOException {
        return generateSHA1(new URL(urlStr));
    }

    public static File downloadFile(URL url) throws IOException {
        File tempFile = new File(TEMP_DIR_PATH + File.separatorChar + TEMP_FILE_NAME);
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        fileOutputStream.close();
        return tempFile;
    }
}
