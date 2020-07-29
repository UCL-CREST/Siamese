    public static void copy(File src, File dst) throws IOException {
        if (src.isDirectory()) {
            dst.mkdirs();
            File[] children = src.listFiles();
            for (int i = 0; i < children.length; i++) {
                copy(children[i], new File(dst.getAbsolutePath() + File.separator + children[i].getName()));
            }
        } else {
            if (!dst.exists()) {
                dst.getParentFile().mkdirs();
                dst.createNewFile();
            }
            FileInputStream is = new FileInputStream(src);
            FileOutputStream os = new FileOutputStream(dst);
            os.getChannel().transferFrom(is.getChannel(), 0, src.length());
            is.close();
            os.close();
        }
    }
