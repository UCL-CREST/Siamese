    public static void main(String[] args) throws IOException {
        long readfilelen = 0;
        BufferedRandomAccessFile brafReadFile, brafWriteFile;
        brafReadFile = new BufferedRandomAccessFile("C:\\WINNT\\Fonts\\STKAITI.TTF");
        readfilelen = brafReadFile.initfilelen;
        brafWriteFile = new BufferedRandomAccessFile(".\\STKAITI.001", "rw", 10);
        byte buf[] = new byte[1024];
        int readcount;
        long start = System.currentTimeMillis();
        while ((readcount = brafReadFile.read(buf)) != -1) {
            brafWriteFile.write(buf, 0, readcount);
        }
        brafWriteFile.close();
        brafReadFile.close();
        System.out.println("BufferedRandomAccessFile Copy & Write File: " + brafReadFile.filename + "    FileSize: " + java.lang.Integer.toString((int) readfilelen >> 1024) + " (KB)    " + "Spend: " + (double) (System.currentTimeMillis() - start) / 1000 + "(s)");
        java.io.FileInputStream fdin = new java.io.FileInputStream("C:\\WINNT\\Fonts\\STKAITI.TTF");
        java.io.BufferedInputStream bis = new java.io.BufferedInputStream(fdin, 1024);
        java.io.DataInputStream dis = new java.io.DataInputStream(bis);
        java.io.FileOutputStream fdout = new java.io.FileOutputStream(".\\STKAITI.002");
        java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(fdout, 1024);
        java.io.DataOutputStream dos = new java.io.DataOutputStream(bos);
        start = System.currentTimeMillis();
        for (int i = 0; i < readfilelen; i++) {
            dos.write(dis.readByte());
        }
        dos.close();
        dis.close();
        System.out.println("DataBufferedios Copy & Write File: " + brafReadFile.filename + "    FileSize: " + java.lang.Integer.toString((int) readfilelen >> 1024) + " (KB)    " + "Spend: " + (double) (System.currentTimeMillis() - start) / 1000 + "(s)");
    }
