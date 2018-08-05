    public void run() {
        FileInputStream src;
        FileOutputStream dest;
        try {
            dest = new FileOutputStream(srcName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        FileChannel destC = dest.getChannel();
        FileChannel srcC;
        ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
        try {
            int fileNo = 0;
            while (true) {
                int i = 1;
                String destName = srcName + "_" + fileNo;
                src = new FileInputStream(destName);
                srcC = src.getChannel();
                while ((i > 0)) {
                    i = srcC.read(buf);
                    buf.flip();
                    destC.write(buf);
                    buf.compact();
                }
                srcC.close();
                src.close();
                fileNo++;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
    }
