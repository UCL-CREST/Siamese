    private static void zip(File[] files, File base, ZipOutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        final String FILE_SEP = System.getProperty("file.separator");
        for (int i = 0; i < files.length; i++) {
            File file_on_disk = new File(base.getPath() + FILE_SEP + files[i].getPath());
            if (file_on_disk.isDirectory()) {
                zip(file_on_disk, base, out);
            } else {
                FileInputStream fin = new FileInputStream(file_on_disk);
                ZipEntry entry = new ZipEntry(files[i].getPath().replace(FILE_SEP, "/"));
                out.putNextEntry(entry);
                int bytes_read = 0;
                while ((bytes_read = fin.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                fin.close();
            }
        }
    }
