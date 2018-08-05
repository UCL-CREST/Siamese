    public static void main(String[] args) {
        try {
            Object o = Naming.lookup("Server");
            IServer serverStub = (IServer) o;
            File srcDir = new File(args[0]);
            File dstDir = new File(args[1]);
            File[] srcFiles = srcDir.listFiles();
            long position = 0;
            for (File f : srcFiles) {
                if (f.isDirectory()) continue;
                try {
                    FileChannel srcChannel = new FileInputStream(f).getChannel();
                    String fileName = dstDir.getAbsolutePath() + System.getProperty("file.separator") + f.getName();
                    FileChannel dstChannel = new FileOutputStream(fileName).getChannel();
                    System.out.println("Coping " + fileName);
                    ByteBuffer buffer = ByteBuffer.allocate(65536);
                    int nr = 0;
                    srcChannel.position(nr);
                    nr = srcChannel.read(buffer);
                    while (nr > -1) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.limit()];
                        buffer.get(bytes);
                        position = serverStub.write(bytes, position);
                        buffer.clear();
                        nr = srcChannel.read(buffer);
                    }
                    System.out.println("Done ");
                    srcChannel.close();
                    dstChannel.close();
                } catch (IOException e) {
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
