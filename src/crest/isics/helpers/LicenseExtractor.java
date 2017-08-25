package crest.isics.helpers;

import java.io.*;

public class LicenseExtractor {

    public static String extractLicense(String fileName) {
        StringBuilder sbuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = null;
            int index = 0;
            boolean inCommentRegion = false;
            boolean foundFirstComment = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("/*")) {
                    inCommentRegion = true;
                    foundFirstComment = true;
                } else if (line.trim().startsWith("*/")) {
                    inCommentRegion = false;
                }

                if (inCommentRegion) {
                    line = line.trim().replaceAll("\\*+", "").replaceAll("/", "");
                    sbuilder.append(line.trim() + " ");
                }

                if (foundFirstComment && !inCommentRegion)
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return sbuilder.toString();
    }

    public static String extractLicenseWithNinka(String filename) {
        Runtime rt = Runtime.getRuntime();
        String result = "";
        try {
            Process pr = rt.exec("ninka-1.3/ninka.pl -d " + filename);
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line=null;
            while((line=input.readLine()) != null) {
                result += line;
            }

            // delete the generated license file
            File f = new File(filename + ".license");
            if (!f.delete()) {
                System.out.println("ERROR: Can't delete the license file: " + filename + ".license");
            }

            int exitVal = pr.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}
