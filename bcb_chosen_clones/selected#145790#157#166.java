    public void persist(FreeFormConfigurable ffConfigurable, String relativePath) {
        File file = getConfigFile(ffConfigurable, relativePath, PROPERTIES_CONFIG_EXT);
        InputStream is = ffConfigurable.getInputConfigStream();
        try {
            OutputStream os = new FileOutputStream(file);
            IOUtils.copy(is, os);
        } catch (Exception e) {
            throw new ConfigurationException("Failed to store free from config for class " + ffConfigurable.getClass().getName() + " into file " + file.getAbsolutePath());
        }
    }
