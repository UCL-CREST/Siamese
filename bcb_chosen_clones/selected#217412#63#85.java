    public static void zipToStream(OutputStream ops, String[] files, String[] zipPath) throws IOException {
        String[] zp = zipPath == null ? files : zipPath;
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(ops));
        byte data[] = new byte[BUFFER];
        for (int i = 0; i < files.length; i++) {
            if (files[i] == null) {
                continue;
            }
            FileInputStream fi = new FileInputStream(files[i]);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(zp[i]);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            out.closeEntry();
            origin.close();
        }
        out.finish();
        out.flush();
    }
