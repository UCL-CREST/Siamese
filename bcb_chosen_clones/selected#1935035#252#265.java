    private void addToOutputStream(ZipOutputStream output, InputStream input, ZipEntry ze) throws IOException {
        try {
            output.putNextEntry(ze);
        } catch (ZipException zipEx) {
            input.close();
            return;
        }
        int numBytes = -1;
        while ((numBytes = input.read(buffer)) > 0) {
            output.write(buffer, 0, numBytes);
        }
        output.closeEntry();
        input.close();
    }
