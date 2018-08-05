    protected void createSettingsIfNecessary() throws IOException {
        OutputStream out = null;
        try {
            final File fSettings = SettingsUtils.getSettingsFile();
            if (!fSettings.exists()) {
                fSettings.createNewFile();
                final Path src = new Path("mvn/settings.xml");
                final InputStream in = FileLocator.openStream(getBundle(), src, false);
                out = new FileOutputStream(SettingsUtils.getSettings(), true);
                IOUtils.copy(in, out);
            } else {
                Logger.getLog().info("File settings.xml already exists at " + fSettings);
            }
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
