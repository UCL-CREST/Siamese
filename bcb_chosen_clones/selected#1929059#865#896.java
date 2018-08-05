    private static void zip(ZipOutputStream zo, File root, File file) throws IOException {
        String rootPath = root.getAbsolutePath();
        String path = file.getAbsolutePath();
        path = path.replace('\\', '/');
        if (file.isDirectory()) {
            path = path + "/";
        }
        String name = path.substring(rootPath.length());
        if (Utils.isEmptyString(name)) {
            name = file.getName();
        }
        ZipEntry entry = new ZipEntry(name);
        if (file.isFile()) {
            zo.putNextEntry(entry);
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
            byte[] buff = new byte[4096];
            int read;
            while ((read = is.read(buff)) > 0) {
                zo.write(buff, 0, read);
            }
            zo.closeEntry();
        } else if (file.isDirectory()) {
            zo.putNextEntry(entry);
            zo.closeEntry();
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File f : files) {
                    zip(zo, root, f);
                }
            }
        }
    }
