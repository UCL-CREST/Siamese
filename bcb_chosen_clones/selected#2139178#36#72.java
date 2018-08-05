    public void run() {
        FileInputStream src;
        try {
            src = new FileInputStream(srcName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        FileOutputStream dest;
        FileChannel srcC = src.getChannel();
        ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
        try {
            int i = 1;
            int fileNo = 0;
            long maxByte = this.maxSize << 10;
            long nbByte = srcC.size();
            long nbFile = (nbByte / maxByte) + 1;
            for (fileNo = 0; fileNo < nbFile; fileNo++) {
                long fileByte = 0;
                String destName = srcName + "_" + fileNo;
                dest = new FileOutputStream(destName);
                FileChannel destC = dest.getChannel();
                while ((i > 0) && fileByte < maxByte) {
                    i = srcC.read(buf);
                    buf.flip();
                    fileByte += i;
                    destC.write(buf);
                    buf.compact();
                }
                destC.close();
                dest.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
    }
