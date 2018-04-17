    public void run() {
        FileInputStream src;
        FileOutputStream dest;
        try {
            src = new FileInputStream(srcName);
            dest = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        FileChannel srcC = src.getChannel();
        FileChannel destC = dest.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            int i;
            System.out.println(srcC.size());
            while ((i = srcC.read(buf)) > 0) {
                System.out.println(buf.getChar(2));
                buf.flip();
                destC.write(buf);
                buf.compact();
            }
            destC.close();
            dest.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
    }
