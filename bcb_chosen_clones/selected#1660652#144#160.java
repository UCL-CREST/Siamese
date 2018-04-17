    private void writeZip(File file, ZipOutputStream zout) throws IOException {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                writeZip(files[i], zout);
            } else {
                FileInputStream fis = new FileInputStream(files[i]);
                zout.putNextEntry(new ZipEntry(files[i].getPath().substring(BUILD_DIR.length())));
                byte[] buf = new byte[1024];
                int begin;
                while ((begin = fis.read(buf)) != -1) {
                    zout.write(buf, 0, begin);
                }
                fis.close();
            }
        }
    }
