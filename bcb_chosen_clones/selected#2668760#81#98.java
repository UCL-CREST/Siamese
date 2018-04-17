    public static void copy(File src, File dest) throws IOException {
        if (dest.exists() && dest.isFile()) {
            logger.fine("cp " + src + " " + dest + " -- Destination file " + dest + " already exists. Deleting...");
            dest.delete();
        }
        final File parent = dest.getParentFile();
        if (!parent.exists()) {
            logger.info("Directory to contain destination does not exist. Creating...");
            parent.mkdirs();
        }
        final FileInputStream fis = new FileInputStream(src);
        final FileOutputStream fos = new FileOutputStream(dest);
        final byte[] b = new byte[2048];
        int n;
        while ((n = fis.read(b)) != -1) fos.write(b, 0, n);
        fis.close();
        fos.close();
    }
