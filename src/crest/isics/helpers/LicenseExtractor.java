package crest.isics.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
//                System.out.println(line);
                result += line;
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
