    public static void copy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(target).getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER);
            while (in.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    out.write(buffer);
                }
                buffer.clear();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            close(in);
            close(out);
        }
    }
