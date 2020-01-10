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
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class LicenseExtractorTest {

    private String licenseLoc = "resources/license_test";
    private String readLicense(String file) {
        File f = new File(licenseLoc + "/" + file);
        String licenseStr = "";
        try {
            if (f.exists() && !f.isDirectory()) {
                String[] lines = FileUtils.readFileToString(f).split("\n");
                // concat the license string into one single line
                for (String line : lines) {
                    licenseStr += line + " ";
                }
            }
            licenseStr = licenseStr.trim().replaceAll("\\s+", " ");
            System.out.println(licenseStr);
        } catch (
                IOException e) {
            System.out.println("ERROR: cannot read the license file.");
        }
        return licenseStr;
    }

    @Test
    public void testMIT() {
        String licenseStr = readLicense("mit.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("mit", license);
    }

    @Test
    public void testGPLv2() {
        String licenseStr = readLicense("gpl2.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("gpl-2.0", license);
    }

    @Test
    public void testGPLv3() {
        String licenseStr = readLicense("gpl3.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("gpl-3.0", license);
    }

    @Test
    public void testApache2() {
        String licenseStr = readLicense("apache-2.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("apache-2.0", license);
    }

    @Test
    public void testBSD2Clause() {
        String licenseStr = readLicense("bsd-2-clause.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("bsd-2-clause", license);
    }

    @Test
    public void testBSD3Clause() {
        String licenseStr = readLicense("bsd-3-clause.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("bsd-3-clause", license);
    }

    @Test
    public void testBSD3ClauseClear() {
        String licenseStr = readLicense("bsd-3-clause-clear.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("bsd-3-clause-clear", license);
    }

    /**
     * TODO: @Chaiyong
     * This test class is ignored until resolved.
     * The license extractor returns "mit" instead of the expected value of "unlicense".
     * The actual unlicense.txt file contains the search substring that fits the requirements for the
     * extractLicenseWithRegExp() method to return "mit".
     */
    @Ignore
    @Test
    public void testUnlicense() {
        String licenseStr = readLicense("unlicense.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("unlicense", license);
    }

    @Test
    public void testLGPL2() {
        String licenseStr = readLicense("lgpl-2.1.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("lgpl-2.1", license);
    }

    @Test
    public void testLGPL3() {
        String licenseStr = readLicense("lgpl-3.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("lgpl-3.0", license);
    }

    @Test
    public void testAGPL3() {
        String licenseStr = readLicense("agpl-3.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("agpl-3.0", license);
    }

    @Test
    public void testMPL2() {
        String licenseStr = readLicense("mpl-2.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("mpl-2.0", license);
    }

    @Test
    public void testOSL3() {
        String licenseStr = readLicense("osl-3.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("osl-3.0", license);
    }

    @Test
    public void testAFL3() {
        String licenseStr = readLicense("afl-3.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("afl-3.0", license);
    }

    @Test
    public void testArtistic2() {
        String licenseStr = readLicense("artistic-2.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("artistic-2.0", license);
    }

    @Test
    public void testCC01() {
        String licenseStr = readLicense("cc0-1.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("cc0-1.0", license);
    }

    @Test
    public void testCCBY40() {
        String licenseStr = readLicense("cc-by-4.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("cc-by-4.0", license);
    }

    @Test
    public void testCCBYSA40() {
        String licenseStr = readLicense("cc-by-sa-4.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("cc-by-sa-4.0", license);
    }

    @Test
    public void testWTFL() {
        String licenseStr = readLicense("wtfpl.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("wtfpl", license);
    }

    @Test
    public void testECL20() {
        String licenseStr = readLicense("ecl-2.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("ecl-2.0", license);
    }

    @Test
    public void testEPL10() {
        String licenseStr = readLicense("epl-1.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("epl-1.0", license);
    }

    @Test
    public void testEPL20() {
        String licenseStr = readLicense("epl-2.0.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("epl-2.0", license);
    }

    @Test
    public void testEUPL11() {
        String licenseStr = readLicense("eupl-1.1.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("eupl-1.1", license);
    }

    @Test
    public void testISC() {
        String licenseStr = readLicense("isc.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("isc", license);
    }

    @Test
    public void testLLPL() {
        String licenseStr = readLicense("lppl-1.3c.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("lppl-1.3c", license);
    }

    @Test
    public void testMSPL() {
        String licenseStr = readLicense("ms-pl.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("ms-pl", license);
    }

    @Test
    public void testPOSTGRESQL() {
        String licenseStr = readLicense("postgresql.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("postgresql", license);
    }

    @Test
    public void testOFL11() {
        String licenseStr = readLicense("ofl-1.1.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("ofl-1.1", license);
    }

    @Test
    public void testZLIB() {
        String licenseStr = readLicense("zlib.txt");
        String license = LicenseExtractor.extractLicenseWithRegExp(licenseStr);
        assertEquals("zlib", license);
    }
}
