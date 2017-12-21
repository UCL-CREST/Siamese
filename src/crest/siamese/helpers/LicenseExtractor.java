package crest.siamese.helpers;

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

    public static String extractLicenseWithRegExp(String license) {
        if (license.contains("MIT License"))
            return "mit";
        else if (license.contains("GNU GENERAL PUBLIC LICENSE")) {
            if (license.contains("Version 2"))
                return "gpl-2.0";
            else if (license.contains("Version 3"))
                return "gpl-3.0";
            else
                return "unknown";
        }
        else if (license.contains("Apache License, Version 2.0"))
            return "apache-2.0";
        else if (license.contains("BSD")) {
            int clauseCount = 0;
            if (license.contains("Redistributions of source code"))
                clauseCount++;
            if (license.contains("Redistributions in binary form"))
                clauseCount++;
            if (license.contains("Neither the name"))
                clauseCount++;
            if (license.contains("NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE"))
                clauseCount++;

            if (clauseCount == 2)
                return "bsd-2-clause";
            else if (clauseCount == 3)
                return "bsd-3-clause";
            else if (clauseCount == 4)
                return "bsd-3-clause-clear";
            else
                return "unknown";
        }
        else if (license.contains("http://unlicense.org"))
            return "unlicense";

        else if (license.contains("GNU LESSER GENERAL PUBLIC LICENSE")) {
            if (license.contains("Version 2.1"))
                return "lgpl-2.1";
            else if (license.contains("Version 3"))
                return "lgpl-3.0";
            else
                return "unknown";
        }
        else if (license.contains("GNU AFFERO GENERAL PUBLIC LICENSE") && license.contains("Version 3"))
            return "agpl-3.0";
        else if (license.contains("Mozilla Public License Version 2.0"))
            return "mpl-2.0";
        else if (license.contains("Open Software License version 3.0"))
            return "osl-3.0";
        else if (license.contains("Academic Free License version 3.0"))
            return "afl-3.0";
        else if (license.contains("Artistic License 2.0"))
            return "artistic-2.0";
        else if (license.contains("CC0 1.0"))
            return "cc0-1.0";
        else if (license.contains("Creative Commons Attribution 4.0"))
            return "cc-by-4.0";
        else if (license.contains("Attribution-NonCommercial-ShareAlike 4.0"))
            return "cc-by-sa-4.0";
        else if (license.contains("DO WHAT THE FUCK YOU WANT TO"))
            return "wtfpl";
        else if (license.contains("Educational Community License, Version 2.0") || license.contains("ECL-2.0"))
            return "ecl-2.0";
        else if (license.contains("Eclipse Public License - v 1.0"))
            return "epl-1.0";
        else if (license.contains("EUPL V.1.1"))
            return "eupl-1.1";
        else if (license.contains("ISC License"))
            return "isc";
        else if (license.contains("LPPL Version 1.3c"))
            return "lppl-1.3c";
        else if (license.contains("Microsoft Public License") || license.contains("(MS-PL)"))
            return "ms-pl";
        else if (license.contains("PostgreSQL License"))
            return "postgresql";
        else if (license.contains("SIL Open Font License") && license.toLowerCase().contains("version 1.1"))
            return "ofl-1.1";
        else if (license.contains("NCSA Open Source License"))
            return "ncsa";
        else if (license.contains("Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software"))
            return "zlib";
        else
            return "unknown";
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
