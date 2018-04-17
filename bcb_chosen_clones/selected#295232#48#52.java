    private static void saveSettings(String destinationFileName, byte[] content) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(destinationFileName);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
        IOUtils.copy(byteArrayInputStream, fileOutputStream);
    }
