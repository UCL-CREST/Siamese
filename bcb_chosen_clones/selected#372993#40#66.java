    public static void copyFile(File src, File dest, boolean preserveFileDate) throws IOException {
        if (src.exists() && src.isDirectory()) {
            throw new IOException("source file exists but is a directory");
        }
        if (dest.exists() && dest.isDirectory()) {
            dest = new File(dest, src.getName());
        }
        if (!dest.exists()) {
            dest.createNewFile();
        }
        FileChannel srcCH = null;
        FileChannel destCH = null;
        try {
            srcCH = new FileInputStream(src).getChannel();
            destCH = new FileOutputStream(dest).getChannel();
            destCH.transferFrom(srcCH, 0, srcCH.size());
        } finally {
            closeQuietly(srcCH);
            closeQuietly(destCH);
        }
        if (src.length() != dest.length()) {
            throw new IOException("Failed to copy full contents from '" + src + "' to '" + dest + "'");
        }
        if (preserveFileDate) {
            dest.setLastModified(src.lastModified());
        }
    }
