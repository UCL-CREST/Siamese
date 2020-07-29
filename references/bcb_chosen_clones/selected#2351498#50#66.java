    private void zipingTemp(String path) throws IOException {
        FileInputStream fin;
        byte[] buf = new byte[512];
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path));
        String[] files = new File(tempdir).list();
        for (int i = 0; i < files.length; i++) {
            fin = new FileInputStream(tempdir + "/" + files[i]);
            zout.putNextEntry(new ZipEntry(files[i]));
            int len;
            while ((len = fin.read(buf)) > 0) {
                zout.write(buf, 0, len);
            }
            zout.closeEntry();
            fin.close();
        }
        zout.close();
    }
