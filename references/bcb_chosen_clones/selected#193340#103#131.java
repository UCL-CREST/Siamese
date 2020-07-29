    public static void zip(File destFile, File[] files) throws IOException {
        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(destFile);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        out.setMethod(ZipOutputStream.DEFLATED);
        byte[] data = new byte[BUFFER_SIZE];
        for (int i = 0; i < files.length; i++) {
            if (log.isDebugEnabled()) {
                log.debug("Adding: " + files[i].getName());
            }
            if (files[i].isDirectory()) {
                if (log.isDebugEnabled()) {
                    log.debug("Skipping directory: " + files[i]);
                }
                continue;
            }
            FileInputStream fi = new FileInputStream(files[i]);
            origin = new BufferedInputStream(fi, BUFFER_SIZE);
            ZipEntry entry = new ZipEntry(files[i].getName());
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }
        out.flush();
        out.close();
    }
