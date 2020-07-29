    public static boolean deleteDirectory(File dir) {
        AssertUtility.notNull(dir);
        boolean flag = true;
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                flag = deleteDirectory(file);
            } else {
                flag = file.delete();
            }
            if (!flag) {
                return false;
            }
        }
        flag = dir.delete();
        return flag;
    }
