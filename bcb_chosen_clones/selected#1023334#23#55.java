    public static final void createZipFile(String sourcePath, String zipPath, String sourceFileName, String zipFileName) throws Exception {
        ZipOutputStream zipOutStream = null;
        FileInputStream fin = null;
        try {
            zipOutStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipPath + File.separator + zipFileName)));
            byte data[] = new byte[BUFFER_SIZE];
            File file = new File(sourcePath + File.separator + sourceFileName);
            ZipEntry entry = new ZipEntry(sourceFileName);
            zipOutStream.putNextEntry(entry);
            fin = new FileInputStream(file);
            int bytesRead;
            while ((bytesRead = fin.read(data, 0, BUFFER_SIZE)) > 0) {
                zipOutStream.write(data, 0, bytesRead);
            }
            fin.close();
            fin = null;
            zipOutStream.closeEntry();
            zipOutStream.flush();
            zipOutStream.close();
            zipOutStream = null;
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (fin != null) {
                fin.close();
                fin = null;
            }
            if (zipOutStream != null) {
                zipOutStream.close();
                zipOutStream = null;
            }
        }
    }
