        private void packDir(File dir, File rel, ZipOutputStream zout) throws IOException {
            byte[] buffer = new byte[65536];
            ZipEntry e = new ZipEntry(IO.relativePath(dir, rel) + '/');
            e.setTime(dir.lastModified());
            zout.putNextEntry(e);
            for (File f : dir.listFiles(new FileFilter() {

                public boolean accept(File test) {
                    String s = test.getName();
                    return !s.equals(".DS_Store") && !s.toLowerCase().equals(".svn") && !s.toLowerCase().equals(".cvs");
                }
            })) {
                if (f.isDirectory()) {
                    packDir(f, rel, zout);
                } else {
                    e = new ZipEntry(IO.relativePath(f, rel));
                    e.setTime(f.lastModified());
                    zout.putNextEntry(e);
                    InputStream in = new BufferedInputStream(new FileInputStream(f));
                    try {
                        int bytes;
                        while ((bytes = in.read(buffer)) >= 0) {
                            zout.write(buffer, 0, bytes);
                        }
                        zout.closeEntry();
                    } finally {
                        in.close();
                    }
                }
            }
        }
