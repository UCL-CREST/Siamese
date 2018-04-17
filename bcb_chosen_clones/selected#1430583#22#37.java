    private static void add(ZipOutputStream zos, File file, String root) throws IOException {
        String name = file.getPath();
        name = name.substring(root.length());
        ZipEntry ze = new ZipEntry(name);
        zos.putNextEntry(ze);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[100000];
        int read = fis.read(data);
        while (read > 0) {
            zos.write(data, 0, read);
            read = fis.read(data);
        }
        zos.closeEntry();
        fis.close();
        file.delete();
    }
