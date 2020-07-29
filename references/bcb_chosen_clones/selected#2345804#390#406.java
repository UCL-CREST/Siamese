    private static void addToZip(File[] files, ZipOutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addToZip(files[i].listFiles(), out);
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                out.putNextEntry(new ZipEntry(files[i].getPath()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
        }
    }
