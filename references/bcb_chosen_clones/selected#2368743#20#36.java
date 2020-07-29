    public static void main(String[] args) throws IOException {
        FileChannel fc = new FileOutputStream("src/com/aaron/nio/data.txt").getChannel();
        fc.write(ByteBuffer.wrap("dfsdf ".getBytes()));
        fc.close();
        fc = new RandomAccessFile("src/com/aaron/nio/data.txt", "rw").getChannel();
        fc.position(fc.size());
        fc.write(ByteBuffer.wrap("中文的 ".getBytes()));
        fc.close();
        fc = new FileInputStream("src/com/aaron/nio/data.txt").getChannel();
        ByteBuffer buff = ByteBuffer.allocate(1024);
        fc.read(buff);
        buff.flip();
        while (buff.hasRemaining()) {
            System.out.print(buff.getChar());
        }
        fc.close();
    }
