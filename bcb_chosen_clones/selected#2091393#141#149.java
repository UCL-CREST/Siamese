    public static void copyFile(File src, File dst) throws IOException {
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(dst));
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = is.read(buf, 0, 1024)) != -1) os.write(buf, 0, count);
        is.close();
        os.close();
    }
