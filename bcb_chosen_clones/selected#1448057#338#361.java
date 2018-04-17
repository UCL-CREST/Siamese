    public static boolean exportStandalone(String projectDirectory, String destinyJARPath) {
        boolean exported = true;
        try {
            File destinyJarFile = new File(destinyJARPath);
            FileOutputStream mergedFile = new FileOutputStream(destinyJarFile);
            ZipOutputStream os = new ZipOutputStream(mergedFile);
            String manifest = Writer.defaultManifestFile("es.eucm.eadventure.engine.EAdventureStandalone");
            ZipEntry manifestEntry = new ZipEntry("META-INF/MANIFEST.MF");
            os.putNextEntry(manifestEntry);
            os.write(manifest.getBytes());
            os.closeEntry();
            os.flush();
            File.mergeZipAndDirToJar("web/eAdventure_temp.jar", projectDirectory, os);
            addNeededLibrariesToJar(os, Controller.getInstance());
            os.close();
        } catch (FileNotFoundException e) {
            exported = false;
            ReportDialog.GenerateErrorReport(e, true, "UNKNOWNERROR");
        } catch (IOException e) {
            exported = false;
            ReportDialog.GenerateErrorReport(e, true, "UNKNOWNERROR");
        }
        return exported;
    }
