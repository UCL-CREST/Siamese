    private static final void toZip(ZipOutputStream out, File file, String path) throws IOException {
        if (file.isFile()) {
            ZipEntry entry = new ZipEntry(path + file.getName());
            out.putNextEntry(entry);
            BufferedInputStream origin = null;
            byte data[] = new byte[BUFFER];
            FileInputStream fi = new FileInputStream(file);
            origin = new BufferedInputStream(fi, BUFFER);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                toZip(out, files[i], path + file.getName() + "/");
            }
        }
    }
