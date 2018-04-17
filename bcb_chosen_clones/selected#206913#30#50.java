    public static PortalConfig install(File xml, File dir) throws IOException, ConfigurationException {
        if (!dir.exists()) {
            log.info("Creating directory {}", dir);
            dir.mkdirs();
        }
        if (!xml.exists()) {
            log.info("Installing default configuration to {}", xml);
            OutputStream output = new FileOutputStream(xml);
            try {
                InputStream input = ResourceLoader.open("res://" + PORTAL_CONFIG_XML);
                try {
                    IOUtils.copy(input, output);
                } finally {
                    input.close();
                }
            } finally {
                output.close();
            }
        }
        return create(xml, dir);
    }
