    private static void doZip(ZipEntry entry, File file, ZipOutputStream out) throws IOException {
        out.putNextEntry(entry);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int buff = 0;
        while (0 < (buff = fis.read(buffer))) {
            out.write(buffer, 0, buff);
        }
        fis.close();
        out.closeEntry();
        out.flush();
    }
