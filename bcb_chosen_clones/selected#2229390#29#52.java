    public void load(String filename) throws VisbardException {
        String defaultFilename = VisbardMain.getSettingsDir() + File.separator + DEFAULT_SETTINGS_FILE;
        File defaultFile = new File(defaultFilename);
        InputStream settingsInStreamFromFile = null;
        try {
            sLogger.info("Loading settings from : " + defaultFilename);
            settingsInStreamFromFile = new FileInputStream(defaultFile);
        } catch (FileNotFoundException fnf) {
            sLogger.info("Unable to load custom settings from user's settings directory (" + fnf.getMessage() + "); reverting to default settings");
            try {
                InputStream settingsInStreamFromJar = VisbardMain.class.getClassLoader().getResourceAsStream(filename);
                FileOutputStream settingsOutStream = new FileOutputStream(defaultFile);
                int c;
                while ((c = settingsInStreamFromJar.read()) != -1) settingsOutStream.write(c);
                settingsInStreamFromJar.close();
                settingsOutStream.close();
                settingsInStreamFromFile = new FileInputStream(defaultFile);
            } catch (IOException ioe) {
                sLogger.warn("Unable to copy default settings to user's settings directory (" + ioe.getMessage() + "); using default settings from ViSBARD distribution package");
                settingsInStreamFromFile = VisbardMain.class.getClassLoader().getResourceAsStream(filename);
            }
        }
        this.processSettingsFile(settingsInStreamFromFile, filename);
    }
