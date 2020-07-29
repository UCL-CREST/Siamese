    public static void copy(final File source, final File dest) throws IOException {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            long size = in.size();
            MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
            out.write(buf);
            dest.setLastModified(source.lastModified());
        } finally {
            close(in);
            close(out);
        }
    }
