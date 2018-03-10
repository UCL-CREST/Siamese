import de.uni_bremen.st.rcf.model.*;
import de.uni_bremen.st.rcf.persistence.AbstractPersistenceManager;
import de.uni_bremen.st.rcf.persistence.PersistenceManagerFactory;

import java.io.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String outfile = "bellon_clones.csv";
        String outToFile = "system,file1,start1,end1,file2,start2,end2,type\n";
        String[] systems = { "eclipse-jdtcore.rcf", "netbeans-javadoc.rcf", "java-swing.rcf" };
        int count = 1;
        int total = 0;
//        String[] systems = { "java-swing.rcf" };
        for (String s: systems) {
            String rcfFile = "bellon_benchmark/" + s;
            System.out.println("File: " + rcfFile);
            File file = new java.io.File(rcfFile);
            AbstractPersistenceManager apm = null;
            try {
                apm = PersistenceManagerFactory.getPersistenceManager(file);
                RCF rcf = apm.load(file);
                Version v = rcf.getVersions().getFirstEntry();
//                for (Version v: rcf.getVersions()) {
                    System.out.println("Version: " + v.getId());
                    System.out.println("Pairs: " + v.getClonePairs().size());
                    total += v.getClonePairs().size();
                    for (ClonePair cp : v.getClonePairs()) {
                        Fragment left = cp.getLeft();
                        int leftStart = left.getStart().getLine();
                        int leftEnd = left.getEnd().getLine();
                        Fragment right = cp.getRight();
                        int rightStart = right.getStart().getLine();
                        int rightEnd = right.getEnd().getLine();
                        int type = cp.getType();

                        // filter only clones with >= 10 lines
                        if ((leftEnd - leftStart + 1) >= 10 && (rightEnd - rightStart + 1) >= 10) {
                            outToFile += count + "," + s + ",";
                            outToFile += left.getStart().getFile().getAbsolutePath() + "," +
                                    leftStart + "," + leftEnd + ",";
                            outToFile += right.getStart().getFile().getAbsolutePath() + "," +
                                    rightStart + "," + rightEnd + ",";
                            outToFile += type + "\n";
                            try {
                                saveClone(count, readFile(left.getStart().getFile().getAbsolutePath()),
                                        left.getStart().getLine(), left.getEnd().getLine());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            count++;
                        }
                    }
//                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        writeToFile(".", outfile, outToFile, true);
        System.out.println("Done processing " + total + " clone pairs.");
        System.out.println("Selected " + (count - 1) + " clone pairs.");
    }

    public static void writeToFile(String location, String filename, String content, boolean isAppend) {
        /* copied from https://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/ */
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(location + "/" + filename, isAppend);
            bw = new BufferedWriter(fw);
            bw.write(content);
//            if (!isAppend)
//                System.out.println("Saved as: " + filename);
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

    public static void saveClone(int id, String[] lines, int start, int end) {
        String cloneStr = "";
        for (int i=start-1; i<end; i++) {
            cloneStr += lines[i] + "\n";
        }
        writeToFile("queries", id + ".java", cloneStr,false);
    }

    public static String[] readFile(String filename) throws IOException {
        Path filePath = new File(filename).toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        String[] stringArray = stringList.toArray(new String[]{});
        return stringArray;
    }
}
