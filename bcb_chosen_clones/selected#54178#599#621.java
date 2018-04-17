    private void archiveDirectory(ZipOutputStream out, File dir, String path) throws IOException {
        byte[] buf = new byte[16384];
        File[] files = dir.listFiles();
        if (files.length > 0) {
            for (int x = 0; x < files.length; x++) {
                if (files[x].isFile()) {
                    FileInputStream in = new FileInputStream(files[x]);
                    out.putNextEntry(new ZipEntry(path + files[x].getName()));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                } else {
                    archiveDirectory(out, files[x], path + files[x].getName() + File.separator);
                }
            }
        } else {
            out.putNextEntry(new ZipEntry(path));
            out.closeEntry();
        }
    }
