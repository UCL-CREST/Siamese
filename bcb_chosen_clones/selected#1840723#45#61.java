    protected void processFile(File file, Path path) {
        try {
            ZipEntry ze = new ZipEntry(path.isRoot() ? file.getName() : path.toString());
            ze.setTime(file.lastModified());
            ze.setSize(file.length());
            zout.putNextEntry(ze);
            FileInputStream fis = new FileInputStream(file);
            int len;
            while ((len = fis.read(buf)) != -1) {
                zout.write(buf, 0, len);
            }
            fis.close();
            zout.closeEntry();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
