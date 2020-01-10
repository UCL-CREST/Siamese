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

import java.io.BufferedReader;
import java.io.File;
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

    public static String extractLicenseWithRegExp(String license) {
        if (license.toLowerCase().contains("mit license") ||
            license.toLowerCase()
                    .contains("the software is provided \"as is\", without warranty of any kind, express or implied"))
            return "mit";

        else if (license.trim().toLowerCase().startsWith("gnu lesser general public license") ||
                license.toLowerCase()
                        .contains("the terms of the gnu lesser public license")) {
            if (license.toLowerCase().contains("version 2.1"))
                return "lgpl-2.1";
            else if (license.toLowerCase().contains("version 3"))
                return "lgpl-3.0";
        }

        else if (license.trim().toLowerCase().startsWith("gnu general public license") ||
                    license.toLowerCase()
                            .contains("the terms of the gnu general public license")) {
            if (license.toLowerCase().contains("version 2"))
                return "gpl-2.0";
            else if (license.toLowerCase().contains("version 3"))
                return "gpl-3.0";
            else
                return "gpl";
        }

        else if ((license.trim().toLowerCase().startsWith("gnu affero general public license") ||
                license.toLowerCase()
                        .contains("the terms of the gnu affero public license"))
                && license.toLowerCase().contains("version 3"))
            return "agpl-3.0";

        else if (license.toLowerCase().contains("apache license, version 2.0"))
            return "apache-2.0";
        else if (license.toLowerCase().contains("apache license"))
            return "apache";
        else if (license.toLowerCase().contains("redistribution and use in source and binary forms")) {
            int clauseCount = 0;
            if (license.toLowerCase().contains("redistributions of source code"))
                clauseCount++;
            if (license.toLowerCase().contains("redistributions in binary form"))
                clauseCount++;
            if (license.toLowerCase().contains("neither the name"))
                clauseCount++;
            if (license.toLowerCase()
                    .contains("no express or implied licenses to any party's patent rights are granted by this license"))
                clauseCount++;

            if (clauseCount == 2)
                return "bsd-2-clause";
            else if (clauseCount == 3)
                return "bsd-3-clause";
            else if (clauseCount == 4)
                return "bsd-3-clause-clear";
        }
        else if (license.toLowerCase().contains("http://unlicense.org"))
            return "unlicense";

        else if (license.toLowerCase().contains("mozilla public license version 2.0"))
            return "mpl-2.0";
        else if (license.toLowerCase().contains("open software license version 3.0"))
            return "osl-3.0";
        else if (license.toLowerCase().contains("academic free license version 3.0"))
            return "afl-3.0";
        else if (license.toLowerCase().contains("artistic license 2.0"))
            return "artistic-2.0";
        else if (license.toLowerCase().contains("cc0 1.0"))
            return "cc0-1.0";
        else if (license.toLowerCase().contains("creative commons attribution 4.0"))
            return "cc-by-4.0";
        else if (license.toLowerCase().contains("attribution-noncommercial-sharealike 4.0")
        || license.toLowerCase().contains("attribution-sharealike 4.0"))
            return "cc-by-sa-4.0";
        else if (license.toLowerCase().contains("do what the fuck you want to"))
            return "wtfpl";
        else if (license.toLowerCase().contains("educational community license, version 2.0")
                || license.toLowerCase().contains("ecl-2.0"))
            return "ecl-2.0";
        else if (license.toLowerCase().contains("eclipse public license - v 1.0"))
            return "epl-1.0";
        else if (license.toLowerCase().contains("eclipse public license - v 2.0"))
            return "epl-2.0";
        else if (license.toLowerCase().contains("eupl v.1.1"))
            return "eupl-1.1";
        else if (license.toLowerCase().contains("isc license"))
            return "isc";
        else if (license.toLowerCase().contains("lppl version 1.3c"))
            return "lppl-1.3c";
        else if (license.toLowerCase().contains("microsoft public license")
                || license.toLowerCase().contains("(ms-pl)"))
            return "ms-pl";
        else if (license.toLowerCase().contains("in no event shall")
                && license.toLowerCase()
                .contains("be liable to any party for direct, indirect, special, incidental, or consequential damages"))
            return "postgresql";
        else if (license.toLowerCase().contains("sil open font license")
                && license.toLowerCase().contains("version 1.1"))
            return "ofl-1.1";
        else if (license.toLowerCase().contains("ncsa open source license"))
            return "ncsa";
        else if (license.toLowerCase()
                .contains("altered source versions must be plainly marked as such") &&
                license.toLowerCase().contains("misrepresented as being the original software"))
            return "zlib";
        else
            return "unknown";

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
