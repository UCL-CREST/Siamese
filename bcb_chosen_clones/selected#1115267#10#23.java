    public static void main(String args[]) throws Exception {
        FileInputStream fin = new FileInputStream("D:/work/test.txt");
        FileOutputStream fout = new FileOutputStream("D:/work/output.txt");
        FileChannel inChannel = fin.getChannel();
        FileChannel outChannel = fout.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            int ret = inChannel.read(buffer);
            if (ret == -1) break;
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }
    }
