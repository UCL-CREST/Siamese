    static void processDirectory(String baseDir, String currentDir, ZipOutputStream zout, FilenameFilter filter) throws IOException {
        File[] files = new File(currentDir).listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    processDirectory(baseDir, f.getPath(), zout, filter);
                } else {
                    String fpath = f.getPath();
                    String fpath2 = fpath.substring(baseDir.length());
                    if (filter == null || filter.accept(f.getParentFile(), f.getName())) {
                        System.out.printf("Adding %s as %s%n", fpath, fpath2);
                        ZipEntry ze = new ZipEntry(fpath2.replace('\\', '/'));
                        ze.setSize(f.length());
                        ze.setTime(f.lastModified());
                        zout.putNextEntry(ze);
                        zout.write(IOUtils.load(f));
                    }
                }
            }
        }
    }
