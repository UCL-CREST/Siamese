    public static void copyFile(final File source, final File target) throws FileNotFoundException, IOException {
        FileChannel in = new FileInputStream(source).getChannel(), out = new FileOutputStream(target).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (in.read(buffer) != -1) {
            buffer.flip();
            out.write(buffer);
            buffer.clear();
        }
        out.close();
        in.close();
    }
