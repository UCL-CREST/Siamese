    public static void compressFile(ZipOutputStream zos, InputStream is, String entryName) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        int len = 0;
        while ((len = is.read()) != -1) {
            zos.write(len);
        }
        is.close();
    }
