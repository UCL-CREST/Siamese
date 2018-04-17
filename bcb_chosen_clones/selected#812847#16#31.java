    public static void copyFiles(String strPath, String dstPath) throws IOException {
        File src = new File(strPath);
        File dest = new File(dstPath);
        if (src.isDirectory()) {
            if (dest.exists() != true) dest.mkdirs();
            String list[] = src.list();
            for (int i = 0; i < list.length; i++) {
                String dest1 = dest.getAbsolutePath() + File.separator + list[i];
                String src1 = src.getAbsolutePath() + File.separator + list[i];
                copyFiles(src1, dest1);
            }
        } else {
            boolean ready = dest.createNewFile();
            copy(src, dest);
        }
    }
