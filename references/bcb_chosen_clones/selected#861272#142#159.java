    private void addFile(ZipOutputStream zip, File file) throws IOException {
        byte[] buf = new byte[4096];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            String path = getPath(file);
            zip.putNextEntry(new ZipEntry(path));
            int len;
            while ((len = in.read(buf)) > 0) {
                zip.write(buf, 0, len);
            }
            zip.closeEntry();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
