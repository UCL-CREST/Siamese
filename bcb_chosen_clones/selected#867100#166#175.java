    public static void copy(File src, File dst) throws IOException {
        if (src.isDirectory()) {
            String[] srcChildren = src.list();
            for (int i = 0; i < srcChildren.length; ++i) {
                File srcChild = new File(src, srcChildren[i]);
                File dstChild = new File(dst, srcChildren[i]);
                copy(srcChild, dstChild);
            }
        } else transferData(src, dst);
    }
