    public static void synchronize(File source, File destination, boolean smart, long chunkSize) throws IOException {
        if (chunkSize <= 0) {
            log.warn("Chunk size must be positive: using default value.");
            chunkSize = DEFAULT_COPY_BUFFER_SIZE;
        }
        if (source.isDirectory()) {
            if (!destination.exists()) {
                if (!destination.mkdirs()) {
                    throw new IOException("Could not create path " + destination);
                }
            } else if (!destination.isDirectory()) {
                throw new IOException("Source and Destination not of the same type:" + source.getCanonicalPath() + " , " + destination.getCanonicalPath());
            }
            String[] sources = source.list();
            Set<String> srcNames = new HashSet<String>(Arrays.asList(sources));
            String[] dests = destination.list();
            for (String fileName : dests) {
                if (!srcNames.contains(fileName)) {
                    delete(new File(destination, fileName));
                }
            }
            for (String fileName : sources) {
                File srcFile = new File(source, fileName);
                File destFile = new File(destination, fileName);
                synchronize(srcFile, destFile, smart, chunkSize);
            }
        } else {
            if (destination.exists() && destination.isDirectory()) {
                delete(destination);
            }
            if (destination.exists()) {
                long sts = source.lastModified() / FAT_PRECISION;
                long dts = destination.lastModified() / FAT_PRECISION;
                if (!smart || sts == 0 || sts != dts || source.length() != destination.length()) {
                    copyFile(source, destination, chunkSize);
                }
            } else {
                copyFile(source, destination, chunkSize);
            }
        }
    }
