package crest.isics.helpers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyUtils {
    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    public static void writeToFile(String location, String filename, String content, boolean isAppend) {
        if (createDir(location)) {
            /* copied from https://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/ */
            BufferedWriter bw = null;
            FileWriter fw = null;

            try {
                fw = new FileWriter(location + "/" + filename, isAppend);
                bw = new BufferedWriter(fw);
                bw.write(content);
                if (!isAppend)
                    System.out.println("Saved as: " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null)
                        bw.close();
                    if (fw != null)
                        fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        } else {
            System.out.println("ERROR: can't create a directory at: " + location);
        }
    }

    public static boolean createDir(String location) {
        try {
            Files.createDirectories(Paths.get(location));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
