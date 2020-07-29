    private MimeTypesProvider() {
        File mimeTypesFile = new File(XPontusConstantsIF.XPONTUS_HOME_DIR, "mimes.properties");
        try {
            if (!mimeTypesFile.exists()) {
                OutputStream os = null;
                InputStream is = getClass().getResourceAsStream("/net/sf/xpontus/configuration/mimetypes.properties");
                os = FileUtils.openOutputStream(mimeTypesFile);
                IOUtils.copy(is, os);
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }
            provider = new XPontusMimetypesFileTypeMap(mimeTypesFile.getAbsolutePath());
            MimetypesFileTypeMap.setDefaultFileTypeMap(provider);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
