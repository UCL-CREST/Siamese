    public static void copyResource(String src, File dest) throws IOException {
        InputStream fin = ResourceLoader.openResource(src);
        if (fin == null) {
            LogFactory.getLog(ResourceLoader.class).error("Failed to load resource '" + src + "'");
            LogFactory.getLog(ResourceLoader.class).info("Attempting to open file from from file system");
            fin = new FileInputStream(src);
        }
        if (fin == null) {
            LogFactory.getLog(ResourceLoader.class).warn("Giving up on copy operation, can't find source '" + src + "'");
            return;
        }
        BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(dest));
        byte[] buffer = new byte[COPY_BUFFER_SIZE];
        int readBytes = fin.read(buffer);
        while (readBytes > -1) {
            fout.write(buffer, 0, readBytes);
            readBytes = fin.read(buffer);
        }
        fout.close();
        fin.close();
    }
