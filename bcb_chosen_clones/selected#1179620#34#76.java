    public void SendFile(File testfile) {
        try {
            SocketChannel sock = SocketChannel.open(new InetSocketAddress("127.0.0.1", 1234));
            sock.configureBlocking(true);
            while (!sock.finishConnect()) {
                System.out.println("NOT connected!");
            }
            System.out.println("CONNECTED!");
            FileInputStream fis = new FileInputStream(testfile);
            FileChannel fic = fis.getChannel();
            long len = fic.size();
            Buffer.clear();
            Buffer.putLong(len);
            Buffer.flip();
            sock.write(Buffer);
            long cnt = 0;
            while (cnt < len) {
                Buffer.clear();
                int add = fic.read(Buffer);
                cnt += add;
                Buffer.flip();
                while (Buffer.hasRemaining()) {
                    sock.write(Buffer);
                }
            }
            fic.close();
            File tmpfile = getTmp().createNewFile("tmp", "tmp");
            FileOutputStream fos = new FileOutputStream(tmpfile);
            FileChannel foc = fos.getChannel();
            int mlen = -1;
            do {
                Buffer.clear();
                mlen = sock.read(Buffer);
                Buffer.flip();
                if (mlen > 0) {
                    foc.write(Buffer);
                }
            } while (mlen > 0);
            foc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
