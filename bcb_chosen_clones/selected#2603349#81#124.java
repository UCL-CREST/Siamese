    @NotNull
    private Properties loadProperties() {
        File file = new File(homeLocator.getHomeDir(), configFilename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("IOException while creating \"" + file.getAbsolutePath() + "\".", e);
            }
        }
        if (!file.canRead() || !file.canWrite()) {
            throw new RuntimeException("Cannot read and write from file: " + file.getAbsolutePath());
        }
        if (lastModifiedByUs < file.lastModified()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("File \"" + file + "\" is newer on disk. Read it ...");
            }
            Properties properties = new Properties();
            try {
                FileInputStream in = new FileInputStream(file);
                try {
                    properties.loadFromXML(in);
                } catch (InvalidPropertiesFormatException e) {
                    FileOutputStream out = new FileOutputStream(file);
                    try {
                        properties.storeToXML(out, comment);
                    } finally {
                        out.close();
                    }
                } finally {
                    in.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("IOException while reading from \"" + file.getAbsolutePath() + "\".", e);
            }
            this.lastModifiedByUs = file.lastModified();
            this.properties = properties;
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("... read done.");
            }
        }
        assert this.properties != null;
        return this.properties;
    }
