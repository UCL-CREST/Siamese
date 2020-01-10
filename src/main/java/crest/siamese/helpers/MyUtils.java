/*
   Copyright 2018 Chaiyong Ragkhitwetsagul and Jens Krinke

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package crest.siamese.helpers;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            writeToFile(location + "/" + filename, content, isAppend);
        } else {
            System.out.println("ERROR: can't create a directory at: " + location);
        }
    }

    public static void writeToFile(String filePath, String content, boolean isAppend) {
        /* copied from https://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/ */
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(filePath, isAppend);
            bw = new BufferedWriter(fw);
            bw.write(content);
            if (!isAppend)
                System.out.println("Saved as: " + filePath);
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
    }

    public static boolean createDir(String location) {
        try {
            Files.createDirectories(Paths.get(location));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteFile(String folder, String fileName) {
        return deleteFile(folder + "/" + fileName);
    }

    public static boolean deleteFile(String filepath) {
        File f = new File(filepath);
        return f.delete();
    }

    public static boolean copyFile(String fileName, String from, String to) {
        File fromFile = null;
        File toFile = new File(to + "/" + fileName);
        // check the location of the file in the 3 subfolders
        fromFile = new File(from + "/" + fileName);
        try {
            FileUtils.copyFile(fromFile, toFile);
        } catch (IOException e) {
            System.out.println("ERROR: cannot copy file. " + e.getMessage());
            return false;
        }
        return true;
    }

    public static void saveClone(String loc, String filename, String[] lines, int start, int end) {
        String cloneStr = "";
        for (int i=start-1; i<end; i++) {
            cloneStr += lines[i] + "\n";
        }
        writeToFile(loc, filename, cloneStr,false);
    }

    public static String[] readFile(String filename) throws IOException {
        Path filePath = new File(filename).toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        String[] stringArray = stringList.toArray(new String[]{});
        return stringArray;
    }

    public static Date getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
        return date;
    }
}
