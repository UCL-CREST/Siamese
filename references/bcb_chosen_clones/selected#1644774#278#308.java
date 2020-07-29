    public static ZipOutputStream createZipOutputStream(String baseDir, String[] relativeFilePaths, OutputStream os) throws FileNotFoundException, IOException {
        if (baseDir.startsWith("file:/")) {
            baseDir = baseDir.substring(5);
        }
        ZipOutputStream zipoutputstream = new ZipOutputStream(os);
        zipoutputstream.setMethod(ZipOutputStream.STORED);
        for (int i = 0; i < relativeFilePaths.length; i++) {
            File file = new File(baseDir + FILE_SEPARATOR + relativeFilePaths[i]);
            byte[] buffer = new byte[1000];
            int n;
            FileInputStream fis;
            CRC32 crc32 = new CRC32();
            fis = new FileInputStream(file);
            while ((n = fis.read(buffer)) > -1) {
                crc32.update(buffer, 0, n);
            }
            fis.close();
            ZipEntry zipEntry = new ZipEntry(relativeFilePaths[i]);
            zipEntry.setSize(file.length());
            zipEntry.setTime(file.lastModified());
            zipEntry.setCrc(crc32.getValue());
            zipoutputstream.putNextEntry(zipEntry);
            fis = new FileInputStream(file);
            while ((n = fis.read(buffer)) > -1) {
                zipoutputstream.write(buffer, 0, n);
            }
            fis.close();
            zipoutputstream.closeEntry();
        }
        return zipoutputstream;
    }
