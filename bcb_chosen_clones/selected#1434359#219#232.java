    public static void copyFile(File from, File to) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(from);
            out = new FileOutputStream(to);
            byte[] bytes = new byte[1024 * 4];
            int len = 0;
            while ((len = in.read(bytes)) >= 0) out.write(bytes, 0, len);
        } finally {
            Streams.closeQuietly(in);
            Streams.closeQuietly(out);
        }
    }
