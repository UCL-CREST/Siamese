    static void addFile(String path, File f, ZipOutputStream zos) throws IOException {
        ZipEntry ze = new ZipEntry(path + f.getName());
        ze.setSize(f.length());
        ze.setTime(f.lastModified());
        zos.putNextEntry(ze);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            byte buf[] = new byte[1024];
            int read;
            while ((read = fis.read(buf)) >= 0) {
                zos.write(buf, 0, read);
            }
        } finally {
            if (fis != null) fis.close();
        }
        zos.flush();
        zos.closeEntry();
    }
