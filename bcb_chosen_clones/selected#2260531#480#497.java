    public static void addFileToZip(ZipOutputStream zos, File file, String leadPath) throws IOException {
        if (leadPath == null) {
            leadPath = new String("");
        }
        if (file.isDirectory()) {
            return;
        }
        ZipEntry ze = new ZipEntry(leadPath + file.getName());
        zos.putNextEntry(ze);
        FileInputStream in = new FileInputStream(file);
        int c;
        byte[] b = new byte[1024];
        while ((c = in.read(b)) != -1) {
            zos.write(b, 0, c);
        }
        in.close();
        zos.closeEntry();
    }
