    private static void addFileToZip(final String fileName, final String pathToFileInZipFile, final ZipOutputStream zipOutStream, final byte[] copyBuffer) throws IOException {
        BufferedInputStream inStream = null;
        try {
            inStream = new BufferedInputStream(new FileInputStream(fileName));
            final ZipEntry entry = new ZipEntry(pathToFileInZipFile);
            zipOutStream.putNextEntry(entry);
            int bytesRead;
            while ((bytesRead = inStream.read(copyBuffer)) != -1) {
                zipOutStream.write(copyBuffer, 0, bytesRead);
            }
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (final IOException e) {
                    ;
                }
            }
        }
    }
