    private static final void compressFile(final ZipOutputStream out, String parentFolder, final File file, int niveau, boolean racine) throws IOException {
        String zipName = new StringBuilder(parentFolder).append(file.getName()).append(file.isDirectory() ? '/' : "").toString();
        if (racine) {
            zipName = new StringBuilder().append(file.getName()).append(file.isDirectory() ? '/' : "").toString();
        }
        if (!file.isDirectory() || niveau != 0) {
            final ZipEntry entry = new ZipEntry(zipName);
            entry.setSize(file.length());
            entry.setTime(file.lastModified());
            out.putNextEntry(entry);
            racine = false;
        }
        niveau++;
        if (file.isDirectory()) {
            for (final File f : file.listFiles()) compressFile(out, zipName.toString(), f, niveau, racine);
            return;
        }
        final InputStream in = new BufferedInputStream(new FileInputStream(file));
        try {
            final byte[] buf = new byte[8192];
            int bytesRead;
            while (-1 != (bytesRead = in.read(buf))) {
                out.write(buf, 0, bytesRead);
            }
        } finally {
            in.close();
        }
    }
