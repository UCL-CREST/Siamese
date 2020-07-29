    private void addDirectoryToZip(ZipOutputStream out, File dir, String path) throws IOException {
        byte[] buf = new byte[1024];
        File[] children = dir.listFiles();
        for (File f : children) {
            if (f.isDirectory()) {
                out.putNextEntry(new ZipEntry(path + f.getName() + '/'));
                out.closeEntry();
                addDirectoryToZip(out, f, path + f.getName() + '/');
            } else {
                FileInputStream in = new FileInputStream(f);
                out.putNextEntry(new ZipEntry(path + f.getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
                f.deleteOnExit();
            }
        }
    }
