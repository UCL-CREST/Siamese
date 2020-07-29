    public static byte[] readRawFile(String path) throws IOException {
        String fullPath = WebAppInitializer.ROOT + path;
        LOGGER.log(Level.INFO, "Reading raw file: " + fullPath);
        InputStream stream = new FileInputStream(fullPath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int byt = -1;
        while ((byt = stream.read()) != -1) {
            outStream.write(byt);
        }
        stream.close();
        LOGGER.log(Level.INFO, "File read");
        return outStream.toByteArray();
    }
