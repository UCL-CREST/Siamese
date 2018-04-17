    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.err.println("usage: test [src] [dest]");
                return;
            }
            FileInputStream in = new FileInputStream(args[0]);
            FileOutputStream out = new FileOutputStream(args[1]);
            FileChannel src = in.getChannel();
            FileChannel channel = out.getChannel();
            long pos = 0, len = src.size(), ret;
            while (len > 0) {
                if ((ret = channel.transferFrom(src, pos, len)) < 0) break;
                len -= ret;
                pos += ret;
            }
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
