    public static void copy(FileInputStream source, FileOutputStream dest) throws IOException {
        FileChannel in = null, out = null;
        try {
            in = source.getChannel();
            out = dest.getChannel();
            long size = in.size();
            MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
            out.write(buf);
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }
