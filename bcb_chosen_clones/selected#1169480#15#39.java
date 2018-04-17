    public static void main(String[] args) {
        File srcDir = new File(args[0]);
        File dstDir = new File(args[1]);
        File[] srcFiles = srcDir.listFiles();
        for (File f : srcFiles) {
            if (f.isDirectory()) continue;
            try {
                FileChannel srcChannel = new FileInputStream(f).getChannel();
                FileChannel dstChannel = new FileOutputStream(dstDir.getAbsolutePath() + System.getProperty("file.separator") + f.getName()).getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int nr = 0;
                srcChannel.position(nr);
                nr += srcChannel.read(buffer);
                while (nr < f.length()) {
                    buffer.flip();
                    dstChannel.write(buffer);
                    buffer.clear();
                    nr += srcChannel.read(buffer);
                }
                srcChannel.close();
                dstChannel.close();
            } catch (IOException e) {
            }
        }
    }
