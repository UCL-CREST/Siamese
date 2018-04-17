    public static boolean exportAsWebCTObject(String zipFilename, String loName, String authorName, String organization, boolean windowed, String gameFilename, AdventureDataControl adventureData) {
        File tempDir;
        try {
            tempDir = new File(Controller.createTempDirectory().getAbsolutePath());
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
        for (File tempFile : tempDir.listFiles()) {
            if (tempFile.isDirectory()) tempFile.deleteAll();
            tempFile.delete();
        }
        try {
            File jarUnsigned = new File(tempDir.getAbsolutePath() + "/eAdventure.zip");
            FileOutputStream mergedFile = new FileOutputStream(jarUnsigned);
            ZipOutputStream os = new ZipOutputStream(mergedFile);
            String manifestText = Writer.defaultManifestFile("es.eucm.eadventure.engine.EAdventureAppletScorm");
            ZipEntry manifestEntry = new ZipEntry("META-INF/MANIFEST.MF");
            ZipEntry manifestEntry2 = new ZipEntry("META-INF/services/javax.xml.parsers.SAXParserFactory");
            ZipEntry manifestEntry3 = new ZipEntry("META-INF/services/javax.xml.parsers.DocumentBuilderFactory");
            os.putNextEntry(manifestEntry);
            os.write(manifestText.getBytes());
            os.putNextEntry(manifestEntry2);
            os.putNextEntry(manifestEntry3);
            os.closeEntry();
            os.flush();
            File.mergeZipAndDirToJar("web/eAdventure_temp.jar", gameFilename, os);
            addNeededLibrariesToJar(os, Controller.getInstance());
            os.close();
            String fixedLoName = "learningObject";
            jarUnsigned.renameTo(new File(tempDir.getAbsolutePath() + "/" + loName + "_unsigned.jar"));
            File.unzipDir("web/webct_temp.zip", tempDir.getAbsolutePath() + "/");
            JARSigner.signJar(authorName, organization, tempDir.getAbsolutePath() + "/" + loName + "_unsigned.jar", tempDir.getAbsolutePath() + "/CMD_6988980_M/my_files/" + loName + ".jar");
            new File(tempDir.getAbsolutePath() + "/" + loName + "_unsigned.jar").delete();
            writeWebPage(tempDir.getAbsolutePath(), loName, windowed, "es.eucm.eadventure.engine.EAdventureApplet");
            File webpage = new File(tempDir.getAbsolutePath() + "/" + loName + ".html");
            webpage.copyTo(new File(tempDir.getAbsolutePath() + "/CMD_6988980_M/my_files/" + fixedLoName + ".html"));
            webpage.delete();
            File splashScreen = new File("web/splashScreen.gif");
            if (windowed) {
                splashScreen = new File("web/splashScreen_red.gif");
            }
            splashScreen.copyTo(new File(tempDir.getAbsolutePath() + "/CMD_6988980_M/my_files/splashScreen.gif"));
            File.zipDirectory(tempDir.getAbsolutePath() + "/", zipFilename);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
