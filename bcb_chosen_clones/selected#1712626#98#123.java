    public static boolean copyFile(File src, File dest) throws IOException {
        if (src == null) {
            throw new IllegalArgumentException("src == null");
        }
        if (dest == null) {
            throw new IllegalArgumentException("dest == null");
        }
        if (!src.isFile()) {
            return false;
        }
        FileChannel in = new FileInputStream(src).getChannel();
        FileChannel out = new FileOutputStream(dest).getChannel();
        try {
            in.transferTo(0, in.size(), out);
            return true;
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
