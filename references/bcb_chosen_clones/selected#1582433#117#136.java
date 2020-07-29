    public static void main(String[] args) throws Exception {
        System.out.println("Opening destination cbrout.jizz");
        OutputStream out = new BufferedOutputStream(new FileOutputStream("cbrout.jizz"));
        System.out.println("Opening source output.jizz");
        InputStream in = new CbrLiveStream(new BufferedInputStream(new FileInputStream("output.jizz")), System.currentTimeMillis() + 10000, 128);
        System.out.println("Starting read/write loop");
        boolean started = false;
        int len;
        byte[] buf = new byte[4 * 1024];
        while ((len = in.read(buf)) > -1) {
            if (!started) {
                System.out.println("Starting at " + new Date());
                started = true;
            }
            out.write(buf, 0, len);
        }
        System.out.println("Finished at " + new Date());
        out.close();
        in.close();
    }
