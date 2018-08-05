    private static void zip(File dir, File base, ZipOutputStream out) throws IOException {
        File[] files = dir.listFiles();
        byte[] buffer = new byte[8192];
        final String FILE_SEP = System.getProperty("file.separator");
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                zip(files[i], base, out);
            } else {
                FileInputStream fin = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(files[i].getPath().substring(base.getPath().length() + 1).replace(FILE_SEP, "/"));
                out.putNextEntry(entry);
                int bytes_read = 0;
                while ((bytes_read = fin.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                fin.close();
            }
        }
    }
