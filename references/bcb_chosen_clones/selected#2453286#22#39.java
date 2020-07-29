    public static int deepDeleteChildren(File dir) {
        int deleted = 0;
        if (dir.isDirectory()) {
            File[] fs = dir.listFiles();
            for (File f : fs) {
                if (f.isDirectory()) {
                    deleted += deepDelete(f);
                } else {
                    boolean del = f.delete();
                    if (!del) {
                        throw new RuntimeException("cannot delete file " + f.getAbsolutePath());
                    }
                    deleted++;
                }
            }
        }
        return deleted;
    }
