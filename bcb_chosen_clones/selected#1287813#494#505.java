    public static boolean copyFile(File src, File target) throws IOException {
        if (src == null || target == null || !src.exists()) return false;
        if (!target.exists()) if (!createNewFile(target)) return false;
        InputStream ins = new BufferedInputStream(new FileInputStream(src));
        OutputStream ops = new BufferedOutputStream(new FileOutputStream(target));
        int b;
        while (-1 != (b = ins.read())) ops.write(b);
        Streams.safeClose(ins);
        Streams.safeFlush(ops);
        Streams.safeClose(ops);
        return target.setLastModified(src.lastModified());
    }
