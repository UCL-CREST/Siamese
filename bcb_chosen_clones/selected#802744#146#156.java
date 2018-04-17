    public static void copyFile(File src, File dest) throws IOException {
        if (!src.exists()) throw new IOException("File not found '" + src.getAbsolutePath() + "'");
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
        byte[] read = new byte[128];
        int len = 128;
        while ((len = in.read(read)) > 0) out.write(read, 0, len);
        out.flush();
        out.close();
        in.close();
    }
