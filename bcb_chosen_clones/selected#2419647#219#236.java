    private void writeFileToZipFile(File file, String zipFilePath, ZipOutputStream zipOutput) throws IOException {
        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        zipOutput.putNextEntry(zipEntry);
        BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream(file));
        try {
            byte[] buffer = new byte[1024];
            while (bufferedInput.available() > 0) {
                int read = bufferedInput.read(buffer);
                zipOutput.write(buffer, 0, read);
            }
        } finally {
            try {
                bufferedInput.close();
            } catch (Exception e) {
            }
        }
        zipOutput.closeEntry();
    }
