    private static void addFileToZipArchive(String name, File f, ZipOutputStream zout) throws Exception {
        byte data[] = new byte[2048];
        FileInputStream fi = new FileInputStream(f);
        BufferedInputStream origin = new BufferedInputStream(fi, 2048);
        ZipEntry entry = new ZipEntry(name);
        zout.putNextEntry(entry);
        int count = 0;
        while ((count = origin.read(data, 0, 2048)) != -1) {
            zout.write(data, 0, count);
        }
        origin.close();
    }
