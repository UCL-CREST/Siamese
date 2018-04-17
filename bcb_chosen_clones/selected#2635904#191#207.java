    private static void zip(File[] files, ZipOutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                zip(files[i], files[i], out);
            } else {
                FileInputStream fin = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(files[i].getPath());
                out.putNextEntry(entry);
                int bytes_read = 0;
                while ((bytes_read = fin.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                fin.close();
            }
        }
    }
