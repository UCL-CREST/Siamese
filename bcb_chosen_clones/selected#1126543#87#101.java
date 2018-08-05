    public static void zip(String files[], String target) throws IOException {
        byte b[] = new byte[512];
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(target));
        for (int i = 0; i < files.length; i++) {
            InputStream in = new FileInputStream(files[i]);
            ZipEntry e = new ZipEntry(files[i].replace(File.separatorChar, '/'));
            zout.putNextEntry(e);
            int len = 0;
            while ((len = in.read(b)) != -1) {
                zout.write(b, 0, len);
            }
            zout.closeEntry();
        }
        zout.close();
    }
