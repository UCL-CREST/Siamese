    @SuppressWarnings("null")
    public static void copyFile(File src, File dst) throws IOException {
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs();
        }
        dst.createNewFile();
        FileChannel srcC = null;
        FileChannel dstC = null;
        try {
            srcC = new FileInputStream(src).getChannel();
            dstC = new FileOutputStream(dst).getChannel();
            dstC.transferFrom(srcC, 0, srcC.size());
        } finally {
            try {
                if (dst != null) {
                    dstC.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (src != null) {
                    srcC.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
