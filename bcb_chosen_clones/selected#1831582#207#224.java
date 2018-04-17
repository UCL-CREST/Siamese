    public static void copyFile(File source, File destination) throws IOException {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(destination).getChannel();
            long size = in.size();
            MappedByteBuffer buffer = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
            out.write(buffer);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
