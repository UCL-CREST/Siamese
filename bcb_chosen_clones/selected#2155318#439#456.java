    protected byte[] readSourceConfigFile(String configFilePath) throws InputOutputException, IOException {
        byte[] sourceConfigFile = new byte[0];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            FileInputStream fis = new FileInputStream(configFilePath);
            try {
                Stream.readTo(fis, os);
                sourceConfigFile = os.toByteArray();
            } finally {
                fis.close();
            }
        } catch (IOException e) {
            throw new InputOutputException(e, configFilePath);
        } finally {
            os.close();
        }
        return sourceConfigFile;
    }
