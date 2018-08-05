    private static void zipDirectory(File dir, ZipOutputStream zout, String base) throws IOException {
        byte buffer[] = new byte[8192];
        String[] files = dir.list();
        if (files == null || files.length == 0) return;
        for (int i = 0; i < files.length; i++) {
            String path;
            if (base == null) {
                path = files[i];
            } else {
                path = base + "/" + files[i];
            }
            File f = new File(dir, files[i]);
            if (f.isDirectory()) zipDirectory(f, zout, path); else {
                ZipEntry zentry = new ZipEntry(path);
                zout.putNextEntry(zentry);
                FileInputStream inputStream = new FileInputStream(f);
                int len;
                while ((len = inputStream.read(buffer)) != -1) zout.write(buffer, 0, len);
                inputStream.close();
                zout.flush();
                zout.closeEntry();
            }
        }
    }
